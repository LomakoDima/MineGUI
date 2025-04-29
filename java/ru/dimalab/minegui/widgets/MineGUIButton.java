package ru.dimalab.minegui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.lwjgl.glfw.GLFW;
import ru.dimalab.minegui.screens.tooltips.Tooltip;
import ru.dimalab.minegui.utils.Alignment;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.utils.TextAlignment;
import ru.dimalab.minegui.utils.TextStyle;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.List;
import java.util.Optional;

public class MineGUIButton implements MineGUIWidget {
    private int x, y, width, height;
    private final int textXOffset;
    private final int textYOffset;
    public final Component text;
    private final Component hoverText;
    private final Runnable onClick;
    private final MineGUIColorPalette solidColor;
    private final MineGUIColorPalette hoverSolidColor;
    private final MineGUIColorPalette gradientStartColor;
    private final MineGUIColorPalette gradientEndColor;
    private final MineGUIColorPalette hoverGradientStartColor;
    private final MineGUIColorPalette hoverGradientEndColor;
    private final boolean useGradient;
    private final MineGUIColorPalette borderColor;
    private final MineGUIColorPalette textColor;
    private final MineGUIColorPalette borderThickness;
    private final ResourceLocation imageNormal;
    private final ResourceLocation imageHover;
    private final TextStyle textStyle;
    private final TextStyle hoverTextStyle;
    private final float textSize;
    private final float hoverTextSize;
    private final MineGUIColorPalette textGradientStartColor;
    private final MineGUIColorPalette textGradientEndColor;
    private final MineGUIColorPalette hoverTextGradientStartColor;
    private final MineGUIColorPalette hoverTextGradientEndColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    private MineGUIColorPalette pressedSolidColor;
    private MineGUIColorPalette pressedGradientStartColor;
    private MineGUIColorPalette pressedGradientEndColor;
    private ResourceLocation imagePressed;

    private final int maxWidth;

    private final Tooltip tooltip;

    private final TextAlignment textAlignment;
    private final int offsetX;
    private final int offsetY;
    private final Alignment buttonAlignment;

    private static long handCursor = -1;
    private static long arrowCursor = -1;

