package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.utils.MouseHelper;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.function.Consumer;

public class MineGUISlider implements MineGUIWidget {
    private final int x, y;
    private final int width, height;
    private final int minValue, maxValue;
    private final int step;
    private int currentValue;

    private boolean dragging = false;
    private final Component label;
    private Consumer<Integer> onValueChanged;

    private int backgroundColor;
    private int fillColor;
    private int knobColor;
    private int borderColor;
    private int textColor;

    private final ResourceLocation backgroundTexture;
    private final ResourceLocation fillTexture;
    private final ResourceLocation knobTexture;
    private final int knobWidth;
    private final int knobHeight;

    private MineGUISlider(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.minValue = builder.minValue;
        this.maxValue = builder.maxValue;
        this.step = builder.step;
        this.currentValue = clampValue(builder.defaultValue);
        this.label = builder.label;
        this.onValueChanged = builder.onValueChanged;
        this.backgroundColor = builder.backgroundColor.getColor();
        this.fillColor = builder.fillColor.getColor();
        this.knobColor = builder.knobColor.getColor();
        this.borderColor = builder.borderColor.getColor();
        this.textColor = builder.textColor.getColor();
        this.backgroundTexture = builder.backgroundTexture;
        this.fillTexture = builder.fillTexture;
        this.knobTexture = builder.knobTexture;
        this.knobWidth = builder.knobWidth != -1 ? builder.knobWidth : height;
        this.knobHeight = builder.knobHeight != -1 ? builder.knobHeight : height + 4;
    }

    public static class Builder {
        private int x, y, width, height;
        private int minValue, maxValue, step, defaultValue;
        private Component label;
        private Consumer<Integer> onValueChanged;
        private MineGUIColorPalette backgroundColor = MineGUIColorPalette.DIM_GRAY;
        private MineGUIColorPalette fillColor = MineGUIColorPalette.LIME_GREEN;
        private MineGUIColorPalette knobColor = MineGUIColorPalette.WHITE;
        private MineGUIColorPalette borderColor = MineGUIColorPalette.BLACK;
        private MineGUIColorPalette textColor = MineGUIColorPalette.WHITE;

        private ResourceLocation backgroundTexture;
        private ResourceLocation fillTexture;
        private ResourceLocation knobTexture;
        private int knobWidth = -1;
        private int knobHeight = -1;

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

        public Builder setRange(int minValue, int maxValue, int step) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.step = step;
            return this;
        }

        public Builder setDefaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder setLabel(Component label) {
            this.label = label;
            return this;
        }

        public Builder setOnValueChanged(Consumer<Integer> onValueChanged) {
            this.onValueChanged = onValueChanged;
            return this;
        }

        public Builder setColors(MineGUIColorPalette backgroundColor, MineGUIColorPalette fillColor, MineGUIColorPalette knobColor, MineGUIColorPalette borderColor, MineGUIColorPalette textColor) {
            this.backgroundColor = backgroundColor;
            this.fillColor = fillColor;
            this.knobColor = knobColor;
            this.borderColor = borderColor;
            this.textColor = textColor;
            return this;
        }

        public Builder setBackgroundTexture(ResourceLocation texture) {
            this.backgroundTexture = texture;
            return this;
        }

        public Builder setFillTexture(ResourceLocation texture) {
            this.fillTexture = texture;
            return this;
        }

        public Builder setKnobTexture(ResourceLocation texture) {
            this.knobTexture = texture;
            return this;
        }

        public Builder setKnobSize(int width, int height) {
            this.knobWidth = width;
            this.knobHeight = height;
            return this;
        }

        public MineGUISlider build() {
            return new MineGUISlider(this);
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (backgroundTexture != null) {
            graphics.blit(backgroundTexture, x, y, 0, 0, width, height, width, height);
        } else {
            graphics.fill(x, y, x + width, y + height, backgroundColor);
        }

        int filledWidth = (int) ((currentValue - minValue) / (float) (maxValue - minValue) * width);
        if (fillTexture != null) {
            graphics.blit(fillTexture, x, y, 0, 0, filledWidth, height, filledWidth, height);
        } else {
            graphics.fill(x, y, x + filledWidth, y + height, fillColor);
        }

        int knobX = x + filledWidth - (knobWidth / 2);
        int knobY = y - (knobHeight - height) / 2;
        if (knobTexture != null) {
            graphics.blit(knobTexture, knobX, knobY, 0, 0, knobWidth, knobHeight, knobWidth, knobHeight);
        } else {
            graphics.fill(knobX, knobY, knobX + knobWidth, knobY + knobHeight, knobColor);
        }

        String display = label.getString() + ": " + currentValue;
        int textWidth = font.width(display);
        int textX = x + (width - textWidth) / 2;
        int textY = y - font.lineHeight - 4;
        graphics.drawString(font, Component.literal(display), textX, textY, textColor, false);
    }


    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (MouseHelper.LEFT.isLeft() && isMouseOver(mouseX, mouseY)) {
            dragging = true;
            updateValueFromMouse(mouseX);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        if (MouseHelper.LEFT.isLeft()) {
            dragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(int mouseX, int mouseY, int button, double dragX, double dragY) {
        if (dragging && MouseHelper.LEFT.isLeft()) {
            updateValueFromMouse(mouseX);
            return true;
        }
        return false;
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
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
