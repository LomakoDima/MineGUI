package ru.dimalab.minegui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.dimalab.minegui.utils.TextStyle;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIButton implements MineGUIWidget {
    private final int x, y, width, height;
    private final Component text;
    private final Component hoverText;
    private final Runnable onClick;
    private final int solidColor;
    private final int hoverSolidColor;
    private final int gradientStartColor;
    private final int gradientEndColor;
    private final int hoverGradientStartColor;
    private final int hoverGradientEndColor;
    private final boolean useGradient;
    private final int borderColor;
    private final int textColor;
    private final int borderThickness;
    private final ResourceLocation imageNormal;
    private final ResourceLocation imageHover;
    private final TextStyle textStyle;
    private final TextStyle hoverTextStyle;
    private final float textSize;
    private final float hoverTextSize;
    private boolean isHovered = false;

    public MineGUIButton(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
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
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, int mouseX, int mouseY) {
        isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        // Отрисовка фона кнопки
        if (isHovered && imageHover != null) {
            guiGraphics.blit(imageHover, x, y, 0, 0, width, height, width, height);
        } else if (imageNormal != null) {
            guiGraphics.blit(imageNormal, x, y, 0, 0, width, height, width, height);
        } else {
            if (useGradient) {
                int startColor = isHovered ? hoverGradientStartColor : gradientStartColor;
                int endColor = isHovered ? hoverGradientEndColor : gradientEndColor;
                guiGraphics.fillGradient(x, y, x + width, y + height, startColor, endColor);
            } else {
                int color = isHovered ? hoverSolidColor : solidColor;
                guiGraphics.fill(x, y, x + width, y + height, color);
            }
        }

        for (int i = 0; i < borderThickness; i++) {
            guiGraphics.renderOutline(x - i, y - i, width + i * 2, height + i * 2, borderColor);
        }

        String styledText = isHovered && hoverTextStyle != null
                ? hoverTextStyle.apply(hoverText.getString())
                : textStyle != null
                ? textStyle.apply(text.getString())
                : text.getString();

        float textSizeToUse = isHovered ? hoverTextSize : textSize;
        float scaleFactor = textSizeToUse / 9.0f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleFactor, scaleFactor, 1.0f);
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                Component.literal(styledText),
                (int) ((x + width / 2) / scaleFactor),
                (int) ((y + height / 2 - 4) / scaleFactor),
                textColor
        );
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isHovered) {
            onClick.run();
            return true;
        }
        return false;
    }

    public static class Builder {
        private int x, y, width, height;
        private Component text = Component.literal("Button");
        private Component hoverText = null;
        private Runnable onClick = () -> {};
        private int solidColor;
        private int hoverSolidColor;
        private int gradientStartColor;
        private int gradientEndColor;
        private int hoverGradientStartColor;
        private int hoverGradientEndColor;
        private boolean useGradient;
        private int borderColor;
        private int textColor;
        private int borderThickness;
        private ResourceLocation imageNormal = null;
        private ResourceLocation imageHover = null;
        private TextStyle textStyle = null;
        private TextStyle hoverTextStyle = null;
        private float textSize = 9.0f;
        private float hoverTextSize = 9.0f;

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
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

        public Builder setSolidColor(int color, int hoverColor) {
            this.solidColor = color;
            this.hoverSolidColor = hoverColor;
            this.useGradient = false;
            return this;
        }

        public Builder setGradientColors(int start, int end, int hoverStart, int hoverEnd) {
            this.gradientStartColor = start;
            this.gradientEndColor = end;
            this.hoverGradientStartColor = hoverStart;
            this.hoverGradientEndColor = hoverEnd;
            this.useGradient = true;
            return this;
        }

        public Builder setBorderColor(int color) {
            this.borderColor = color;
            return this;
        }

        public Builder setTextColor(int color) {
            this.textColor = color;
            return this;
        }

        public Builder setBorderThickness(int thickness) {
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

        public MineGUIButton build() {
            return new MineGUIButton(this);
        }
    }
}
