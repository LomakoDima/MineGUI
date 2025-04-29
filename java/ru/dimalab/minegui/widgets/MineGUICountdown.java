package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUICountdown implements MineGUIWidget {
    private int x;
    private int y;
    private int secondsRemaining;
    private long lastTickTime;
    private boolean active;

    public MineGUICountdown(int initialSeconds, int x, int y) {
        this.secondsRemaining = initialSeconds;
        this.x = x;
        this.y = y;
        this.lastTickTime = System.currentTimeMillis();
        this.active = true;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (active) {
            String timeText = formatTime(secondsRemaining);
            graphics.drawCenteredString(font, timeText, x + getWidth() / 2, y + getHeight() / 2 - 4, 0xFFFFFF);
        }
    }

    @Override
    public void tick() {
        if (active && secondsRemaining > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTickTime >= 1000L) {
                secondsRemaining--;
                lastTickTime = currentTime;
            }
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getWidth() { return 40; }

    @Override
    public int getHeight() { return 12; }

    public void start() { active = true; }
    public void stop() { active = false; }
    public void reset(int seconds) {
        secondsRemaining = seconds;
        lastTickTime = System.currentTimeMillis();
    }
    public boolean isFinished() { return secondsRemaining <= 0; }
}