    public MineGUIButton(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.textXOffset = builder.textXOffset;
        this.textYOffset = builder.textYOffset;
        this.text = builder.text;
        this.hoverText = builder.hoverText != null ? builder.hoverText : builder.text;
        this.onClick = builder.onClick;
        this.solidColor = builder.solidColor;
        this.hoverSolidColor = builder.hoverSolidColor;
        this.gradientStartColor = builder.gradientStartColor;
        this.gradientEndColor = builder.gradientEndColor;
        this.hoverGradientStartColor = builder.hoverGradientStartColor;
        this.hoverGradientEndColor = builder.hoverGradientEndColor;
        this.useGradient = builder.useGradient;
        this.borderColor = builder.borderColor;
        this.textColor = builder.textColor;
        this.borderThickness = builder.borderThickness;
        this.imageNormal = builder.imageNormal;
        this.imageHover = builder.imageHover;
        this.textStyle = builder.textStyle;
        this.hoverTextStyle = builder.hoverTextStyle;
        this.textSize = builder.textSize;
        this.hoverTextSize = builder.hoverTextSize;
        this.textGradientStartColor = builder.textGradientStartColor;
        this.textGradientEndColor = builder.textGradientEndColor;
        this.hoverTextGradientStartColor = builder.hoverTextGradientStartColor;
        this.hoverTextGradientEndColor = builder.hoverTextGradientEndColor;
        this.tooltip = builder.tooltip;
        this.pressedSolidColor = builder.pressedSolidColor;
        this.pressedGradientStartColor = builder.pressedGradientStartColor;
        this.pressedGradientEndColor = builder.pressedGradientEndColor;
        this.imagePressed = builder.imagePressed;
        this.maxWidth = builder.maxWidth;
        this.textAlignment = builder.textAlignment;
        this.offsetX = builder.xOffset;
        this.offsetY = builder.yOffset;
        this.buttonAlignment = builder.buttonAlignment;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, int mouseX, int mouseY) {
        isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        if (handCursor == -1 || arrowCursor == -1) {
            handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
            arrowCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        }

        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        if (isHovered) {
            GLFW.glfwSetCursor(windowHandle, handCursor);
        } else {
            GLFW.glfwSetCursor(windowHandle, arrowCursor);
        }

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        calculateAlignedPosition(screenWidth, screenHeight); // Вычисляем x, y здесь

        if (isHovered && imageHover != null) {
            guiGraphics.blit(imageHover, x, y, 0, 0, width, height, width, height);
        } else if (imageNormal != null) {
            guiGraphics.blit(imageNormal, x, y, 0, 0, width, height, width, height);
        } else {
            if (useGradient) {
                int startColor = isHovered ? hoverGradientStartColor.getColor() : gradientStartColor.getColor();
                int endColor = isHovered ? hoverGradientEndColor.getColor() : gradientEndColor.getColor();
                guiGraphics.fillGradient(x, y, x + width, y + height, startColor, endColor);
            } else {
                int color = isHovered ? hoverSolidColor.getColor() : solidColor.getColor();
                guiGraphics.fill(x, y, x + width, y + height, color);
            }
        }

        boolean usePressedState = isPressed && isHovered;

        if (usePressedState && imagePressed != null) {
            guiGraphics.blit(imagePressed, x, y, 0, 0, width, height, width, height);
        } else if (isHovered && imageHover != null) {
            guiGraphics.blit(imageHover, x, y, 0, 0, width, height, width, height);
        } else if (imageNormal != null) {
            guiGraphics.blit(imageNormal, x, y, 0, 0, width, height, width, height);
        } else {
            if (useGradient) {
                int startColor = usePressedState ? pressedGradientStartColor.getColor()
                        : (isHovered ? hoverGradientStartColor.getColor() : gradientStartColor.getColor());
                int endColor = usePressedState ? pressedGradientEndColor.getColor()
                        : (isHovered ? hoverGradientEndColor.getColor() : gradientEndColor.getColor());
                guiGraphics.fillGradient(x, y, x + width, y + height, startColor, endColor);
            } else {
                int color = usePressedState ? pressedSolidColor.getColor()
                        : (isHovered ? hoverSolidColor.getColor() : solidColor.getColor());
                guiGraphics.fill(x, y, x + width, y + height, color);
            }
        }

        for (int i = 0; i < borderThickness.getColor(); i++) {
            guiGraphics.renderOutline(x - i, y - i, width + i * 2, height + i * 2, borderColor.getColor());
        }

        float textSizeToUse = isHovered ? hoverTextSize : textSize;
        float scaleFactor = textSizeToUse / 9.0f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleFactor, scaleFactor, 1.0f);

        //int textX = (int) ((x + textXOffset) / scaleFactor);
        //int textY = (int) ((y + textYOffset) / scaleFactor);

        Component textToRender = isHovered ? hoverText : text;
        List<FormattedText> lines = font.getSplitter().splitLines(textToRender, (int) (maxWidth / scaleFactor), Style.EMPTY);
        int lineHeight = font.lineHeight + 2; // Возможно, стоит использовать просто font.lineHeight
        int totalTextHeight = (lines.size() * font.lineHeight + (lines.size() - 1) * 2) ;

        int startY = (int) ((y + (height - totalTextHeight * scaleFactor) / 2 + textYOffset) / scaleFactor);

