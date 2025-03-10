package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.function.Consumer;

public class MineGUICheckBox {
    private final int x, y;
    private final int size;
    private boolean checked;
    private final Component label;
    private Consumer<Boolean> onToggle;

    private int boxColor = FastColor.ARGB32.color(255, 60, 60, 60);
    private int borderColor = FastColor.ARGB32.color(255, 0, 0, 0);
    private int checkColor = FastColor.ARGB32.color(255, 0, 200, 0);
    private int textColor = 0xFFFFFF;

    public MineGUICheckBox(int x, int y, int size, Component label, boolean defaultValue) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
        this.checked = defaultValue;
    }

    public void setOnToggle(Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
    }


    public void setColors(int boxColor, int borderColor, int checkColor, int textColor) {
        this.boxColor = boxColor;
        this.borderColor = borderColor;
        this.checkColor = checkColor;
        this.textColor = textColor;
    }

    public void onClick(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            toggle();
        }
    }

    private void toggle() {
        checked = !checked;
        if (onToggle != null) {
            onToggle.accept(checked);
        }
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(x, y, x + size, y + size, boxColor);
        graphics.renderOutline(x, y, size, size, borderColor);

        if (checked) {
            int padding = 4;
            graphics.fill(x + padding, y + padding, x + size - padding, y + size - padding, checkColor);
        }

        int textX = x + size + 8;
        int textY = y + (size - font.lineHeight) / 2;
        graphics.drawString(font, label, textX, textY, textColor, false);
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + size && mouseY >= y && mouseY < y + size;
    }

    public boolean isChecked() {
        return checked;
    }
}
