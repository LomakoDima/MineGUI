package ru.dimalab.minegui.widgets.custom.parts;

public interface Resizable {
    default int getWidth() { return 0; }
    default int getHeight() { return 0; }
}
