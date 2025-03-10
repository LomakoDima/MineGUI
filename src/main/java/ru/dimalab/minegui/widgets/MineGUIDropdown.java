package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.List;
import java.util.function.Consumer;

public class MineGUIDropdown {
    private final int x, y, width, height;
    private List<String> options;

    private String selectedOption = "";
    private boolean expanded = false;

    private int hoveredIndex = -1;

    private int backgroundColor = FastColor.ARGB32.color(255, 40, 40, 40);
    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int borderColorFocused = FastColor.ARGB32.color(255, 0, 120, 255);
    private int textColor = 0xFFFFFF;
    private int hoverColor = FastColor.ARGB32.color(255, 60, 60, 60);

    private Consumer<String> onSelect;

    public MineGUIDropdown(int x, int y, int width, int height, List<String> options, String defaultOption) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.options = options;

        if (options.contains(defaultOption)) {
            this.selectedOption = defaultOption;
        } else if (!options.isEmpty()) {
            this.selectedOption = options.get(0);
        }
    }



    public void setOnSelect(Consumer<String> onSelect) {
        this.onSelect = onSelect;
    }

    public String getSelectedOption() {
        return selectedOption;
    }



    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int border = expanded ? borderColorFocused : borderColor;


        graphics.fill(x, y, x + width, y + height, backgroundColor);
        graphics.renderOutline(x, y, width, height, border);


        graphics.drawString(font, Component.literal(selectedOption), x + 4, y + (height - font.lineHeight) / 2, textColor, false);


        int triangleX = x + width - 10;
        int triangleY = y + height / 2 - 2;
        graphics.fill(triangleX, triangleY, triangleX + 6, triangleY + 1, textColor);
        graphics.fill(triangleX + 1, triangleY + 1, triangleX + 5, triangleY + 2, textColor);
        graphics.fill(triangleX + 2, triangleY + 2, triangleX + 4, triangleY + 3, textColor);

        if (expanded) {
            renderDropdown(graphics, font, mouseX, mouseY);
        }
    }

    private void renderDropdown(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int optionHeight = height;
        int totalHeight = options.size() * optionHeight;

        graphics.fill(x, y + height, x + width, y + height + totalHeight, backgroundColor);


        hoveredIndex = -1;

        for (int i = 0; i < options.size(); i++) {
            int optionY = y + height + i * optionHeight;

            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= optionY && mouseY <= optionY + optionHeight;
            if (hovered) {
                hoveredIndex = i;
                graphics.fill(x, optionY, x + width, optionY + optionHeight, hoverColor);
            }

            graphics.drawString(font, Component.literal(options.get(i)), x + 4, optionY + (optionHeight - font.lineHeight) / 2, textColor, false);
        }
    }



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
}
