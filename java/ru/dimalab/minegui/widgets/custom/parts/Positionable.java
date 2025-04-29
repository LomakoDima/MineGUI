package ru.dimalab.minegui.widgets.custom.parts;

public interface Positionable {
    int DEFAULT_X = 0;
    int DEFAULT_Y = 0;

    default int getX() { return DEFAULT_X; }
    default int getY() { return DEFAULT_Y; }
    default void updatePosition(int screenWidth, int screenHeight) {}
    default void setPosition(int x, int y) {}
}
