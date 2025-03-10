package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.function.Consumer;

public class MineGUISlider {
    private final int x, y;
    private final int width, height;
    private final int minValue, maxValue;
    private final int step;
    private int currentValue;

    private boolean dragging = false;
    private final Component label;
    private Consumer<Integer> onValueChanged;

    private int backgroundColor = FastColor.ARGB32.color(255, 60, 60, 60);
    private int fillColor = FastColor.ARGB32.color(255, 76, 175, 80);
    private int knobColor = FastColor.ARGB32.color(255, 255, 255, 255);
    private int borderColor = FastColor.ARGB32.color(255, 0, 0, 0);
    private int textColor = 0xFFFFFF;

    public MineGUISlider(int x, int y, int width, int height, int minValue, int maxValue, int step, int defaultValue, Component label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
        this.currentValue = clampValue(defaultValue);
        this.label = label;
    }

    public void setOnValueChanged(Consumer<Integer> onValueChanged) {
        this.onValueChanged = onValueChanged;
    }

    public void setColors(int backgroundColor, int fillColor, int knobColor, int borderColor, int textColor) {
        this.backgroundColor = backgroundColor;
        this.fillColor = fillColor;
        this.knobColor = knobColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            dragging = true;
            updateValueFromMouse(mouseX);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        dragging = false;
        return false;
    }

    public boolean mouseDragged(int mouseX, int mouseY) {
        if (dragging) {
            updateValueFromMouse(mouseX);
            return true;
        }
        return false;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(x, y, x + width, y + height, backgroundColor);

        int filledWidth = (int) ((currentValue - minValue) / (float) (maxValue - minValue) * width);
        graphics.fill(x, y, x + filledWidth, y + height, fillColor);

        int knobX = x + filledWidth - height / 2;
        graphics.fill(knobX, y - 2, knobX + height, y + height + 2, knobColor);


        String display = label.getString() + ": " + currentValue;
        int textWidth = font.width(display);
        int textX = x + (width - textWidth) / 2;
        int textY = y - font.lineHeight - 4;

        graphics.drawString(font, Component.literal(display), textX, textY, textColor, false);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y - 5 && mouseY <= y + height + 5;
    }


    private void updateValueFromMouse(int mouseX) {
        float percent = (mouseX - x) / (float) width;
        int rawValue = minValue + Math.round((maxValue - minValue) * percent);
        int steppedValue = clampValue(Math.round(rawValue / (float) step) * step);

        if (steppedValue != currentValue) {
            currentValue = steppedValue;
            if (onValueChanged != null) {
                onValueChanged.accept(currentValue);
            }
        }
    }

    private int clampValue(int value) {
        return Math.max(minValue, Math.min(maxValue, value));
    }

    public int getValue() {
        return currentValue;
    }

    public void setValue(int value) {
        this.currentValue = clampValue(value);
    }
}
