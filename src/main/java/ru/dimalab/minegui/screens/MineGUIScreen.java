package ru.dimalab.minegui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.manager.ScreenManager;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class MineGUIScreen extends Screen {

    protected final Minecraft minecraftInstance;
    protected final List<MineGUIWidget> widgets = new ArrayList<>();

    public MineGUIScreen(Minecraft mc, String title) {
        super(Component.literal(title));
        this.minecraftInstance = mc;
        this.minecraft = mc; // важно для Screen
    }

    protected abstract void initWidgets();

    @Override
    protected void init() {
        super.init();
        widgets.clear();
        initWidgets(); // инициализация виджетов от наследников
    }

    @Override
    public void tick() {
        super.tick();
        for (MineGUIWidget widget : widgets) {
            widget.tick();
        }
    }

    public void addWidgets(MineGUIWidget... widgetsToAdd) {
        for (MineGUIWidget widget : widgetsToAdd) {
            widgets.add(widget);
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null) {
            return;
        }

        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for (MineGUIWidget widget : widgets) {
            widget.render(guiGraphics, this.font, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (MineGUIWidget widget : widgets) {
            if (widget.mouseClicked((int) mouseX, (int) mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (MineGUIWidget widget : widgets) {
            if (widget.mouseReleased((int) mouseX, (int) mouseY, button)) {
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (MineGUIWidget widget : widgets) {
            if (widget.mouseDragged((int) mouseX, (int) mouseY, button, dragX, dragY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (MineGUIWidget widget : widgets) {
            if (widget.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        ScreenManager.closeScreen();
    }
}
