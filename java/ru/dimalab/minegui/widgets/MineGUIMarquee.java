package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIMarquee implements MineGUIWidget {

    private final int x, y;
    private final int width;
    private final MineGUIColorPalette color;
    private final boolean useGradient;
    private final int gradientStartColor;
    private final int gradientEndColor;

    private final String text;
    private float offset;
    private final float speed;

    private Font cachedFont;

    private MineGUIMarquee(Builder builder) {
        this.text = builder.text;
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.color = builder.color;
        this.gradientStartColor = builder.gradientStartColor;
        this.gradientEndColor = builder.gradientEndColor;
        this.useGradient = builder.useGradient;
        this.speed = builder.speed;
        this.offset = 0;
    }

    @Override
    public void tick() {
        if (cachedFont == null) return;

        int textWidth = cachedFont.width(text);

        offset -= speed;

        if (offset < -textWidth) {
            offset = width;
        }

    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (cachedFont == null) {
            cachedFont = font;
        }


        graphics.enableScissor(x, y, x + width, y + font.lineHeight);

        if (useGradient) {
            drawGradientText(graphics, font, x + (int) offset, y);
        } else {
            graphics.drawString(font, Component.literal(text), x + (int) offset, y, color.getColor(), false);
        }

        graphics.disableScissor();
    }

    private void drawGradientText(GuiGraphics graphics, Font font, int xPos, int yPos) {
        int textWidth = font.width(text);

        for (int i = 0; i < textWidth; i++) {
            float ratio = (float) i / Math.max(textWidth - 1, 1);
            int blendedColor = blendColors(gradientStartColor, gradientEndColor, ratio);

            String partialText = text.substring(0, Math.min(text.length(), i / (font.width(" ") / 2)));

            graphics.drawString(font, Component.literal(partialText), xPos, yPos, blendedColor, false);
        }

        graphics.drawString(font, Component.literal(text), xPos, yPos, gradientStartColor, false);
    }

    private int blendColors(int color1, int color2, float ratio) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (r << 16) | (g << 8) | b;
    }

    public static class Builder {
        private String text = "";
        private int x = 0;
        private int y = 0;
        private int width = 100;
        private MineGUIColorPalette color = MineGUIColorPalette.WHITE; // Белый
        private int gradientStartColor = 0xFFFFFFFF;
        private int gradientEndColor = 0xFFFFFFFF;
        private boolean useGradient = false;
        private float speed = 1.0f;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setColor(MineGUIColorPalette color) {
            this.color = color;
            return this;
        }

        public Builder setUseGradient(boolean useGradient) {
            this.useGradient = useGradient;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public MineGUIMarquee build() {
            return new MineGUIMarquee(this);
        }
    }
}
