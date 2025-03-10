package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIText implements MineGUIWidget {
    private final String text;
    private final int x, y;
    private final int color;
    private final int gradientStartColor;
    private final int gradientEndColor;
    private final boolean useGradient;

    private MineGUIText(Builder builder) {
        this.text = builder.text;
        this.x = builder.x;
        this.y = builder.y;
        this.color = builder.color;
        this.gradientStartColor = builder.gradientStartColor;
        this.gradientEndColor = builder.gradientEndColor;
        this.useGradient = builder.useGradient;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (useGradient) {
            drawGradientText(graphics, font, text, x, y, gradientStartColor, gradientEndColor);
        } else {
            graphics.drawString(font, text, x, y, color);
        }
    }

    private void drawGradientText(GuiGraphics graphics, Font font, String text, int x, int y, int startColor, int endColor) {
        int length = text.length();
        for (int i = 0; i < length; i++) {
            float ratio = (float) i / Math.max(length - 1, 1);
            int color = blendColors(startColor, endColor, ratio);
            graphics.drawString(font, String.valueOf(text.charAt(i)), x + font.width(text.substring(0, i)), y, color);
        }
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

    public static class Builder {
        private String text;
        private int x, y;
        private int color = 0xFFFFFF;
        private int gradientStartColor;
        private int gradientEndColor;
        private boolean useGradient = false;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            this.useGradient = false;
            return this;
        }

        public Builder setGradientColors(int startColor, int endColor) {
            this.gradientStartColor = startColor;
            this.gradientEndColor = endColor;
            this.useGradient = true;
            return this;
        }

        public MineGUIText build() {
            return new MineGUIText(this);
        }
    }

}
