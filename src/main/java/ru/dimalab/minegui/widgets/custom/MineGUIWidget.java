package ru.dimalab.minegui.widgets.custom;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface MineGUIWidget {
    void render(GuiGraphics graphics, Font font, int mouseX, int mouseY);

    default void tick() {
    }

    default boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    default boolean mouseReleased(int mouseX, int mouseY, int button) {
        return false;
    }

    default boolean mouseDragged(int mouseX, int mouseY, int button, double dragX, double dragY) {
        return false;
    }

    default boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
}
