package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIScrollbar implements MineGUIWidget {
    public enum Orientation {
        VERTICAL, HORIZONTAL
    }

    private int x;
    private int y;
    private int width;
    private int height;
    private final Orientation orientation;
    private int scrollValue;
    private int minValue = 0;
    private int maxValue = 100;
    private int thumbSize = 20;
    private boolean isDragging = false;
    private int dragOffset;

    public MineGUIScrollbar(int x, int y, int size, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        if (orientation == Orientation.VERTICAL) {
            this.width = 8;
            this.height = size;
        } else {
            this.width = size;
            this.height = 8;
        }
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF808080);

        int thumbPos = calculateThumbPosition();
        if (orientation == Orientation.VERTICAL) {
            graphics.fill(getX(), getY() + thumbPos, getX() + getWidth(), getY() + thumbPos + thumbSize, 0xFFA0A0A0);
        } else {
            graphics.fill(getX() + thumbPos, getY(), getX() + thumbPos + thumbSize, getY() + getHeight(), 0xFFA0A0A0);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOverThumb(mouseX, mouseY)) {
            isDragging = true;
            if (orientation == Orientation.VERTICAL) {
                dragOffset = mouseY - (getY() + calculateThumbPosition());
            } else {
                dragOffset = mouseX - (getX() + calculateThumbPosition());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        int step = (int) (-deltaY * 5);
        setScrollValue(scrollValue + step);
        return true;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        isDragging = false;
        return false;
    }

    @Override
    public boolean mouseDragged(int mouseX, int mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            int newPosition;
            if (orientation == Orientation.VERTICAL) {
                newPosition = mouseY - getY() - dragOffset;
                newPosition = Math.max(0, Math.min(newPosition, getHeight() - thumbSize));
                scrollValue = (int) ((newPosition / (float) (getHeight() - thumbSize)) * (maxValue - minValue) + minValue);
            } else {
                newPosition = mouseX - getX() - dragOffset;
                newPosition = Math.max(0, Math.min(newPosition, getWidth() - thumbSize));
                scrollValue = (int) ((newPosition / (float) (getWidth() - thumbSize)) * (maxValue - minValue) + minValue);
            }
            return true;
        }
        return false;
    }

    private int calculateThumbPosition() {
        float progress = (scrollValue - minValue) / (float) (maxValue - minValue);
        if (orientation == Orientation.VERTICAL) {
            return (int) (progress * (getHeight() - thumbSize));
        } else {
            return (int) (progress * (getWidth() - thumbSize));
        }
    }

    private boolean isMouseOverThumb(int mouseX, int mouseY) {
        int thumbPos = calculateThumbPosition();
        if (orientation == Orientation.VERTICAL) {
            return mouseX >= getX() && mouseX <= getX() + getWidth() &&
                    mouseY >= getY() + thumbPos && mouseY <= getY() + thumbPos + thumbSize;
        } else {
            return mouseX >= getX() + thumbPos && mouseX <= getX() + thumbPos + thumbSize &&
                    mouseY >= getY() && mouseY <= getY() + getHeight();
        }
    }

    public void setRange(int min, int max) {
        this.minValue = min;
        this.maxValue = max;
    }

    public int getScrollValue() {
        return scrollValue;
    }

    public void setScrollValue(int value) {
        this.scrollValue = Math.max(minValue, Math.min(value, maxValue));
    }
}
