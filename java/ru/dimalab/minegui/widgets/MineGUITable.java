package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUITable implements MineGUIWidget {
    private final int x, y;
    private final int columnWidth;
    private final int rowHeight;
    private final int columns;

    private String[] headers = null;
    private final List<String[]> rows = new ArrayList<>();

    private final int headerColor;
    private final int rowColor1;
    private final int rowColor2;
    private final int textColor;
    private final int borderColor;

    private MineGUITable(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.columns = builder.columns;
        this.columnWidth = builder.columnWidth;
        this.rowHeight = builder.rowHeight;

        this.headerColor = builder.headerColor.getColor();
        this.rowColor1 = builder.rowColor1.getColor();
        this.rowColor2 = builder.rowColor2.getColor();
        this.textColor = builder.textColor.getColor();
        this.borderColor = builder.borderColor.getColor();
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
                graphics.fill(x, currentY, x + columns * columnWidth, currentY + rowHeight, MineGUIColorPalette.WHITE.getColor() & 0x64FFFFFF);
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

    public static class Builder {
        private int x = 0, y = 0;
        private int columns = 1;
        private int columnWidth = 100;
        private int rowHeight = 20;

        private MineGUIColorPalette headerColor = MineGUIColorPalette.DIM_GRAY;
        private MineGUIColorPalette rowColor1 = MineGUIColorPalette.GRAY;
        private MineGUIColorPalette rowColor2 = MineGUIColorPalette.DARK_GRAY;
        private MineGUIColorPalette textColor = MineGUIColorPalette.WHITE;
        private MineGUIColorPalette borderColor = MineGUIColorPalette.BLACK;

        public Builder() {}

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder columns(int columns) {
            this.columns = columns;
            return this;
        }

        public Builder columnWidth(int width) {
            this.columnWidth = width;
            return this;
        }

        public Builder rowHeight(int height) {
            this.rowHeight = height;
            return this;
        }

        public Builder headerColor(MineGUIColorPalette color) {
            this.headerColor = color;
            return this;
        }

        public Builder rowColor1(MineGUIColorPalette color) {
            this.rowColor1 = color;
            return this;
        }

        public Builder rowColor2(MineGUIColorPalette color) {
            this.rowColor2 = color;
            return this;
        }

        public Builder textColor(MineGUIColorPalette color) {
            this.textColor = color;
            return this;
        }

        public Builder borderColor(MineGUIColorPalette color) {
            this.borderColor = color;
            return this;
        }

        public MineGUITable build() {
            validate();
            return new MineGUITable(this);
        }

        private void validate() {
            if (columns <= 0) throw new IllegalArgumentException("Columns должны быть больше 0");
            if (columnWidth <= 0) throw new IllegalArgumentException("ColumnWidth должен быть больше 0");
            if (rowHeight <= 0) throw new IllegalArgumentException("RowHeight должен быть больше 0");
        }
    }
}
