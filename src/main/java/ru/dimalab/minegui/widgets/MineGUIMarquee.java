package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MineGUIMarquee {
    private final int x, y;
    private final int width;
    private final int color;
    private final boolean useGradient;
    private final int gradientStartColor;
    private final int gradientEndColor;

    private final String text;
    private float offset;
    private final float speed;


    public MineGUIMarquee(String text, int x, int y, int width, int color, int gradientStartColor, int gradientEndColor, boolean useGradient, float speed) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.color = color;
        this.gradientStartColor = gradientStartColor;
        this.gradientEndColor = gradientEndColor;
        this.useGradient = useGradient;
        this.speed = speed;
        this.offset = 0;
    }

    public void tick(Font font) {
        int textWidth = font.width(text);

        offset -= speed;

        if (offset < -textWidth) {
            offset = width;
        }
    }

    public void render(GuiGraphics graphics, Font font) {
        graphics.enableScissor(x, y, x + width, y + font.lineHeight);

        if (useGradient) {
            drawGradientText(graphics, font, x + (int) offset, y);
        } else {
            graphics.drawString(font, Component.literal(text), x + (int) offset, y, color, false);
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
}
