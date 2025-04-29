package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.List;
import java.util.function.Consumer;

public class MineGUIDropdown implements MineGUIWidget {
    private final int x, y, width, height;
    private List<String> options;

    private String selectedOption = "";
    private boolean expanded = false;

    private int hoveredIndex = -1;

    private MineGUIColorPalette backgroundColor;
    private MineGUIColorPalette borderColor;
    private MineGUIColorPalette borderColorFocused;
    private MineGUIColorPalette textColor;
    private MineGUIColorPalette hoverColor;

    private Consumer<String> onSelect;

    public MineGUIDropdown(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.options = builder.options;
        this.backgroundColor = builder.backgroundColor;
        this.borderColor = builder.borderColor;
        this.borderColorFocused = builder.borderColorFocused;
        this.textColor = builder.textColor;
        this.hoverColor = builder.hoverColor;

        if (options.contains(builder.defaultOption)) {
            this.selectedOption = builder.defaultOption;
        } else if (!options.isEmpty()) {
            this.selectedOption = options.get(0);
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int border = expanded ? borderColorFocused.getColor() : borderColor.getColor();


        graphics.fill(x, y, x + width, y + height, backgroundColor.getColor());
        graphics.renderOutline(x, y, width, height, border);


        graphics.drawString(font, Component.literal(selectedOption), x + 4, y + (height - font.lineHeight) / 2, textColor.getColor(), false);


        int triangleX = x + width - 10;
        int triangleY = y + height / 2 - 2;
        graphics.fill(triangleX, triangleY, triangleX + 6, triangleY + 1, textColor.getColor());
        graphics.fill(triangleX + 1, triangleY + 1, triangleX + 5, triangleY + 2, textColor.getColor());
        graphics.fill(triangleX + 2, triangleY + 2, triangleX + 4, triangleY + 3, textColor.getColor());

        if (expanded) {
            renderDropdown(graphics, font, mouseX, mouseY);
        }
    }

    private void renderDropdown(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int optionHeight = height;
        int totalHeight = options.size() * optionHeight;

        graphics.fill(x, y + height, x + width, y + height + totalHeight, backgroundColor.getColor());


        hoveredIndex = -1;

        for (int i = 0; i < options.size(); i++) {
            int optionY = y + height + i * optionHeight;

            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= optionY && mouseY <= optionY + optionHeight;
            if (hovered) {
                hoveredIndex = i;
                graphics.fill(x, optionY, x + width, optionY + optionHeight, hoverColor.getColor());
            }

            graphics.drawString(font, Component.literal(options.get(i)), x + 4, optionY + (optionHeight - font.lineHeight) / 2, textColor.getColor(), false);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {

        if (isInside(mouseX, mouseY)) {
            expanded = !expanded;
            return true;
        }


        if (expanded) {
            int optionHeight = height;
            for (int i = 0; i < options.size(); i++) {
                int optionY = y + height + i * optionHeight;

                if (mouseX >= x && mouseX <= x + width && mouseY >= optionY && mouseY <= optionY + optionHeight) {
                    selectedOption = options.get(i);
                    expanded = false;

                    if (onSelect != null) {
                        onSelect.accept(selectedOption);
                    }
                    return true;
                }
            }

            expanded = false;
        }

        return false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static class Builder {
        private int x, y, width, height;
        private List<String> options;
        private String defaultOption = "";
        private MineGUIColorPalette backgroundColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette borderColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette borderColorFocused = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette textColor = MineGUIColorPalette.TRANSPARENT;
        private MineGUIColorPalette hoverColor = MineGUIColorPalette.TRANSPARENT;

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setOptions(List<String> options) {
            this.options = options;
            return this;
        }

        public Builder setDefaultOption(String defaultOption) {
            this.defaultOption = defaultOption;
            return this;
        }

        public Builder setBackgroundColor(MineGUIColorPalette backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setBorderColor(MineGUIColorPalette borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setBorderColorFocused(MineGUIColorPalette borderColorFocused) {
            this.borderColorFocused = borderColorFocused;
            return this;
        }

        public Builder setTextColor(MineGUIColorPalette textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setHoverColor(MineGUIColorPalette hoverColor) {
            this.hoverColor = hoverColor;
            return this;
        }

        public MineGUIDropdown build() {return new MineGUIDropdown(this);}
    }

}
