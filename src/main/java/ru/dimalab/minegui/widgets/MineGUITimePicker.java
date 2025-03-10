package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.function.BiConsumer;

public class MineGUITimePicker {
    private final int x, y;
    private final int width, height;

    private int hours = 0;
    private int minutes = 0;

    private int backgroundColor = FastColor.ARGB32.color(255, 60, 60, 60);
    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int activeBorderColor = FastColor.ARGB32.color(255, 0, 120, 255);
    private int textColor = 0xFFFFFF;
    private int arrowColor = 0xAAAAAA;
    private int arrowHoverColor = 0xFFFFFF;

    private BiConsumer<Integer, Integer> onTimeChanged;

    // Для ховеров
    private boolean hoverHourUp, hoverHourDown;
    private boolean hoverMinUp, hoverMinDown;

    public MineGUITimePicker(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setOnTimeChanged(BiConsumer<Integer, Integer> onTimeChanged) {
        this.onTimeChanged = onTimeChanged;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setTime(int hours, int minutes) {
        this.hours = clamp(hours, 0, 23);
        this.minutes = clamp(minutes, 0, 59);

        if (onTimeChanged != null) {
            onTimeChanged.accept(this.hours, this.minutes);
        }
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(x, y, x + width, y + height, backgroundColor);
        graphics.renderOutline(x, y, width, height, borderColor);

        int sectionWidth = width / 2;

        renderSection(graphics, font, x, y, sectionWidth, height, hours, 23,
                hoverHourUp, hoverHourDown, "Hours");

        renderSection(graphics, font, x + sectionWidth, y, sectionWidth, height, minutes, 59,
                hoverMinUp, hoverMinDown, "Minutes");
    }

    private void renderSection(GuiGraphics graphics, Font font, int x, int y, int width, int height,
                               int value, int maxValue, boolean hoverUp, boolean hoverDown, String label) {

        int halfHeight = height / 2;


        int arrowUpColor = hoverUp ? arrowHoverColor : arrowColor;
        graphics.drawString(font, Component.literal("▲"), x + width / 2 - 3, y + 3, arrowUpColor, false);


        String valueText = String.format("%02d", value);
        int textY = y + halfHeight - font.lineHeight / 2;
        int textX = x + (width - font.width(valueText)) / 2;
        graphics.drawString(font, Component.literal(valueText), textX, textY, textColor, false);

        int arrowDownColor = hoverDown ? arrowHoverColor : arrowColor;
        graphics.drawString(font, Component.literal("▼"), x + width / 2 - 3, y + height - font.lineHeight - 3, arrowDownColor, false);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!isInside(mouseX, mouseY)) return false;

        int sectionWidth = width / 2;


        if (mouseX >= x && mouseX < x + sectionWidth) {

            if (mouseY >= y && mouseY < y + height / 2) {
                incrementHours();
            } else {
                decrementHours();
            }
            return true;
        }


        if (mouseX >= x + sectionWidth && mouseX <= x + width) {
            if (mouseY >= y && mouseY < y + height / 2) {
                incrementMinutes();
            } else {
                decrementMinutes();
            }
            return true;
        }

        return false;
    }

    public void mouseMoved(int mouseX, int mouseY) {
        int sectionWidth = width / 2;

        hoverHourUp = isInside(mouseX, mouseY) && mouseX >= x && mouseX < x + sectionWidth && mouseY >= y && mouseY < y + height / 2;
        hoverHourDown = isInside(mouseX, mouseY) && mouseX >= x && mouseX < x + sectionWidth && mouseY >= y + height / 2 && mouseY <= y + height;

        hoverMinUp = isInside(mouseX, mouseY) && mouseX >= x + sectionWidth && mouseX < x + width && mouseY >= y && mouseY < y + height / 2;
        hoverMinDown = isInside(mouseX, mouseY) && mouseX >= x + sectionWidth && mouseX < x + width && mouseY >= y + height / 2 && mouseY <= y + height;
    }


    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private void incrementHours() {
        hours = (hours + 1) % 24;
        if (onTimeChanged != null) onTimeChanged.accept(hours, minutes);
    }

    private void decrementHours() {
        hours = (hours - 1 + 24) % 24;
        if (onTimeChanged != null) onTimeChanged.accept(hours, minutes);
    }

    private void incrementMinutes() {
        minutes = (minutes + 1) % 60;
        if (onTimeChanged != null) onTimeChanged.accept(hours, minutes);
    }

    private void decrementMinutes() {
        minutes = (minutes - 1 + 60) % 60;
        if (onTimeChanged != null) onTimeChanged.accept(hours, minutes);
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(val, max));
    }
}
