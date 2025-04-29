package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import ru.dimalab.minegui.screens.tooltips.Tooltip;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.awt.*;
import java.util.*;

public class MineGUIColorPicker implements MineGUIWidget {
    private int x;
    private int y;
    private final int width;
    private final int height;

    private final int svBoxSize;
    private final int hueSliderWidth = 15;
    private final int previewSize = 25;
    private final int padding = 5;
    private final int infoHeight = 50;

    private float hue = 0.0f;
    private float saturation = 0.0f;
    private float value = 1.0f;

    private int selectedColorArgb;
    private final int originalColorArgb;

    private boolean draggingSv = false;
    private boolean draggingHue = false;



    public MineGUIColorPicker(int x, int y, int size, int initialColorArgb) {
        this.x = x;
        this.y = y;
        this.svBoxSize = size;
        this.originalColorArgb = initialColorArgb;
        this.selectedColorArgb = initialColorArgb;

        this.width = svBoxSize + padding + hueSliderWidth + padding + previewSize + padding + 80;
        this.height = Math.max(svBoxSize, previewSize * 2 + padding + infoHeight);

        updateHsvFromRgb(initialColorArgb);
    }

    private void updateHsvFromRgb(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.value = hsb[2];
        updateSelectedColorFromHsv();
    }

    private void updateSelectedColorFromHsv() {
        this.selectedColorArgb = Color.HSBtoRGB(this.hue, this.saturation, this.value) | 0xFF000000;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int currentX = x;

        renderSvBox(graphics, currentX, y);
        currentX += svBoxSize + padding;

        renderHueSlider(graphics, currentX, y);
        currentX += hueSliderWidth + padding;

        renderInfoPanel(graphics, font, currentX, y);
    }

    private void renderSvBox(GuiGraphics graphics, int boxX, int boxY) {
        for (int i = 0; i < svBoxSize; ++i) {
            float currentSaturation = (float) i / (float) svBoxSize;

            int colorTop = Color.HSBtoRGB(this.hue, currentSaturation, 1.0f) | 0xFF000000;

            int colorBottom = 0xFF000000;

            graphics.fillGradient(
                    boxX + i,
                    boxY,
                    boxX + i + 1,
                    boxY + svBoxSize,
                    colorTop,
                    colorBottom
            );
        }

        graphics.renderOutline(boxX - 1, boxY - 1, svBoxSize + 2, svBoxSize + 2, 0xFF606060);

        int selectorX = boxX + Mth.clamp((int) (this.saturation * svBoxSize), 0, svBoxSize > 0 ? svBoxSize -1 : 0);
        int selectorY = boxY + Mth.clamp((int) ((1.0f - this.value) * svBoxSize), 0, svBoxSize > 0 ? svBoxSize -1 : 0);

        int r = (selectedColorArgb >> 16) & 0xFF;
        int g = (selectedColorArgb >> 8) & 0xFF;
        int b = selectedColorArgb & 0xFF;
        double luminance = (0.2126 * r + 0.7152 * g + 0.0722 * b) / 255.0;
        int selectorOuterColor = luminance > 0.5 ? 0xFF000000 : 0xFFFFFFFF; // Black on light, White on dark
        int selectorInnerColor = luminance > 0.5 ? 0xFFFFFFFF : 0xFF000000; // Opposite for inner part

        int radius = 3;

        graphics.fill(selectorX - radius, selectorY - radius + 1, selectorX - radius + 1, selectorY + radius, selectorOuterColor); // Left edge
        graphics.fill(selectorX + radius, selectorY - radius + 1, selectorX + radius + 1, selectorY + radius, selectorOuterColor); // Right edge
        graphics.fill(selectorX - radius + 1, selectorY - radius, selectorX + radius, selectorY - radius + 1, selectorOuterColor); // Top edge
        graphics.fill(selectorX - radius + 1, selectorY + radius, selectorX + radius, selectorY + radius + 1, selectorOuterColor); // Bottom edge

        int innerRadius = radius -1;
        graphics.fill(selectorX - innerRadius, selectorY - innerRadius + 1, selectorX - innerRadius + 1, selectorY + innerRadius, selectorInnerColor); // Left edge
        graphics.fill(selectorX + innerRadius, selectorY - innerRadius + 1, selectorX + innerRadius + 1, selectorY + innerRadius, selectorInnerColor); // Right edge
        graphics.fill(selectorX - innerRadius + 1, selectorY - innerRadius, selectorX + innerRadius, selectorY - innerRadius + 1, selectorInnerColor); // Top edge
        graphics.fill(selectorX - innerRadius + 1, selectorY + innerRadius, selectorX + innerRadius, selectorY + innerRadius + 1, selectorInnerColor); // Bottom edge

        graphics.renderOutline(boxX - 1, boxY - 1, svBoxSize + 2, svBoxSize + 2, 0xFF808080); // Grey outline
    }


