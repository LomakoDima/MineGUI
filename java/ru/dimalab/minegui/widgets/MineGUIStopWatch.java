package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIStopWatch implements MineGUIWidget {
    private int x;
    private int y;
    private long startTime;
    private long accumulatedTime;
    private boolean isRunning;
    private boolean wasClicked;

    public MineGUIStopWatch(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.accumulatedTime = builder.initialTime;
        this.isRunning = builder.startImmediately;
        this.startTime = System.currentTimeMillis();

        if (!builder.startImmediately && builder.pausedImmediately) {
            this.isRunning = false;
        }
        reset();
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        String timeText = formatTime(getCurrentTime());
        graphics.drawCenteredString(font, timeText, x + getWidth()/2, y + getHeight()/2 - 4, 0xFFFFFF);
    }

    @Override
    public void tick() {
        if(isRunning) {
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if(isHovered(mouseX, mouseY)) {
            if(button == 0) { // ЛКМ
                toggle();
                return true;
            }
            if(button == 1) { // ПКМ
                reset();
                return true;
            }
        }
        return false;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + getWidth() &&
                mouseY >= y && mouseY <= y + getHeight();
    }

    private String formatTime(long millis) {
        long minutes = (millis / 60000) % 60;
        long seconds = (millis / 1000) % 60;
        long milliseconds = (millis % 1000) / 10;
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
    }

    public void toggle() {
        if(isRunning) {
            pause();
        } else {
            start();
        }
    }

    public void start() {
        if(!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public void pause() {
        if(isRunning) {
            accumulatedTime += System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public void reset() {
        accumulatedTime = 0;
        startTime = System.currentTimeMillis();
        isRunning = false;
    }

    public long getCurrentTime() {
        return isRunning ?
                accumulatedTime + (System.currentTimeMillis() - startTime) :
                accumulatedTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public static class Builder {
        private int x;
        private int y;
        private long initialTime = 0;
        private boolean startImmediately = false;
        private boolean pausedImmediately = false;


        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setInitialTime(long initialTime) {
            this.initialTime = initialTime;
            return this;
        }

        public Builder startImmediately() {
            this.startImmediately = true;
            this.pausedImmediately = false;
            return this;
        }

        public Builder pauseImmediately() {
            this.startImmediately = false;
            this.pausedImmediately = true;
            return this;
        }

        public Builder resetImmediately() {
            this.initialTime = 0;
            this.startImmediately = false;
            this.pausedImmediately = false;
            return this;
        }

        public MineGUIStopWatch build() {
            return new MineGUIStopWatch(this);
        }
    }
}
