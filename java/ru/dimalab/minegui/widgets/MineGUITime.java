package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MineGUITime implements MineGUIWidget {
    private int x;
    private int y;
    private LocalTime currentTime;
    private final DateTimeFormatter formatter;
    private final Font font;

    public MineGUITime(Font font, int x, int y, String timePattern) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.formatter = DateTimeFormatter.ofPattern(timePattern);
        this.currentTime = LocalTime.now();

    }

    private void updateTime() {
        currentTime = LocalTime.now();
    }

    @Override
    public void tick() {
        updateTime();
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.drawString(
                font,
                formatter.format(currentTime),
                x,
                y,
                0xFFFFFF,
                false
        );
    }

}
