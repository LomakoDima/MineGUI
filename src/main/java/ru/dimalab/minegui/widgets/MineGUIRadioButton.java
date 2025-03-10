package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class MineGUIRadioButton {
    private final int x, y, size;
    private final String label;
    private boolean selected = false;

    private final MineGUIRadioGroup group;

    private int textColor = 0xFFFFFF;
    private int backgroundColor = FastColor.ARGB32.color(255, 60, 60, 60);
    private int borderColor = FastColor.ARGB32.color(255, 100, 100, 100);
    private int selectedColor = FastColor.ARGB32.color(255, 0, 120, 255);
    private int hoverColor = FastColor.ARGB32.color(255, 80, 80, 80);

    public MineGUIRadioButton(int x, int y, int size, String label, MineGUIRadioGroup group) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
        this.group = group;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getLabel() {
        return label;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        boolean hovered = isInside(mouseX, mouseY);

        graphics.fill(x, y, x + size, y + size, hovered ? hoverColor : backgroundColor);
        graphics.renderOutline(x, y, size, size, borderColor);


        if (selected) {
            int innerMargin = size / 4;
            graphics.fill(
                    x + innerMargin,
                    y + innerMargin,
                    x + size - innerMargin,
                    y + size - innerMargin,
                    selectedColor
            );
        }


        graphics.drawString(
                font,
                Component.literal(label),
                x + size + 5,
                y + (size - font.lineHeight) / 2,
                textColor,
                false
        );
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            group.selectButton(this);
            return true;
        }
        return false;
    }

    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
}
