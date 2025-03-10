package ru.dimalab.minegui.utils;

public enum TextStyle {
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKETHROUGH,
    SHADOW;

    public String apply(String text) {
        return switch (this) {
            case BOLD -> "§l" + text;
            case ITALIC -> "§o" + text;
            case UNDERLINE -> "§n" + text;
            case STRIKETHROUGH -> "§m" + text;
            case SHADOW -> "§k" + text;
        };
    }
}
