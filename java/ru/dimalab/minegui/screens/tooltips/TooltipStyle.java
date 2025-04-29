package ru.dimalab.minegui.screens.tooltips;

import ru.dimalab.minegui.utils.MineGUIColorPalette;

public record TooltipStyle(int backgroundColor,
                           int textColor,
                           int padding,
                           int borderColor,
                           int borderRadius)  {
    public static final TooltipStyle DEFAULT = new TooltipStyle(
            MineGUIColorPalette.DARK_GRAY.getColor(),
            MineGUIColorPalette.WHITE.getColor(),
            4,
            MineGUIColorPalette.BLACK.getColor(),
            3
    );
    public static final TooltipStyle ORANGE = new TooltipStyle(
            MineGUIColorPalette.ORANGE.getColor(),
            MineGUIColorPalette.WHITE.getColor(),
            4,
            MineGUIColorPalette.GOLD.getColor(),
            3
    );
}
