package ru.dimalab.minegui.utils;

public enum Visibility {
    VISIBLE,
    HIDDEN,
    GONE;

    public boolean isVisible() {
        return this == VISIBLE;
    }

    public boolean isGone() {
        return this == GONE;
    }

    public boolean isHidden() {
        return this == HIDDEN;
    }
}
