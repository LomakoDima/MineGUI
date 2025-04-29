package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.screens.tooltips.Tooltip;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MineGUIRadioGroup implements MineGUIWidget {
    private final List<RadioButton> buttons = new ArrayList<>();
    private Consumer<String> onSelect;
    private int x, y, width, height;

    public static class Builder {
        private final MineGUIRadioGroup group = new MineGUIRadioGroup();
        private final List<RadioButtonParams> buttonParams = new ArrayList<>();

        public Builder addButton(int x, int y, int size, String label) {
            buttonParams.add(new RadioButtonParams(x, y, size, label));
            return this;
        }

        public Builder setOnSelect(Consumer<String> onSelect) {
            group.onSelect = onSelect;
            return this;
        }

        public MineGUIRadioGroup build() {
            buttonParams.forEach(params ->
                    group.addButton(new RadioButton(params.x, params.y, params.size, params.label))
            );
            group.calculateBounds();
            return group;
        }

        private record RadioButtonParams(int x, int y, int size, String label) {}
    }

    private MineGUIRadioGroup() {}

    private void addButton(RadioButton button) {
        buttons.add(button);
        if (buttons.size() == 1) {
            selectButton(button);
        }
    }

    private void calculateBounds() {
        int minX = buttons.stream().mapToInt(b -> b.x).min().orElse(0);
        int minY = buttons.stream().mapToInt(b -> b.y).min().orElse(0);
        int maxX = buttons.stream().mapToInt(b -> b.x + b.size).max().orElse(0);
        int maxY = buttons.stream().mapToInt(b -> b.y + b.size).max().orElse(0);

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    private void selectButton(RadioButton button) {
        buttons.forEach(b -> b.selected = false);
        button.selected = true;
        if (onSelect != null) onSelect.accept(button.label);
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        buttons.forEach(btn -> btn.render(graphics, font, mouseX, mouseY));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        for (RadioButton rb : buttons) {
            if (rb.isInside(mouseX, mouseY)) {
                selectButton(rb);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public void setPosition(int x, int y) {
        int dx = x - this.x;
        int dy = y - this.y;
        buttons.forEach(btn -> {
            btn.x += dx;
            btn.y += dy;
        });
        calculateBounds();
    }


    private static class RadioButton {
        int x, y, size;
        String label;
        boolean selected;

        int textColor = 0xFFFFFF;
        int bgColor = FastColor.ARGB32.color(255, 60, 60, 60);
        int borderColor = FastColor.ARGB32.color(255, 100, 100, 100);
        int selectedColor = FastColor.ARGB32.color(255, 0, 120, 255);
        int hoverColor = FastColor.ARGB32.color(255, 80, 80, 80);

        RadioButton(int x, int y, int size, String label) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.label = label;
        }

        void render(GuiGraphics gui, Font font, int mouseX, int mouseY) {
            boolean hovered = isInside(mouseX, mouseY);

            gui.fill(x, y, x + size, y + size, hovered ? hoverColor : bgColor);
            gui.renderOutline(x, y, size, size, borderColor);

            if (selected) {
                int margin = size / 4;
                gui.fill(x + margin, y + margin, x + size - margin, y + size - margin, selectedColor);
            }

            gui.drawString(font, Component.literal(label), x + size + 5, y + (size - font.lineHeight)/2, textColor);
        }

        boolean isInside(int mx, int my) {
            return mx >= x && mx <= x + size && my >= y && my <= y + size;
        }
    }
}
