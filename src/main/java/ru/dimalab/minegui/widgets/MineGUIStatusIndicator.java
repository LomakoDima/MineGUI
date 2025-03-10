package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class MineGUIStatusIndicator {
    private final int x, y;
    private final int size;

    private String label;
    private StatusType status;

    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int textColor = 0xFFFFFF;

    public enum StatusType {
        ACTIVE(FastColor.ARGB32.color(255, 0, 200, 0)),
        INACTIVE(FastColor.ARGB32.color(255, 120, 120, 120)),
        WARNING(FastColor.ARGB32.color(255, 255, 165, 0)),
        ERROR(FastColor.ARGB32.color(255, 200, 0, 0));

        private final int color;

        StatusType(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    public MineGUIStatusIndicator(int x, int y, int size, String label, StatusType initialStatus) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
        this.status = initialStatus;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void render(GuiGraphics graphics, Font font) {

        graphics.fill(x, y, x + size, y + size, status.getColor());

        graphics.renderOutline(x, y, size, size, borderColor);

        if (label != null && !label.isEmpty()) {
            graphics.drawString(
                    font,
                    Component.literal(label),
                    x + size + 6,
                    y + (size - font.lineHeight) / 2,
                    textColor,
                    false
            );
        }
    }
}
