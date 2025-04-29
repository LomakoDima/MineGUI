package ru.dimalab.minegui.widgets.custom.parts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface Renderable {
    default void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {}
    default void render1(GuiGraphics graphics, Font font, int mouseX, int mouseY, int width, int height, boolean active) {}
}
