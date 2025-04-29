package ru.dimalab.minegui.widgets.notifications;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface NotificationRenderer {
    void render(GuiGraphics graphics, Font font, Notification notification, int x, int y, int width, float alpha);
}
