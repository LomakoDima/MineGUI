package ru.dimalab.minegui.widgets.notifications;

import net.minecraft.network.chat.Component;

public record Notification(Component text,
                           NotificationType type,
                           long creationTime,
                           long duration,
                           Runnable onClick) {
}
