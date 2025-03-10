package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class MineGUITab {
    private final String label;
    private final int index;

    private boolean active = false;
    private int x, y, width, height;


    private int backgroundColor = FastColor.ARGB32.color(255, 50, 50, 50);
    private int activeColor = FastColor.ARGB32.color(255, 0, 120, 255);
    private int textColor = 0xFFFFFF;

    public MineGUITab(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public void render(GuiGraphics graphics, Font font, int x, int y, int width, int height, boolean active) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = active;

        int color = active ? activeColor : backgroundColor;

        graphics.fill(x, y, x + width, y + height, color);

        int textX = x + (width - font.width(label)) / 2;
        int textY = y + (height - font.lineHeight) / 2;
        graphics.drawString(font, Component.literal(label), textX, textY, textColor, false);
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
}
