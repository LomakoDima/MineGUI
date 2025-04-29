package ru.dimalab.minegui.widgets.custom;

import ru.dimalab.minegui.screens.tooltips.Tooltip;
import ru.dimalab.minegui.widgets.custom.parts.*;

import java.util.Optional;

public interface MineGUIWidget extends
        Renderable,
        MouseListener,
        KeyListener,
        Tickable,
        Positionable,
        Resizable {
    default Optional<Tooltip> getTooltip(int mouseX, int mouseY) {
        return Optional.empty();
}}
