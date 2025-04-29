package ru.dimalab.minegui.widgets.notifications;

import ru.dimalab.minegui.utils.MineGUIColorPalette;

public enum NotificationType {
    INFO(MineGUIColorPalette.WHITE.getColor(), MineGUIColorPalette.FOREST_GREEN.getColor(), "i"),
    WARNING(MineGUIColorPalette.WHITE.getColor(), MineGUIColorPalette.MUSTARD.getColor(), "!"),
    ERROR(MineGUIColorPalette.WHITE.getColor(), MineGUIColorPalette.DARK_RED.getColor(), "X");

    public final int primaryColor;
    public final int backgroundColor;
    public final String icon;

    NotificationType(int primaryColor, int backgroundColor, String icon) {
        this.primaryColor = primaryColor;
        this.backgroundColor = backgroundColor;
        this.icon = icon;
    }
}
