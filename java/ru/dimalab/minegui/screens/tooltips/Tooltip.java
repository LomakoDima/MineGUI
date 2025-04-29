package ru.dimalab.minegui.screens.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface Tooltip {
    void render(GuiGraphics graphics, Font font, int x, int y);
    void update();
    int getWidth(Font font);
    int getHeight(Font font);
}
