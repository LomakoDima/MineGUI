package ru.dimalab.minegui.screens.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public record TextTooltip(Component text,
                          TooltipStyle style,
                          TooltipPosition position) implements Tooltip {
    @Override
    public void render(GuiGraphics graphics, Font font, int x, int y) {
        graphics.fill(x, y, x + getWidth(font), y + getHeight(font), style.backgroundColor());
        graphics.drawString(font, text, x + style.padding(), y + style.padding(), style.textColor());
    }

    @Override
    public int getWidth(Font font) {
        return font.width(text) + style.padding() * 2;
    }

    @Override
    public int getHeight(Font font) {
        return font.lineHeight + style.padding() * 2;
    }

    @Override
    public void update() {}
}
