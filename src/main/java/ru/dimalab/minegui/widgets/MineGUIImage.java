package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class MineGUIImage {
    private final ResourceLocation texture;
    private final int x, y;
    private final int width, height;

    public MineGUIImage(ResourceLocation texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(GuiGraphics graphics) {
        graphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }
}
