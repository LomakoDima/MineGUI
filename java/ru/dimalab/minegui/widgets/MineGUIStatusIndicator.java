package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIStatusIndicator implements MineGUIWidget {
    private final int x, y;
    private final int size;

    private String label;
    private StatusType status;

    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private MineGUIColorPalette textColor = MineGUIColorPalette.WHITE;

    public enum StatusType {
        ACTIVE(MineGUIColorPalette.LIME_GREEN.getColor()),
        INACTIVE(MineGUIColorPalette.GRAY.getColor()),
        WARNING(MineGUIColorPalette.ORANGE.getColor()),
        ERROR(MineGUIColorPalette.RED.getColor());

        private final int color;

        StatusType(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    public MineGUIStatusIndicator(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.size = builder.size;
        this.label = builder.label;
        this.status = builder.status;
    }

    public StatusType getStatus() {
        return status;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {

        graphics.fill(x, y, x + size, y + size, status.getColor());

        graphics.renderOutline(x, y, size, size, borderColor);

        if (label != null && !label.isEmpty()) {
            graphics.drawString(
                    font,
                    Component.literal(label),
                    x + size + 6,
                    y + (size - font.lineHeight) / 2,
                    textColor.getColor(),
                    false
            );
        }
    }

    public static class Builder {
        private int x;
        private int y;
        private int size = 16;
        private String label = "";
        private StatusType status = StatusType.ACTIVE;
        private MineGUIColorPalette textColor;

       public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setStatus(StatusType status) {
            this.status = status;
            return this;
        }

        public Builder setTextColor(MineGUIColorPalette color) {
            this.textColor = color;
            return this;
        }

        public MineGUIStatusIndicator build() {
            return new MineGUIStatusIndicator(this);
        }
    }
}
