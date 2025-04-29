/*  В разработке...

package ru.dimalab.minegui.widgets;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class CircleWidget implements MineGUIWidget {
    private final int centerX;
    private final int centerY;
    private final int radius;
    private final int color;
    private final int superSampling;

    public CircleWidget(int centerX, int centerY,
                                   int radius, MineGUIColorPalette color) {
        this(centerX, centerY, radius, color, 4);
    }

    public CircleWidget(int centerX, int centerY,
                                   int radius, MineGUIColorPalette color,
                                   int superSampling) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.color = color.getColor();
        this.superSampling = Math.max(1, superSampling);
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        drawSupersampledCircle(graphics, centerX, centerY, radius, color);
    }

    private void drawSupersampledCircle(GuiGraphics graphics, int x0, int y0, int r, int color) {
        final int baseAlpha = (color >> 24) & 0xFF;
        final int red = (color >> 16) & 0xFF;
        final int green = (color >> 8) & 0xFF;
        final int blue = color & 0xFF;

        final int size = r * 2 + 1;
        final float[][] alphaMap = new float[size][size];

        // Предварительный расчет прозрачности
        for (int y = -r; y <= r; y++) {
            for (int x = -r; x <= r; x++) {
                float distance = (float) Math.sqrt(x*x + y*y);
                float delta = Math.abs(distance - r);
                alphaMap[y + r][x + r] = Math.max(0, 1f - delta * superSampling);
            }
        }

        // Отрисовка с усреднением
        for (int y = -r; y <= r; y++) {
            for (int x = -r; x <= r; x++) {
                if (x*x + y*y <= (r + 1)*(r + 1)) {
                    float alphaSum = 0;

                    // Суперсэмплинг
                    for (int dy = 0; dy < superSampling; dy++) {
                        for (int dx = 0; dx < superSampling; dx++) {
                            float fx = x + dx / (float) superSampling;
                            float fy = y + dy / (float) superSampling;
                            float distance = (float) Math.sqrt(fx*fx + fy*fy);
                            alphaSum += Math.max(0, 1f - Math.abs(distance - r));
                        }
                    }

                    float alpha = alphaSum / (superSampling * superSampling);
                    int finalAlpha = (int) (baseAlpha * alpha);
                    int finalColor = (finalAlpha << 24) | (red << 16) | (green << 8) | blue;
                    graphics.fill(x0 + x, y0 + y, x0 + x + 1, y0 + y + 1, finalColor);
                }
            }
        }
    }
}

 */