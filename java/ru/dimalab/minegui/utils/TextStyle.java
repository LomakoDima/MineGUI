package ru.dimalab.minegui.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

public enum TextStyle {
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKETHROUGH,
    OBFUSCATED;

    public String apply(String text) {
        return switch (this) {
            case BOLD -> "§l" + text;
            case ITALIC -> "§o" + text;
            case UNDERLINE -> "§n" + text;
            case STRIKETHROUGH -> "§m" + text;
            case OBFUSCATED -> "§k" + text;
        };
    }
}