        int currentY = startY;
        for (FormattedText line : lines) {
            String styledText = isHovered && hoverTextStyle != null
                    ? hoverTextStyle.apply(line.getString())
                    : textStyle != null
                    ? textStyle.apply(line.getString())
                    : line.getString();

            int lineWidth = font.width(styledText);
            float scaledLineWidth = lineWidth * scaleFactor;

            float relativeX;
            switch (textAlignment) {
                case CENTER:
                    relativeX = (width - scaledLineWidth) / 2f + textXOffset;
                    break;
                case LEFT:
                    relativeX = textXOffset;
                    break;
                case RIGHT:
                    relativeX = width - scaledLineWidth - textXOffset;
                    break;
                default:
                    relativeX = textXOffset;
            }
            int textX1 = (int) ((x + relativeX) / scaleFactor);

            int startColor = isHovered ? hoverTextGradientStartColor.getColor() : textGradientStartColor.getColor();
            int endColor = isHovered ? hoverTextGradientEndColor.getColor() : textGradientEndColor.getColor();

            if (startColor != 0 && endColor != 0) {
                drawGradientText(guiGraphics, font, styledText, textX1, currentY);
            } else {
                guiGraphics.drawString(font, styledText, textX1, currentY, textColor.getColor(), false);
            }
            currentY += lineHeight;
        }

