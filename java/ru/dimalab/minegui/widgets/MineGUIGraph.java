package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUIGraph implements MineGUIWidget {
    private int x;
    private int y;
    private int width;
    private int height;
    private final List<Float> dataPoints = new ArrayList<>();
    private int axisColor = 0xFF000000;
    private int graphColor = 0xFFFF0000;
    private int backgroundColor = 0x80FFFFFF;

    public MineGUIGraph(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(x, y, x + width, y + height, backgroundColor);

        graphics.hLine(x, x + width, y + height/2, axisColor);
        graphics.vLine(x + width/2, y, y + height, axisColor);

        if(dataPoints.size() > 1) {
            float maxValue = getMaxValue();
            float minValue = getMinValue();
            float verticalScale = height / (maxValue - minValue);

            for(int i = 1; i < dataPoints.size(); i++) {
                int x1 = x + (width * (i-1))/dataPoints.size();
                int y1 = y + height - (int)((dataPoints.get(i-1) - minValue) * verticalScale);
                int x2 = x + (width * i)/dataPoints.size();
                int y2 = y + height - (int)((dataPoints.get(i) - minValue) * verticalScale);

                graphics.fill(RenderType.gui(), x1, y1, x2, y2, 0, graphColor);
            }
        }
    }

    public void addDataPoint(float value) {
        dataPoints.add(value);
        if(dataPoints.size() > width/2) {
            dataPoints.remove(0);
        }
    }

    private float getMaxValue() {
        return dataPoints.stream().max(Float::compare).orElse(1f);
    }

    private float getMinValue() {
        return dataPoints.stream().min(Float::compare).orElse(0f);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setAxisColor(int color) {
        this.axisColor = color;
    }

    public void setGraphColor(int color) {
        this.graphColor = color;
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }
}
