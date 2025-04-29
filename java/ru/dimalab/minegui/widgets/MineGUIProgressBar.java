package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIProgressBar implements MineGUIWidget {
    private final int x, y, width, height;

    private int max = 100;
    private int value = 0;

    private int backgroundColor = FastColor.ARGB32.color(255, 50, 50, 50);
    private int progressColor = FastColor.ARGB32.color(255, 0, 200, 0);
    private int borderColor = FastColor.ARGB32.color(255, 100, 100, 100);
    private int textColor = 0xFFFFFF;

    private boolean showPercentText = true;

    private ResourceLocation backgroundTexture;
    private ResourceLocation progressTexture;
    private int backgroundTexU, backgroundTexV, progressTexU, progressTexV;

    public MineGUIProgressBar(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.max = builder.max;
        this.value = builder.value;
        this.backgroundColor = builder.backgroundColor;
        this.progressColor = builder.progressColor;
        this.borderColor = builder.borderColor;
        this.textColor = builder.textColor;
        this.showPercentText = builder.showPercentText;
        this.backgroundTexture = builder.backgroundTexture;
        this.progressTexture = builder.progressTexture;
        this.backgroundTexU = builder.backgroundTexU;
        this.backgroundTexV = builder.backgroundTexV;
    }

    public void setMax(int max) {
        this.max = Math.max(1, max);
    }

    public void setValue(int value) {
        this.value = Math.max(0, Math.min(value, max));
    }

    public int getValue() {
        return value;
    }

    public void setShowPercentText(boolean show) {
        this.showPercentText = show;
    }

    public void setColors(int backgroundColor, int progressColor, int borderColor) {
        this.backgroundColor = backgroundColor;
        this.progressColor = progressColor;
        this.borderColor = borderColor;
    }

    public void setBackgroundTexture(ResourceLocation texture, int texU, int texV) {
        this.backgroundTexture = texture;
        this.backgroundTexU = texU;
        this.backgroundTexV = texV;
    }

    public void setProgressTexture(ResourceLocation texture, int texU, int texV) {
        this.progressTexture = texture;
        this.progressTexU = texU;
        this.progressTexV = texV;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {

        if (backgroundTexture != null) {
            graphics.blit(backgroundTexture, x, y, backgroundTexU, backgroundTexV, width, height);
        } else {
            graphics.fill(x, y, x + width, y + height, backgroundColor);
        }

        graphics.renderOutline(x, y, width, height, borderColor);

        int progressWidth = (int) ((value / (float) max) * width);

        if (progressWidth > 0) {
            if (progressTexture != null) {
                graphics.blit(progressTexture, x, y, progressTexU, progressTexV, progressWidth, height);
            } else {
                graphics.fill(x, y, x + progressWidth, y + height, progressColor);
            }
        }

        if (showPercentText) {
            String percentText = String.format("%d%%", (int) ((value / (float) max) * 100));
            int textX = x + (width - font.width(percentText)) / 2;
            int textY = y + (height - font.lineHeight) / 2;
            graphics.drawString(font, Component.literal(percentText), textX, textY, textColor, false);
        }
    }

    public static class Builder {
        private int x, y, width, height;
        private int max = 100;
        private int value = 0;
        private int backgroundColor = FastColor.ARGB32.color(255, 50, 50, 50);
        private int progressColor = FastColor.ARGB32.color(255, 0, 200, 0);
        private int borderColor = FastColor.ARGB32.color(255, 100, 100, 100);
        private int textColor = 0xFFFFFF;
        private boolean showPercentText = true;

        private ResourceLocation backgroundTexture;
        private ResourceLocation progressTexture;
        private int backgroundTexU, backgroundTexV, progressTexU, progressTexV;
        //private int backgroundTexWidth, backgroundTexHeight;

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

        public Builder setMax(int max) {
            this.max = Math.max(1, max);
            return this;
        }

        public Builder setValue(int value) {
            this.value = Math.max(0, Math.min(value, max));
            return this;
        }

        public Builder setBackgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder setProgressColor(int color) {
            this.progressColor = color;
            return this;
        }

        public Builder setBorderColor(int color) {
            this.borderColor = color;
            return this;
        }

        public Builder setTextColor(int color) {
            this.textColor = color;
            return this;
        }

        public Builder setShowPercentText(boolean show) {
            this.showPercentText = show;
            return this;
        }

        public Builder setBackgroundTexture(ResourceLocation texture, int texU, int texV) {
            this.backgroundTexture = texture;
            this.backgroundTexU = texU;
            this.backgroundTexV = texV;
            return this;
        }

        public Builder setProgressTexture(ResourceLocation texture, int texU, int texV) {
            this.progressTexture = texture;
            this.progressTexU = texU;
            this.progressTexV = texV;
            return this;
        }

        public MineGUIProgressBar build() {
            return new MineGUIProgressBar(this);
        }

    }
}
