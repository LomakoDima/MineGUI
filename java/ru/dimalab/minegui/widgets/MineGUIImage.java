package ru.dimalab.minegui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;

public class MineGUIImage implements MineGUIWidget {
    private final ResourceLocation texture;
    private final int x, y;
    private final int width, height;

    private final boolean drawBorder;
    private final int borderColor;
    private final int borderThickness;

    private MineGUIImage(Builder builder) {
        this.texture = builder.texture;
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;

        this.drawBorder = builder.drawBorder;
        this.borderColor = builder.borderColor;
        this.borderThickness = builder.borderThickness;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.blit(texture, x, y, 0, 0, width, height, width, height);

        if (drawBorder) {
            drawBorder(graphics);
        }
    }

    private void drawBorder(GuiGraphics graphics) {
        int left = x;
        int top = y;
        int right = x + width;
        int bottom = y + height;

        graphics.fill(left - borderThickness, top - borderThickness, right + borderThickness, top, borderColor);
        graphics.fill(left - borderThickness, bottom, right + borderThickness, bottom + borderThickness, borderColor);
        graphics.fill(left - borderThickness, top, left, bottom, borderColor);
        graphics.fill(right, top, right + borderThickness, bottom, borderColor);
    }

    public static class Builder {
        private ResourceLocation texture;
        private int x = 0, y = 0;
        private int width = 16, height = 16;

        private boolean drawBorder = false;
        private int borderColor = 0xFFFFFFFF;
        private int borderThickness = 1;

        public Builder() {
        }

        public Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder border(boolean drawBorder) {
            this.drawBorder = drawBorder;
            return this;
        }

        public Builder borderColor(int color) {
            this.borderColor = color;
            return this;
        }

        public Builder borderThickness(int thickness) {
            this.borderThickness = thickness;
            return this;
        }

        public MineGUIImage build() {
            if (texture == null) {
                throw new IllegalStateException("Texture must be set for MineGUIImage!");
            }
            return new MineGUIImage(this);
        }
    }
}
