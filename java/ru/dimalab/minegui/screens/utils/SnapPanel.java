package ru.dimalab.minegui.screens.utils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;

import java.awt.*;


public class SnapPanel {
    private static final int PANEL_SIZE = 100;
    private static final int ZONE_SIZE = 40;
    private static final int EDGE_MARGIN = 10;

    private final int screenWidth;
    private final int screenHeight;
    private Rectangle topZone;
    private Rectangle bottomZone;
    private Rectangle leftZone;
    private Rectangle rightZone;
    private int hoveredZone = -1;

    public SnapPanel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        calculateZones();
    }

    private void calculateZones() {
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;

        topZone = new Rectangle(centerX - ZONE_SIZE/2, centerY - PANEL_SIZE/2, ZONE_SIZE, ZONE_SIZE);
        bottomZone = new Rectangle(centerX - ZONE_SIZE/2, centerY + PANEL_SIZE/2 - ZONE_SIZE, ZONE_SIZE, ZONE_SIZE);
        leftZone = new Rectangle(centerX - PANEL_SIZE/2, centerY - ZONE_SIZE/2, ZONE_SIZE, ZONE_SIZE);
        rightZone = new Rectangle(centerX + PANEL_SIZE/2 - ZONE_SIZE, centerY - ZONE_SIZE/2, ZONE_SIZE, ZONE_SIZE);
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        // Фон панели
        graphics.fill(screenWidth/2 - PANEL_SIZE/2, screenHeight/2 - PANEL_SIZE/2,
                screenWidth/2 + PANEL_SIZE/2, screenHeight/2 + PANEL_SIZE/2, 0xAA222222);

        // Отрисовка зон с подсветкой
        renderZone(graphics, topZone, 0x66FFFFFF);
        renderZone(graphics, bottomZone, 0x66FFFFFF);
        renderZone(graphics, leftZone, 0x66FFFFFF);
        renderZone(graphics, rightZone, 0x66FFFFFF);

        // Подсветка выбранной зоны
        if(hoveredZone != -1) {
            int color = 0x88FFFFFF;
            switch(hoveredZone) {
                case 0 -> renderZone(graphics, topZone, color);
                case 1 -> renderZone(graphics, bottomZone, color);
                case 2 -> renderZone(graphics, leftZone, color);
                case 3 -> renderZone(graphics, rightZone, color);
            }
        }
    }

    private void renderZone(GuiGraphics graphics, Rectangle rect, int color) {
        graphics.fill(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, color);
    }

    public int checkHoveredZone(int mouseX, int mouseY) {
        hoveredZone = -1;
        if(topZone.contains(mouseX, mouseY)) hoveredZone = 0;
        else if(bottomZone.contains(mouseX, mouseY)) hoveredZone = 1;
        else if(leftZone.contains(mouseX, mouseY)) hoveredZone = 2;
        else if(rightZone.contains(mouseX, mouseY)) hoveredZone = 3;
        return hoveredZone;
    }

    public void snapWindow(CollapsibleWindow window, int zone) {
        switch(zone) {
            case 0 -> window.setPosition(window.getX(), EDGE_MARGIN);
            case 1 -> window.setPosition(window.getX(), screenHeight - window.getHeight() - EDGE_MARGIN);
            case 2 -> window.setPosition(EDGE_MARGIN, window.getY());
            case 3 -> window.setPosition(screenWidth - window.getWidth() - EDGE_MARGIN, window.getY());
        }
    }
}
