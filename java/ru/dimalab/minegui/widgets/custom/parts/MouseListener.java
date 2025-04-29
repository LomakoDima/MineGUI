package ru.dimalab.minegui.widgets.custom.parts;

public interface MouseListener {
    default boolean mouseClicked(int mouseX, int mouseY, int button) { return false; }
    default boolean mouseReleased(int mouseX, int mouseY, int button) { return false; }
    default boolean mouseDragged(int mouseX, int mouseY, int button, double dragX, double dragY) { return false; }
    default boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) { return false; }
}