        guiGraphics.pose().popPose();
    }

    private void drawGradientText(GuiGraphics graphics, Font font, String text, int x, int y) {
        int length = text.length();
        int currentX = x;

        for (int i = 0; i < length; i++) {
            float ratio = (float) i / Math.max(length - 1, 1);
            int blendedColor = blendColors(
                    isHovered ? hoverTextGradientStartColor.getColor() : textGradientStartColor.getColor(),
                    isHovered ? hoverTextGradientEndColor.getColor() : textGradientEndColor.getColor(),
                    ratio
            );
            graphics.drawString(font, String.valueOf(text.charAt(i)), currentX, y, blendedColor, false);
            currentX += font.width(String.valueOf(text.charAt(i)));
        }
    }

    private void calculateAlignedPosition(int screenWidth, int screenHeight) {
        int baseX = 0;
        int baseY = 0;

        switch (this.buttonAlignment) {
            case TOP_LEFT:
            case CENTER_LEFT:
            case BOTTOM_LEFT:
                baseX = 0;
                break;
            case TOP_CENTER:
            case CENTER:
            case BOTTOM_CENTER:
                baseX = screenWidth / 2 - this.width / 2;
                break;
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                baseX = screenWidth - this.width;
                break;
        }

        switch (this.buttonAlignment) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                baseY = 0;
                break;
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT:
                baseY = screenHeight / 2 - this.height / 2;
                break;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                baseY = screenHeight - this.height;
                break;
        }

        this.x = baseX + this.offsetX;
        this.y = baseY + this.offsetY;
    }

    private int blendColors(int color1, int color2, float ratio) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = (color2 & 0xFF);

        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (r << 16) | (g << 8) | b;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isHovered) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            onClick.run();
            isPressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        isPressed = false; // Сбрасываем состояние при отпускании
        return false;
    }

    @Override
    public Optional<Tooltip> getTooltip(int mouseX, int mouseY) {
        if (isHovered && tooltip != null) {
            return Optional.of(tooltip);
        }
        return Optional.empty();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static class Builder {
        private int x, y, width, height;
        private int xOffset = 0, yOffset = 0;
        private int textXOffset;
        private int textYOffset;
        private Component text = Component.literal("Button");
        private Component hoverText = null;
        private Runnable onClick = () -> {};
        private MineGUIColorPalette solidColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette hoverSolidColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette gradientStartColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette gradientEndColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette hoverGradientStartColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette hoverGradientEndColor = MineGUIColorPalette.TRANSPARENT;
        private boolean useGradient;
        private MineGUIColorPalette borderColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette textColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette borderThickness = MineGUIColorPalette.TRANSPARENT;
        private ResourceLocation imageNormal = null;
        private ResourceLocation imageHover = null;
        private TextStyle textStyle = null;
        private TextStyle hoverTextStyle = null;
        private float textSize = 9.0f;
        private float hoverTextSize = 9.0f;
        private MineGUIColorPalette textGradientStartColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette textGradientEndColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette hoverTextGradientStartColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette hoverTextGradientEndColor = MineGUIColorPalette.TRANSPARENT;
        private Tooltip tooltip = null;

        private MineGUIColorPalette pressedSolidColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette pressedGradientStartColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette pressedGradientEndColor = MineGUIColorPalette.TRANSPARENT;
        private ResourceLocation imagePressed = null;

        private int maxWidth = Integer.MAX_VALUE;

        private TextAlignment textAlignment = TextAlignment.LEFT;
        private Alignment buttonAlignment = Alignment.TOP_LEFT;

        public Builder setOffset(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setText(Component text) {
            this.text = text;
            return this;
        }

        public Builder setHoverText(Component hoverText) {
            this.hoverText = hoverText;
            return this;
        }

        public Builder setOnClick(Runnable onClick) {
            this.onClick = onClick;
            return this;
        }

        public Builder setSolidColor(MineGUIColorPalette color, MineGUIColorPalette hoverColor) {
            this.solidColor = color;
            this.hoverSolidColor = hoverColor;
            this.useGradient = false;
            return this;
        }

        public Builder setGradientColors(MineGUIColorPalette start, MineGUIColorPalette end, MineGUIColorPalette hoverStart, MineGUIColorPalette hoverEnd) {
            this.gradientStartColor = start;
            this.gradientEndColor = end;
            this.hoverGradientStartColor = hoverStart;
            this.hoverGradientEndColor = hoverEnd;
            this.useGradient = true;
            return this;
        }

        public Builder setBorderColor(MineGUIColorPalette color) {
            this.borderColor = color;
            return this;
        }

        public Builder setTextColor(MineGUIColorPalette color) {
            this.textColor = color;
            return this;
        }

        public Builder setBorderThickness(MineGUIColorPalette thickness) {
            this.borderThickness = thickness;
            return this;
        }

        public Builder setImages(String normalPath, String hoverPath) {
            this.imageNormal = normalPath != null ? ResourceLocation.bySeparator(normalPath, ':') : null;
            this.imageHover = hoverPath != null ? ResourceLocation.bySeparator(hoverPath, ':') : null;
            return this;
        }

        public Builder setTextStyle(TextStyle style) {
            this.textStyle = style;
            return this;
        }

        public Builder setHoverTextStyle(TextStyle style) {
            this.hoverTextStyle = style;
            return this;
        }

        public Builder setTextSize(float size) {
            this.textSize = size;
            return this;
        }

        public Builder setHoverTextSize(float size) {
            this.hoverTextSize = size;
            return this;
        }

        public Builder setTextGradient(MineGUIColorPalette startColor, MineGUIColorPalette endColor) {
            this.textGradientStartColor = startColor;
            this.textGradientEndColor = endColor;
            return this;
        }

        public Builder setHoverTextGradient(MineGUIColorPalette startColor, MineGUIColorPalette endColor) {
            this.hoverTextGradientStartColor = startColor;
            this.hoverTextGradientEndColor = endColor;
            return this;
        }

        public Builder setTooltip(Tooltip tooltip) { // Новый метод для установки подсказки
            this.tooltip = tooltip;
            return this;
        }

        public Builder setPressedSolidColor(MineGUIColorPalette color) {
            this.pressedSolidColor = color;
            return this;
        }

        public Builder setPressedGradientColors(MineGUIColorPalette start, MineGUIColorPalette end) {
            this.pressedGradientStartColor = start;
            this.pressedGradientEndColor = end;
            return this;
        }

        public Builder setPressedImage(String imagePath) {
            this.imagePressed = imagePath != null ? ResourceLocation.bySeparator(imagePath, ':') : null;
            return this;
        }

        public Builder setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder setTextAlignment(TextAlignment alignment) {
            this.textAlignment = alignment;
            return this;
        }

        public Builder setButtonAlignment(Alignment alignment) {
            this.buttonAlignment = alignment;
            return this;
        }

        public MineGUIButton build() {
            return new MineGUIButton(this);
        }
    }
}