    private void renderHueSlider(GuiGraphics graphics, int sliderX, int sliderY) {
        for (int j = 0; j < svBoxSize; ++j) {
            float currentHue = (float) j / svBoxSize;
            int color = Color.HSBtoRGB(currentHue, 1.0f, 1.0f) | 0xFF000000;
            graphics.fill(sliderX, sliderY + j, sliderX + hueSliderWidth, sliderY + j + 1, color);
        }

        graphics.renderOutline(sliderX - 1, sliderY - 1, hueSliderWidth + 2, svBoxSize + 2, 0xFF808080); // Grey outline

        int selectorY = sliderY + (int) (this.hue * svBoxSize);
        int markerColor = 0xFFFFFFFF;

        graphics.fill(sliderX - 2, selectorY - 1, sliderX + hueSliderWidth + 2, selectorY + 1, markerColor);
        graphics.fill(sliderX - 1, selectorY - 2, sliderX + hueSliderWidth + 1, selectorY + 2, 0xFF000000); // Black border for visibility
        graphics.fill(sliderX, selectorY - 1, sliderX + hueSliderWidth, selectorY + 1, markerColor);


        /* // Alternative Triangle marker (more complex drawing)
         int[] xPoints = { sliderX - 3, sliderX + hueSliderWidth + 3, sliderX - 3 };
         int[] yPoints = { selectorY - 3, selectorY, selectorY + 3 };
         // GuiGraphics doesn't have drawPolygon, would need manual line drawing or Tessellator
         graphics.fill(sliderX - 2, selectorY - 2, sliderX - 1, selectorY + 2, markerColor);
         graphics.fill(sliderX + hueSliderWidth + 1, selectorY - 1, sliderX + hueSliderWidth + 2, selectorY + 1, markerColor);
         */
    }

    private void renderInfoPanel(GuiGraphics graphics, Font font, int infoX, int infoY) {
        int currentY = infoY;

        graphics.drawString(font, "Current", infoX, currentY, 0xFFFFFF, false);
        currentY += font.lineHeight + 2;
        graphics.fill(infoX - 1, currentY - 1, infoX + previewSize + 1, currentY + previewSize + 1, 0xFF000000); // Border
        graphics.fill(infoX, currentY, infoX + previewSize, currentY + previewSize, this.selectedColorArgb);
        currentY += previewSize + padding;

        graphics.drawString(font, "Original", infoX, currentY, 0xFFFFFF, false);
        currentY += font.lineHeight + 2;
        graphics.fill(infoX - 1, currentY - 1, infoX + previewSize + 1, currentY + previewSize + 1, 0xFF000000); // Border
        graphics.fill(infoX, currentY, infoX + previewSize, currentY + previewSize, this.originalColorArgb);
        currentY += previewSize + padding;

        int r = (selectedColorArgb >> 16) & 0xFF;
        int g = (selectedColorArgb >> 8) & 0xFF;
        int b = selectedColorArgb & 0xFF;
        String hex = String.format("#%06X", selectedColorArgb & 0xFFFFFF);

        int textX = x + svBoxSize + padding + hueSliderWidth + padding;

        graphics.drawString(font, String.format("R: %d", r), textX, currentY, 0xFFFFFF, false);
        graphics.drawString(font, String.format("G: %d", g), textX + 40, currentY, 0xFFFFFF, false);
        graphics.drawString(font, String.format("B: %d", b), textX + 80, currentY, 0xFFFFFF, false);
        currentY += font.lineHeight + 2;

        graphics.drawString(font, String.format("H: %.0f°", this.hue * 360f), textX, currentY, 0xFFFFFF, false);
        graphics.drawString(font, String.format("S: %.0f%%", this.saturation * 100f), textX + 40, currentY, 0xFFFFFF, false);
        graphics.drawString(font, String.format("V: %.0f%%", this.value * 100f), textX + 80, currentY, 0xFFFFFF, false);
        currentY += font.lineHeight + 2;

        graphics.drawString(font, hex, textX, currentY, 0xFFFFFF, false);
    }


    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            int svBoxX = x;
            int svBoxY = y;
            int hueSliderX = x + svBoxSize + padding;
            int hueSliderY = y;

            if (mouseX >= svBoxX && mouseX < svBoxX + svBoxSize && mouseY >= svBoxY && mouseY < svBoxY + svBoxSize) {
                draggingSv = true;
                updateSv(mouseX, mouseY);
                return true;
            }

            if (mouseX >= hueSliderX && mouseX < hueSliderX + hueSliderWidth && mouseY >= hueSliderY && mouseY < hueSliderY + svBoxSize) {
                draggingHue = true;
                updateHue(mouseY);
                return true;
            }
        } else if (button == 1) {
            if (isMouseOver(mouseX, mouseY)) {
                resetToOriginal();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(int mouseX, int mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            if (draggingSv) {
                updateSv(mouseX, mouseY);
                return true;
            }
            if (draggingHue) {
                updateHue(mouseY);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            boolean wasDragging = draggingSv || draggingHue;
            draggingSv = false;
            draggingHue = false;
            return wasDragging;
        }
        return false;
    }

    private void updateSv(int mouseX, int mouseY) {
        int svBoxX = x;
        int svBoxY = y;

        float clampedMouseX = Mth.clamp(mouseX, svBoxX, svBoxX + svBoxSize -1);
        float clampedMouseY = Mth.clamp(mouseY, svBoxY, svBoxY + svBoxSize -1);

        this.saturation = (clampedMouseX - svBoxX) / (float)svBoxSize;
        this.value = 1.0f - (clampedMouseY - svBoxY) / (float)svBoxSize;

        updateSelectedColorFromHsv();
    }

    private void updateHue(int mouseY) {
        int hueSliderY = y;

        float clampedMouseY = Mth.clamp(mouseY, hueSliderY, hueSliderY + svBoxSize - 1);

        this.hue = (clampedMouseY - hueSliderY) / (float)svBoxSize;

        updateSelectedColorFromHsv();
    }


    public void resetToOriginal() {
        updateHsvFromRgb(this.originalColorArgb);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
    }

    public int getSelectedColorArgb() {
        return selectedColorArgb;
    }

    public void setSelectedColor(int argb) {
        updateHsvFromRgb(argb);
    }
}
