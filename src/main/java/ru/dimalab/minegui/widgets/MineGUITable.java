package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.ArrayList;
import java.util.List;

public class MineGUITable {
    private final int x, y;
    private final int columnWidth;
    private final int rowHeight;
    private final int columns;
    private final List<String[]> rows = new ArrayList<>();
    private String[] headers = null;

    private final int headerColor = FastColor.ARGB32.color(255, 40, 40, 40);
    private final int rowColor1 = FastColor.ARGB32.color(255, 60, 60, 60);
    private final int rowColor2 = FastColor.ARGB32.color(255, 80, 80, 80);
    private final int textColor = 0xFFFFFF;
    private final int borderColor = FastColor.ARGB32.color(255, 0, 0, 0);

    public MineGUITable(int x, int y, int columns, int columnWidth, int rowHeight) {
        this.x = x;
        this.y = y;
        this.columns = columns;
        this.columnWidth = columnWidth;
        this.rowHeight = rowHeight;
    }

    public void setHeaders(String... headers) {
        if (headers.length != columns)
            throw new IllegalArgumentException("Количество заголовков не совпадает с количеством колонок");
        this.headers = headers;
    }

    public void addRow(String... rowData) {
        if (rowData.length != columns)
            throw new IllegalArgumentException("Количество данных строки не совпадает с количеством колонок");
        rows.add(rowData);
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int currentY = y;


        if (headers != null) {
            graphics.fill(x, currentY, x + columns * columnWidth, currentY + rowHeight, headerColor);
            for (int col = 0; col < columns; col++) {
                drawCellText(graphics, font, headers[col], col, currentY);
            }
            currentY += rowHeight;
        }


        for (int rowIdx = 0; rowIdx < rows.size(); rowIdx++) {
            String[] row = rows.get(rowIdx);
            int bgColor = (rowIdx % 2 == 0) ? rowColor1 : rowColor2;
            graphics.fill(x, currentY, x + columns * columnWidth, currentY + rowHeight, bgColor);


            if (mouseOver(mouseX, mouseY, x, currentY, columns * columnWidth, rowHeight)) {
                graphics.fill(x, currentY, x + columns * columnWidth, currentY + rowHeight, FastColor.ARGB32.color(100, 255, 255, 255));
            }

            for (int col = 0; col < columns; col++) {
                drawCellText(graphics, font, row[col], col, currentY);
            }

            currentY += rowHeight;
        }


        graphics.renderOutline(x, y, columns * columnWidth, currentY - y, borderColor);
    }

    private void drawCellText(GuiGraphics graphics, Font font, String text, int col, int rowY) {
        int cellX = x + col * columnWidth;
        int padding = 4;
        graphics.drawString(font, Component.literal(text), cellX + padding, rowY + (rowHeight - font.lineHeight) / 2, textColor, false);
    }

    private boolean mouseOver(int mouseX, int mouseY, int rectX, int rectY, int rectW, int rectH) {
        return mouseX >= rectX && mouseX < rectX + rectW && mouseY >= rectY && mouseY < rectY + rectH;
    }
}
