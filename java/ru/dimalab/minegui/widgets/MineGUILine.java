package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import ru.dimalab.minegui.utils.BorderStyle;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUILine implements MineGUIWidget {
    private int x, y, length, thickness;
    private MineGUIColorPalette color;
    private MineGUIColorPalette startColor;
    private MineGUIColorPalette endColor;
    private boolean gradientEnabled;
    private boolean horizontal;
    private BorderStyle borderStyle;
    private boolean fadeEnabled = false;
    private boolean fadeFromStart = true;

    public MineGUILine(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.length = builder.length;
        this.thickness = builder.thickness;
        this.color = builder.color;
        this.horizontal = builder.horizontal;
        this.borderStyle = builder.borderStyle;
        this.fadeEnabled = builder.fadeEnabled;
        this.fadeFromStart = builder.fadeFromStart;
        this.startColor = builder.startColor;
        this.endColor = builder.endColor;
        this.gradientEnabled = builder.gradientEnabled;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        switch (borderStyle) {
            case SOLID -> renderSolidLine(graphics);
            case DOTTED -> renderDottedLine(graphics);
            case DASHED -> renderDashedLine(graphics);
            case DOUBLE -> renderDoubleLine(graphics);
            case DOT_DASH -> renderDotDashLine(graphics);
            case NONE, HIDDEN -> {}
        }
    }

    private void renderSolidLine(GuiGraphics graphics) {
        int startArgb = startColor.getColor();
        int endArgb = endColor.getColor();

        int steps = horizontal ? length : length;
        for (int i = 0; i < steps; i++) {
            float progress = (float) i / (float) steps;

            int color = gradientEnabled
                    ? interpolateColor(progress, startArgb, endArgb)
                    : startArgb;

            if(fadeEnabled) {
                float alphaFactor = fadeFromStart ? progress : (1.0f - progress);
                color = applyAlphaFactor(color, alphaFactor);
            }

            if (horizontal) {
                graphics.fill(x + i, y, x + i + 1, y + thickness, color);
            } else {
                graphics.fill(x, y + i, x + thickness, y + i + 1, color);
            }
        }
    }


    private void renderDottedLine(GuiGraphics graphics) {
        int step = 2;
        if (horizontal) {
            for (int i = 0; i < length; i += step * 2) {
                graphics.fill(x + i, y, Math.min(x + i + step, x + length), y + thickness, color.getColor());
            }
        } else {
            for (int i = 0; i < length; i += step * 2) {
                graphics.fill(x, y + i, x + thickness, Math.min(y + i + step, y + length), color.getColor());
            }
        }
    }

    private void renderDashedLine(GuiGraphics graphics) {
        int dashLength = 4;
        int gap = 2;
        if (horizontal) {
            for (int i = 0; i < length; i += dashLength + gap) {
                int end = Math.min(x + i + dashLength, x + length);
                graphics.fill(x + i, y, end, y + thickness, color.getColor());
            }
        } else {
            for (int i = 0; i < length; i += dashLength + gap) {
                int end = Math.min(y + i + dashLength, y + length);
                graphics.fill(x, y + i, x + thickness, end, color.getColor());
            }
        }
    }

    private void renderDoubleLine(GuiGraphics graphics) {
        int offset = Math.max(1, thickness / 3);
        if (horizontal) {
            graphics.fill(x, y, x + length, y + offset, color.getColor());
            graphics.fill(x, y + thickness - offset, x + length, y + thickness, color.getColor());
        } else {
            graphics.fill(x, y, x + offset, y + length, color.getColor());
            graphics.fill(x + thickness - offset, y, x + thickness, y + length, color.getColor());
        }
    }

    private void renderDotDashLine(GuiGraphics graphics) {
        int dotLength = 2;
        int dashLength = 4;
        int gap = 2;
        int patternLength = dotLength + gap + dashLength + gap;

        if (horizontal) {
            for (int i = 0; i < length; i += patternLength) {
                int dotEnd = Math.min(x + i + dotLength, x + length);
                graphics.fill(x + i, y, dotEnd, y + thickness, color.getColor());

                if (i + dotLength + gap < length) {
                    int dashStart = x + i + dotLength + gap;
                    int dashEnd = Math.min(dashStart + dashLength, x + length);
                    graphics.fill(dashStart, y, dashEnd, y + thickness, color.getColor());
                }
            }
        } else {
            for (int i = 0; i < length; i += patternLength) {
                int dotEnd = Math.min(y + i + dotLength, y + length);
                graphics.fill(x, y + i, x + thickness, dotEnd, color.getColor());

                if (i + dotLength + gap < length) {
                    int dashStart = y + i + dotLength + gap;
                    int dashEnd = Math.min(dashStart + dashLength, y + length);
                    graphics.fill(x, dashStart, x + thickness, dashEnd, color.getColor());
                }
            }
        }
    }

    private static int interpolateColor(float t, int color1, int color2) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = Mth.clamp((int) Mth.lerp(t, a1, a2), 0, 255);
        int r = Mth.clamp((int) Mth.lerp(t, r1, r2), 0, 255);
        int g = Mth.clamp((int) Mth.lerp(t, g1, g2), 0, 255);
        int b = Mth.clamp((int) Mth.lerp(t, b1, b2), 0, 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int applyAlphaFactor(int color, float factor) {
        int alpha = (int) (((color >> 24) & 0xFF) * factor);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static class Builder  {
        private int x;
        private int y;
        private int length;
        private int thickness;
        private MineGUIColorPalette color = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette startColor =  MineGUIColorPalette.TRANSPARENT; // Renamed
        private MineGUIColorPalette endColor = MineGUIColorPalette.TRANSPARENT;   // New
        private boolean gradientEnabled = false; // New
        private boolean horizontal;
        private BorderStyle borderStyle = BorderStyle.SOLID;
        private boolean fadeEnabled = false;
        private boolean fadeFromStart = true;

        public MineGUILine.Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public MineGUILine.Builder setLength(int length) {
            this.length = length;
            return this;
        }

        public MineGUILine.Builder setThickness(int thickness) {
            this.thickness = thickness;
            return this;
        }

        public Builder setColor(MineGUIColorPalette color) {
            this.startColor = color;
            this.endColor = color;
            this.gradientEnabled = false;
            return this;
        }

        public Builder setGradient(MineGUIColorPalette start, MineGUIColorPalette end) {
            this.startColor = start;
            this.endColor = end;
            this.gradientEnabled = true;
            return this;
        }


        public MineGUILine.Builder setHorizontal(boolean horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        public Builder setBorderStyle(BorderStyle style) {
            this.borderStyle = style;
            return this;
        }

        public Builder setFadeEnabled(boolean enabled) {
            this.fadeEnabled = enabled;
            return this;
        }

        public Builder setFadeFromStart(boolean fromStart) {
            this.fadeFromStart = fromStart;
            return this;
        }

        public Builder setGradientEnabled(boolean enabled) {
            this.gradientEnabled = enabled;
            return this;
        }

        public MineGUILine build() {
            if (startColor == null) {
                throw new IllegalStateException("Start color (setColor) must be set for MineGUILine");
            }
            if (gradientEnabled && endColor == null) {
                System.err.println("Warning: Building gradient MineGUILine without endColor set. Gradient will be disabled.");
                this.gradientEnabled = false;
            }
            return new MineGUILine(this);
        }

    }
}
