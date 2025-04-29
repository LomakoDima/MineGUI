package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIColorIndicator  implements MineGUIWidget {
    private final MineGUIColorPicker colorPicker;
    private int x;
    private int y;
    private final int size;
    private boolean isPickerVisible = false;
    private int currentColor;

    public MineGUIColorIndicator(int x, int y, int size, int initialColor) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.currentColor = initialColor;
        this.colorPicker = new MineGUIColorPicker(x, y + size + 5, 150, initialColor);
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(x - 1, y - 1, x + size + 1, y + size + 1, 0xFF808080); // Граница
        graphics.fill(x, y, x + size, y + size, currentColor); // Цвет

        if (isPickerVisible) {
            colorPicker.render(graphics, font, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && isMouseOverIndicator(mouseX, mouseY)) {
            isPickerVisible = !isPickerVisible;
            return true;
        }

        if (isPickerVisible) {
            if (colorPicker.isMouseOver(mouseX, mouseY)) {
                return colorPicker.mouseClicked(mouseX, mouseY, button);
            }
            else {
                isPickerVisible = false;
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean mouseDragged(int mouseX, int mouseY, int button, double dragX, double dragY) {
        if (isPickerVisible) {
            return colorPicker.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        if (isPickerVisible) {
            boolean handledByPicker = colorPicker.mouseReleased(mouseX, mouseY, button);
            currentColor = colorPicker.getSelectedColorArgb();
            return handledByPicker;
        }
        return false;
    }

    private boolean isMouseOverIndicator(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + size && mouseY >= y && mouseY < y + size;
    }

    public int getCurrentColor() {
        return currentColor;
    }

}

