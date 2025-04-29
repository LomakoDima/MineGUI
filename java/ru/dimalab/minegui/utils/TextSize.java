package ru.dimalab.minegui.utils;

public enum TextSize {
    TINY(0.75f),
    SMALL(0.9f),
    MEDIUM(1.0f),
    LARGE(1.2f),
    HUGE(1.5f);

    private final float scale;

    TextSize(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
}
