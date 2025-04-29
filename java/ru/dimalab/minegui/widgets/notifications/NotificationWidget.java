package ru.dimalab.minegui.widgets.notifications;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class NotificationWidget  implements MineGUIWidget {
    private final NotificationManager manager = new NotificationManager();
    private int screenWidth;
    private int screenHeight;

    public NotificationWidget() {
        updatePosition(0, 0); // Инициализация
    }

    public void addNotification(Component text, NotificationType type, long durationMs) {
        manager.addNotification(new Notification(
                text, type, System.currentTimeMillis(), durationMs, null
        ));
    }

    @Override
    public void updatePosition(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        manager.render(graphics, font, screenWidth, screenHeight);
    }

    @Override
    public void tick() {
        manager.tick();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }
}
