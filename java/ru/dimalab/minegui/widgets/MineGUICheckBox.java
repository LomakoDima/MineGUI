package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.function.Consumer;

public class MineGUICheckBox implements MineGUIWidget {
    private final int x, y, size;
    private boolean checked;
    private final Component label;
    private Consumer<Boolean> onToggle;

    private int boxColor;
    private int borderColor;
    private int checkColor;
    private int textColor;

    private ResourceLocation texture;
    private ResourceLocation checkTexture;


    private MineGUICheckBox(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.size = builder.size;
        this.label = builder.label;
        this.checked = builder.defaultValue;

        this.boxColor = builder.boxColor.getColor();
        this.borderColor = builder.borderColor.getColor();
        this.checkColor = builder.checkColor.getColor();
        this.textColor = builder.textColor.getColor();

        this.onToggle = builder.onToggle;
        this.texture = builder.texture;
        this.checkTexture = builder.checkTexture;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (texture != null) {
            graphics.blit(texture, x, y, 0, 0, size, size, size, size);
        } else {
            graphics.fill(x, y, x + size, y + size, boxColor);
            graphics.renderOutline(x, y, size, size, borderColor);
        }

        if (checked) {
            if (checkTexture != null) {
                graphics.blit(checkTexture, x, y, 0, 0, size, size, size, size);
            } else {
                int padding = 4;
                graphics.fill(x + padding, y + padding, x + size - padding, y + size - padding, checkColor);
            }
        }

        int textX = x + size + 8;
        int textY = y + (size - font.lineHeight) / 2;
        graphics.drawString(font, label, textX, textY, textColor, false);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            toggle();
            return true;
        }
        return false;
    }

    private void toggle() {
        checked = !checked;
        if (onToggle != null) {
            onToggle.accept(checked);
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + size && mouseY >= y && mouseY < y + size;
    }

    public boolean isChecked() {
        return checked;
    }


    public static class Builder {
        private int x, y, size;
        private Component label;
        private boolean defaultValue;

        private MineGUIColorPalette boxColor = MineGUIColorPalette.DARK_GRAY;
        private MineGUIColorPalette borderColor = MineGUIColorPalette.BLACK;
        private MineGUIColorPalette checkColor = MineGUIColorPalette.GREEN;
        private MineGUIColorPalette textColor = MineGUIColorPalette.WHITE;

        private Consumer<Boolean> onToggle;

        private ResourceLocation texture;
        private ResourceLocation checkTexture;

        public Builder(int x, int y, int size, Component label) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.label = label;
        }

        public Builder defaultValue(boolean checked) {
            this.defaultValue = checked;
            return this;
        }

        public Builder boxColor(MineGUIColorPalette color) {
            this.boxColor = color;
            return this;
        }

        public Builder borderColor(MineGUIColorPalette color) {
            this.borderColor = color;
            return this;
        }

        public Builder checkColor(MineGUIColorPalette color) {
            this.checkColor = color;
            return this;
        }

        public Builder textColor(MineGUIColorPalette color) {
            this.textColor = color;
            return this;
        }

        public Builder onToggle(Consumer<Boolean> callback) {
            this.onToggle = callback;
            return this;
        }

        public Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public Builder checkTexture(ResourceLocation texture) {
            this.checkTexture = texture;
            return this;
        }

        public MineGUICheckBox build() {
            return new MineGUICheckBox(this);
        }
    }
}
