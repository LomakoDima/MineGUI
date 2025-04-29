package ru.dimalab.minegui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUIImageSlider implements MineGUIWidget {
    private int x, y, width, height;
    private List<ResourceLocation> images;
    private int currentIndex = 0;
    private int previousIndex = -1;
    private int tickCounter = 0;
    private float alpha = 0.0f;
    private static final int SWITCH_INTERVAL = 40;
    private static final int FADE_DURATION = 10;

    private MineGUIImageSlider(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.images = builder.images;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (!images.isEmpty()) {
            Minecraft minecraft = Minecraft.getInstance();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            if (previousIndex != -1) {
                ResourceLocation previousImage = images.get(previousIndex);
                minecraft.getTextureManager().bindForSetup(previousImage);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f - alpha);
                graphics.blit(previousImage, x, y, 0, 0, width, height, width, height);
            }

            ResourceLocation currentImage = images.get(currentIndex);
            minecraft.getTextureManager().bindForSetup(currentImage);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
            graphics.blit(currentImage, x, y, 0, 0, width, height, width, height);

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter >= SWITCH_INTERVAL) {
            tickCounter = 0;
            previousIndex = currentIndex;
            currentIndex = (currentIndex + 1) % images.size();
            alpha = 0.0f;
        }

        if (previousIndex != -1 && alpha < 1.0f) {
            alpha += 1.0f / FADE_DURATION;
            if (alpha > 1.0f) {
                alpha = 1.0f;
                previousIndex = -1;
            }
        }
    }

    public static class Builder {
        private int x, y, width, height;
        private List<ResourceLocation> images = new ArrayList<>();

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

        public Builder addImage(ResourceLocation image) {
            this.images.add(image);
            return this;
        }

        public MineGUIImageSlider build() {
            return new MineGUIImageSlider(this);
        }
    }
}
