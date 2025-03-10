package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MineGUISidebar {
    private final int x, y;
    private final int width, height;

    private boolean expanded = true;

    private int backgroundColor = FastColor.ARGB32.color(255, 40, 40, 40);
    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int activeItemColor = FastColor.ARGB32.color(255, 0, 120, 255);
    private int textColor = 0xFFFFFF;
    private int hoverColor = FastColor.ARGB32.color(255, 60, 60, 60);

    public final List<String> items = new ArrayList<>();
    private int selectedIndex = -1;
    private Consumer<Integer> onItemSelected;

    private int itemHeight = 24;

    public MineGUISidebar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void clearItems() {
        items.clear();
        selectedIndex = -1;
    }

    public void setOnItemSelected(Consumer<Integer> onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void toggle() {
        expanded = !expanded;
    }

    public void setColors(int bgColor, int borderColor, int textColor, int activeItemColor, int hoverColor) {
        this.backgroundColor = bgColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.activeItemColor = activeItemColor;
        this.hoverColor = hoverColor;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (!expanded) {
            return;
        }

        graphics.fill(x, y, x + width, y + height, backgroundColor);
        graphics.renderOutline(x, y, width, height, borderColor);

        int currentY = y + 10;

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);

            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= currentY && mouseY <= currentY + itemHeight;
            boolean selected = i == selectedIndex;

            int itemBgColor = hovered ? hoverColor : (selected ? activeItemColor : backgroundColor);

            graphics.fill(x, currentY, x + width, currentY + itemHeight, itemBgColor);

            int textX = x + 10;
            int textY = currentY + (itemHeight - font.lineHeight) / 2;

            graphics.drawString(font, Component.literal(item), textX, textY, textColor, false);

            currentY += itemHeight + 4;
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (!expanded) return false;

        int currentY = y + 10;

        for (int i = 0; i < items.size(); i++) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= currentY && mouseY <= currentY + itemHeight) {
                selectedIndex = i;

                if (onItemSelected != null) {
                    onItemSelected.accept(i);
                }

                return true;
            }

            currentY += itemHeight + 4;
        }

        return false;
    }

}
