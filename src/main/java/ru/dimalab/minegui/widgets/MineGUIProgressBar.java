package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class MineGUIProgressBar {
    private final int x, y, width, height;

    private int max = 100;
    private int value = 0;

    private int backgroundColor = FastColor.ARGB32.color(255, 50, 50, 50);
    private int progressColor = FastColor.ARGB32.color(255, 0, 200, 0);
    private int borderColor = FastColor.ARGB32.color(255, 100, 100, 100);
    private int textColor = 0xFFFFFF;

    private boolean showPercentText = true;

    public MineGUIProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setMax(int max) {
        this.max = Math.max(1, max);
    }

    public void setValue(int value) {
        this.value = Math.max(0, Math.min(value, max));
    }

    public int getValue() {
        return value;
    }

    public void setShowPercentText(boolean show) {
        this.showPercentText = show;
    }

    public void setColors(int backgroundColor, int progressColor, int borderColor) {
        this.backgroundColor = backgroundColor;
        this.progressColor = progressColor;
        this.borderColor = borderColor;
    }


    public void render(GuiGraphics graphics, Font font) {

        graphics.fill(x, y, x + width, y + height, backgroundColor);


        graphics.renderOutline(x, y, width, height, borderColor);

        int progressWidth = (int) ((value / (float) max) * width);

        if (progressWidth > 0) {
            graphics.fill(x, y, x + progressWidth, y + height, progressColor);
        }

        if (showPercentText) {
            String percentText = String.format("%d%%", (int) ((value / (float) max) * 100));
            int textX = x + (width - font.width(percentText)) / 2;
            int textY = y + (height - font.lineHeight) / 2;
            graphics.drawString(font, Component.literal(percentText), textX, textY, textColor, false);
        }
    }
}
