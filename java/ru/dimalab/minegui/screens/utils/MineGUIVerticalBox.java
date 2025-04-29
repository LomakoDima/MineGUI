package ru.dimalab.minegui.screens.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUIVerticalBox implements MineGUIWidget {
    private final List<MineGUIWidget> children = new ArrayList<>();
    private int x, y;
    private int spacing;
    private int width, height;

    public MineGUIVerticalBox(int x, int y, int spacing) {
        this.x = x;
        this.y = y;
        this.spacing = spacing;
    }

    public void addChild(MineGUIWidget widget) {
        children.add(widget);
        updateLayout();
    }

    private void updateLayout() {
        int offsetY = y;
        width = 0;
        height = 0;

        for (MineGUIWidget child : children) {
            child.setPosition(x, offsetY);
            offsetY += child.getHeight() + spacing;
            height += child.getHeight() + spacing;
            width = Math.max(width, child.getWidth());
        }
        if (!children.isEmpty()) {
            height -= spacing;
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        for (MineGUIWidget child : children) {
            child.render(graphics, font, mouseX, mouseY);
        }
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        updateLayout();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
