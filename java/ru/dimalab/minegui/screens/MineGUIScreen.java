package ru.dimalab.minegui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import ru.dimalab.minegui.manager.ScreenManager;
import ru.dimalab.minegui.screens.utils.CollapsibleWindow;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.utils.KeyHelper;
import ru.dimalab.minegui.utils.json.MineGUIWidgetConfigManager;
import ru.dimalab.minegui.utils.json.ScreenConfig;
import ru.dimalab.minegui.utils.json.WidgetData;
import ru.dimalab.minegui.widgets.MineGUIButton;
import ru.dimalab.minegui.widgets.MineGUIText;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class MineGUIScreen extends Screen {
    private int x;
    private int y;

    protected final Minecraft minecraftInstance;
    protected final List<MineGUIWidget> widgets = new ArrayList<>();

    private MineGUIWidget draggingWidget = null;
    private int dragOffsetX, dragOffsetY;

    private MineGUIColorPalette backgroundColor;
    private ResourceLocation backgroundTexture;
    private float backgroundOpacity = 1.0f;

    private List<MineGUIColorPalette> gradientColors;
    private boolean useGradient = false;

    private static final String CONFIG_FILE = "minegui_widgets";

    public MineGUIScreen(Minecraft mc, String title) {
        super(Component.literal(title));
        this.minecraftInstance = mc;
        this.minecraft = mc;
        this.backgroundColor = MineGUIColorPalette.TRANSPARENT;
    }

    protected abstract void setupWidgets();

    @Override
    protected void init() {
        super.init();
        widgets.clear();
        setupWidgets();
        saveWidgets();
    }

    @Override
    public void tick() {
        super.tick();

        for (MineGUIWidget widget : widgets) {
            widget.tick();
        }
    }

    public void addWidgets(MineGUIWidget... widgetsToAdd) {
        Collections.addAll(widgets, widgetsToAdd);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null) return;

        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for (MineGUIWidget widget : widgets) {
            widget.render(guiGraphics, this.font, mouseX, mouseY);
        }

        widgets.stream()
                .map(widget -> widget.getTooltip(mouseX, mouseY))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .ifPresent(tooltip -> {
                    int x = mouseX + 12;
                    int y = mouseY - 12;
                    tooltip.render(guiGraphics, font, x, y);
                });
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        for (MineGUIWidget widget : widgets) {
            if (widget.mouseScrolled(mouseX, mouseY, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (backgroundTexture != null) {
            guiGraphics.blit(backgroundTexture, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        } else if (useGradient && gradientColors != null && !gradientColors.isEmpty()) {
            renderGradientBackground(guiGraphics);
        } else if (backgroundColor != null) {
            int color = backgroundColor.getColor();
            int alpha = (int) (backgroundOpacity * 255) << 24;
            guiGraphics.fill(0, 0, this.width, this.height, (color & 0x00FFFFFF) | alpha);
        } else {
            guiGraphics.fill(0, 0, this.width, this.height, (0xFF000000 & 0x00FFFFFF) | ((int) (backgroundOpacity * 255) << 24));
        }
    }

    private void renderGradientBackground(GuiGraphics guiGraphics) {
        int alpha = (int) (backgroundOpacity * 255) << 24;

        if (gradientColors.size() == 2) {
            int startColor = (gradientColors.get(0).getColor() & 0x00FFFFFF) | alpha;
            int endColor = (gradientColors.get(1).getColor() & 0x00FFFFFF) | alpha;
            guiGraphics.fillGradient(0, 0, this.width, this.height, startColor, endColor);
        }

        else if (gradientColors.size() > 2) {
            int segmentHeight = this.height / (gradientColors.size() - 1);
            for (int i = 0; i < gradientColors.size() - 1; i++) {
                int yStart = i * segmentHeight;
                int yEnd = (i + 1) * segmentHeight;
                int colorStart = (gradientColors.get(i).getColor() & 0x00FFFFFF) | alpha;
                int colorEnd = (gradientColors.get(i + 1).getColor() & 0x00FFFFFF) | alpha;
                guiGraphics.fillGradient(0, yStart, this.width, yEnd, colorStart, colorEnd);
            }
        }
    }

    void saveWidgets() {
        ScreenConfig config = new ScreenConfig();

        for (MineGUIWidget widget : widgets) {
            WidgetData data = new WidgetData();

            if (widget instanceof MineGUIText textWidget) {
                data.type = "text";
                data.x = textWidget.getX();
                data.y = textWidget.getY();
                data.text = textWidget.text.getString();
            } else if (widget instanceof MineGUIButton button) {
                data.type = "button";
                data.x = button.getX();
                data.y = button.getY();
                data.buttonText = button.text.getString();
            }

            config.widgets.add(data);
        }

        MineGUIWidgetConfigManager.saveConfig(config, CONFIG_FILE);
    }

    private void loadWidgets() {
        ScreenConfig config = MineGUIWidgetConfigManager.loadConfig(CONFIG_FILE);
        widgets.clear();

        for (WidgetData data : config.widgets) {
            switch (data.type) {
                case "text":
                    MineGUIText text = new MineGUIText.Builder()
                            .setText(Component.literal(data.text))
                            .setPosition(data.x, data.y)
                            .build();
                    widgets.add(text);
                    break;
                case "button":
                    MineGUIButton button = new MineGUIButton.Builder()
                            //.setPosition(data.x, data.y)
                            .setText(Component.literal(data.buttonText))
                            .build();
                    widgets.add(button);
                    break;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = false;



            for (MineGUIWidget widget : widgets) {
                if (widget.mouseClicked((int) mouseX, (int) mouseY, button)) {
                    handled = true;

                    draggingWidget = widget;
                    dragOffsetX = (int) mouseX - widget.getX();
                    dragOffsetY = (int) mouseY - widget.getY();

                    setDragging(true);
                }
            }


            for (MineGUIWidget widget : widgets) {
                boolean isHovered = isMouseOver(widget, (int) mouseX, (int) mouseY);
                if (isHovered && button == 0) { // ЛКМ
                    draggingWidget = widget;
                    dragOffsetX = (int) mouseX - widget.getX();
                    dragOffsetY = (int) mouseY - widget.getY();

                    setDragging(true);
                    // MineGUI.LOGGER.info("Начали перетаскивание: {}", widget);

                    widget.mouseClicked((int) mouseX, (int) mouseY, button);

                    return true; // перехватываем событие
                }

            }



        return handled || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        updateWidgetsPosition();
        saveWidgets();
    }

    private void updateWidgetsPosition() {
        int screenWidth = this.width;
        int screenHeight = this.height;

        for (MineGUIWidget widget : widgets) {

            int x = Math.max(0, Math.min(widget.getX(), screenWidth - widget.getWidth()));
            int y = Math.max(0, Math.min(widget.getY(), screenHeight - widget.getHeight()));

            widget.setPosition(x, y);
            widget.updatePosition(screenWidth, screenHeight);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingWidget != null) {
            draggingWidget.mouseReleased((int) mouseX, (int) mouseY, button);
            draggingWidget = null;
            setDragging(false);
            return true;
        }


        for (MineGUIWidget widget : widgets) {
            widget.mouseReleased((int) mouseX, (int) mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean handled = false;

        for (MineGUIWidget widget : widgets) {
            handled |= widget.keyPressed(keyCode, scanCode, modifiers);
        }

        return handled || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingWidget != null && button == 0) {
            int newX = (int) mouseX - dragOffsetX;
            int newY = (int) mouseY - dragOffsetY;

            newX = Math.max(0, Math.min(newX, this.width - draggingWidget.getWidth()));
            newY = Math.max(0, Math.min(newY, this.height - draggingWidget.getHeight()));

            draggingWidget.setPosition(newX, newY);
            draggingWidget.mouseDragged((int) mouseX, (int) mouseY, button, deltaX, deltaY);

            //MineGUI.LOGGER.info("Перетаскиваем виджет: {}", draggingWidget);

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (MineGUIWidget widget : widgets) {
            if (widget.charTyped(codePoint, modifiers)) {
                return true;
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    private boolean isMouseOver(MineGUIWidget widget, int mouseX, int mouseY) {
        return mouseX >= widget.getX() &&
                mouseY >= widget.getY() &&
                mouseX < widget.getX() + widget.getWidth() &&
                mouseY < widget.getY() + widget.getHeight();
    }


    public void setBackgroundColor(MineGUIColorPalette color) {
        this.backgroundColor = color;
        this.backgroundTexture = null;
    }

    public void setBackgroundTexture(ResourceLocation texture) {
        this.backgroundTexture = texture;
        this.backgroundColor = null;
    }

    public void setBackgroundOpacity(float opacity) {
        this.backgroundOpacity = Math.max(0.0f, Math.min(1.0f, opacity));
    }

    public void setGradientBackground(List<MineGUIColorPalette> colors) {
        this.gradientColors = new ArrayList<>(colors);
        this.useGradient = true;
        this.backgroundColor = null;
        this.backgroundTexture = null;
    }

    public void clearGradient() {
        this.useGradient = false;
        this.gradientColors = null;
    }


    @Override
    public void onClose() {
        ScreenManager.closeScreen();
    }
}
