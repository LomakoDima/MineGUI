/*

package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUITab implements MineGUIWidget {
    private final String label;
    private final int index;
    private boolean active = false;
    private int x, y, width, height;
    private MineGUIColorPalette backgroundColor;
    private MineGUIColorPalette activeColor;
    private MineGUIColorPalette textColor;

    private MineGUITab(Builder builder) {
        this.label = builder.label;
        this.index = builder.index;
        this.backgroundColor = builder.backgroundColor;
        this.activeColor = builder.activeColor;
        this.textColor = builder.textColor;
    }

    public void render(GuiGraphics graphics, Font font, int x, int y, int width, int height, boolean active) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = active;

        int color = active ? activeColor.getColor() : backgroundColor.getColor();
        graphics.fill(x, y, x + width, y + height, color);

        int textX = x + (width - font.width(label)) / 2;
        int textY = y + (height - font.lineHeight) / 2;
        graphics.drawString(font, Component.literal(label), textX, textY, textColor.getColor(), false);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }

    public static class Builder {
        private final String label;
        private final int index;
        private MineGUIColorPalette backgroundColor = MineGUIColorPalette.GRAY;
        private MineGUIColorPalette activeColor = MineGUIColorPalette.BLUE;
        private MineGUIColorPalette textColor = MineGUIColorPalette.WHITE;

        public Builder(String label, int index) {
            this.label = label;
            this.index = index;
        }

        public Builder backgroundColor(MineGUIColorPalette color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder activeColor(MineGUIColorPalette color) {
            this.activeColor = color;
            return this;
        }

        public Builder textColor(MineGUIColorPalette color) {
            this.textColor = color;
            return this;
        }

        public MineGUITab build() {
            return new MineGUITab(this);
        }
    }
}

 */