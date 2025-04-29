package ru.dimalab.minegui.widgets.custom.parts;

public interface KeyListener {
    default boolean charTyped(char codePoint, int modifiers) { return false; }
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) { return false; }
}
