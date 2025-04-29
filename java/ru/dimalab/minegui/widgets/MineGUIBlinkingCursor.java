/*

package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIBlinkingCursor implements MineGUIWidget {
    private int x, y;
    private long lastBlinkTime;
    private boolean visible;

    public MineGUIBlinkingCursor(int x, int y) {
        this.x = x;
        this.y = y;
        this.lastBlinkTime = System.currentTimeMillis();
        this.visible = true;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBlinkTime > 500) { // мигает каждые 500 мс
            visible = !visible;
            lastBlinkTime = currentTime;
        }
        if (visible) {
            graphics.fill(x, y, x + 2, y + 10, 0xFFFFFFFF); // белый вертикальный курсор
        }
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

 */