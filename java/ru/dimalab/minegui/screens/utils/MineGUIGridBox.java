package ru.dimalab.minegui.screens.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUIGridBox implements MineGUIWidget {
    private final List<MineGUIWidget> children = new ArrayList<>();
    private int x, y;
    private int columns;
    private int horizontalSpacing, verticalSpacing;
    private int width, height;
    private int[] columnWidths;
    private int[] rowHeights;

    public MineGUIGridBox(int x, int y, int columns, int horizontalSpacing, int verticalSpacing) {
        this.x = x;
        this.y = y;
        this.columns = columns;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
    }

    public void addChild(MineGUIWidget widget) {
        children.add(widget);
        updateLayout();
    }

    public void setColumns(int columns) {
        this.columns = columns;
        updateLayout();
    }

    private void updateLayout() {
        int rows = (int) Math.ceil((double) children.size() / columns);
        columnWidths = new int[columns];
        rowHeights = new int[rows];

        for (int i = 0; i < children.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            columnWidths[col] = Math.max(columnWidths[col], children.get(i).getWidth());
            rowHeights[row] = Math.max(rowHeights[row], children.get(i).getHeight());
        }

        int[] columnOffsets = new int[columns];
        for (int col = 0; col < columns; col++) {
            columnOffsets[col] = (col == 0) ? x : columnOffsets[col - 1] + columnWidths[col - 1] + horizontalSpacing;
        }

        int[] rowOffsets = new int[rows];
        for (int row = 0; row < rows; row++) {
            rowOffsets[row] = (row == 0) ? y : rowOffsets[row - 1] + rowHeights[row - 1] + verticalSpacing;
        }

        for (int i = 0; i < children.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int posX = columnOffsets[col];
            int posY = rowOffsets[row];
            children.get(i).setPosition(posX, posY);
        }

        width = 0;
        for (int w : columnWidths) width += w;
        width += horizontalSpacing * (columns - 1);

        height = 0;
        for (int h : rowHeights) height += h;
        height += verticalSpacing * (rows - 1);
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        for (MineGUIWidget child : children) {
            child.render(graphics, font, mouseX, mouseY);
        }
    }
}
