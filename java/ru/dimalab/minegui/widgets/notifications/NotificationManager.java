package ru.dimalab.minegui.widgets.notifications;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.parts.Tickable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager implements Tickable {
    private static final int MAX_NOTIFICATIONS = 5;
    private static final int NOTIFICATION_WIDTH = 200;
    private static final int MARGIN = 5;
    private static final int ANIMATION_TIME = 500;

    private final List<Notification> notifications = new CopyOnWriteArrayList<>();
    private NotificationRenderer renderer = this::defaultRenderer;

    public void addNotification(Notification notification) {
        notifications.removeIf(n -> n.text().equals(notification.text()));
        notifications.add(notification);
        if (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(0);
        }
    }

    public void setRenderer(NotificationRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void tick() {
        long currentTime = System.currentTimeMillis();
        notifications.removeIf(n -> currentTime - n.creationTime() > n.duration());
    }

    public void render(GuiGraphics graphics, Font font, int screenWidth, int screenHeight) {
        int yPos = screenHeight - 50;

        for (Notification notification : notifications) {
            long timeAlive = System.currentTimeMillis() - notification.creationTime();
            float alpha = Math.min(1, Math.min(timeAlive / (float) ANIMATION_TIME,
                    (notification.duration() - timeAlive) / (float) ANIMATION_TIME));

            renderer.render(graphics, font, notification,
                    screenWidth - NOTIFICATION_WIDTH - MARGIN, yPos,
                    NOTIFICATION_WIDTH, alpha);

            yPos -= 30;
        }
    }

    private void defaultRenderer(GuiGraphics graphics, Font font, Notification notification,
                                 int x, int y, int width, float alpha) {

        graphics.fill(x, y, x + width, y + 25,
                (int) (alpha * 255) << 24 | notification.type().backgroundColor);

        graphics.drawString(font, notification.type().icon, x + 5, y + 5,
                notification.type().primaryColor | ((int) (alpha * 255) << 24), false);

        graphics.drawString(font, notification.text(), x + 20, y + 5,
                0xFFFFFF | ((int) (alpha * 255) << 24), false);
    }
}

