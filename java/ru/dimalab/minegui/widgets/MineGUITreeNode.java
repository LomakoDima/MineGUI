/*
package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class MineGUITreeNode {
    private String label;
    private int x, y;
    private boolean expanded;
    private List<MineGUITreeNode> children;

    public MineGUITreeNode(String label) {
        this.label = label;
        this.children = new ArrayList<>();
        this.expanded = false;
    }

    public void addChild(MineGUITreeNode child) {
        children.add(child);
    }

    public int render(GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY) {
        this.x = x; // Сохраняем текущие координаты
        this.y = y;
        graphics.drawString(font, (expanded ? "[-] " : "[+] ") + label, x, y, 0xFFFFFFFF);
        int offsetY = 10;
        if (expanded) {
            for (MineGUITreeNode child : children) {
                offsetY += child.render(graphics, font, x + 10, y + offsetY, mouseX, mouseY);
            }
        }
        return offsetY;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 10) {
            expanded = !expanded;
            return true;
        }
        for (MineGUITreeNode child : children) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }
}

 */