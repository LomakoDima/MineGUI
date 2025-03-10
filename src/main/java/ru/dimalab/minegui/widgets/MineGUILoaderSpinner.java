package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

public class MineGUILoaderSpinner {
    private final int x, y;
    private final int radius;
    private final int lineWidth;

    private int color = FastColor.ARGB32.color(255, 0, 120, 255);

    private float angle = 0f;
    private float speed = 6f;

    public MineGUILoaderSpinner(int x, int y, int radius, int lineWidth) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.lineWidth = lineWidth;
    }



    public void setColor(int color) {
        this.color = color;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }



    public void tick() {
        angle = (angle + speed) % 360;
    }



    public void render(GuiGraphics graphics) {
        int segments = 12;
        float segmentAngle = 360f / segments;

        for (int i = 0; i < segments; i++) {
            float currentAngle = angle + i * segmentAngle;

            double rad = Math.toRadians(currentAngle);
            float sin = (float) Math.sin(rad);
            float cos = (float) Math.cos(rad);

            int x1 = x + Math.round(cos * (radius - lineWidth));
            int y1 = y + Math.round(sin * (radius - lineWidth));
            int x2 = x + Math.round(cos * radius);
            int y2 = y + Math.round(sin * radius);

            int alpha = 50 + (205 * i / segments);
            int segmentColor = FastColor.ARGB32.color(alpha, (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF);

            graphics.fill(x1, y1, x2, y2, segmentColor);
        }
    }
}
