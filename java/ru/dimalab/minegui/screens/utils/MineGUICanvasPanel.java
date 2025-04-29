/* ! В разработке !

package ru.dimalab.minegui.screens.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUICanvasPanel implements MineGUIWidget {
    private final List<MineGUIWidget> children = new ArrayList<>();
    private boolean dragging = false;
    private boolean resizing = false;
    private int lastMouseX, lastMouseY;
    private int x, y, width, height;
    private static final int RESIZE_MARGIN = 5;
    private static final int BORDER_COLOR = 0xFFFFFFFF;
    private ResizeDirection resizeDirection = ResizeDirection.NONE;

    public MineGUICanvasPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addChild(MineGUIWidget widget) {
        children.add(widget);
        updateChildPositions();
    }

    public void removeChild(MineGUIWidget widget) {
        children.remove(widget);
    }

    private void updateChildPositions() {
        for (MineGUIWidget child : children) {
            int centerX = x + (width - child.getWidth()) / 2;
            int centerY = y + (height - child.getHeight()) / 2;
            child.setPosition(centerX, centerY);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, net.minecraft.client.gui.Font font, int mouseX, int mouseY) {
        guiGraphics.fill(x, y, x + width, y + height, 0xAA000000);
        renderOutline(guiGraphics, x, y, width, height, BORDER_COLOR);
        updateCursor(mouseX, mouseY);
        updateChildPositions();

        for (MineGUIWidget child : children) {
            child.render(guiGraphics, font, mouseX, mouseY);
        }
    }

    private void renderOutline(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.fill(x, y, x + width, y + 1, color);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, color);
        guiGraphics.fill(x, y + 1, x + 1, y + height - 1, color);
        guiGraphics.fill(x + width - 1, y + 1, x + width, y + height - 1, color);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        resizeDirection = getResizeDirection(mouseX, mouseY);
        if (resizeDirection != ResizeDirection.NONE && button == 0) {
            resizing = true;
            dragging = false;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        if (isMouseOver(mouseX, mouseY) && button == 0 && !resizing) {
            dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (resizing && button == 0) {
            int dx = mouseX - lastMouseX;
            int dy = mouseY - lastMouseY;

            switch (resizeDirection) {
                case RIGHT -> width += dx;
                case BOTTOM -> height += dy;
                case BOTTOM_RIGHT -> { width += dx; height += dy; }
            }
            updateChildPositions();
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        if (dragging && button == 0 && !resizing) {
            int dx = mouseX - lastMouseX;
            int dy = mouseY - lastMouseY;
            setPosition(x + dx, y + dy);
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragging = false;
            resizing = false;
            resizeDirection = ResizeDirection.NONE;
            resetCursor();
            return true;
        }
        return false;
    }

    @Override
    public void setPosition(int x, int y) {
        int dx = x - this.x;
        int dy = y - this.y;
        this.x = x;
        this.y = y;
        for (MineGUIWidget child : children) {
            child.setPosition(child.getX() + dx, child.getY() + dy);
        }
    }

    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    @Override
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    private ResizeDirection getResizeDirection(int mouseX, int mouseY) {
        boolean right = mouseX >= x + width - RESIZE_MARGIN;
        boolean bottom = mouseY >= y + height - RESIZE_MARGIN;

        if (right && bottom) return ResizeDirection.BOTTOM_RIGHT;
        if (right) return ResizeDirection.RIGHT;
        if (bottom) return ResizeDirection.BOTTOM;
        return ResizeDirection.NONE;
    }

    private void updateCursor(int mouseX, int mouseY) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        switch (getResizeDirection(mouseX, mouseY)) {
            case RIGHT -> GLFW.glfwSetCursor(window, GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR));
            case BOTTOM -> GLFW.glfwSetCursor(window, GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR));
            case BOTTOM_RIGHT -> GLFW.glfwSetCursor(window, GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR));
            default -> resetCursor();
        }
    }

    private void resetCursor() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        GLFW.glfwSetCursor(window, GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));
    }

    private enum ResizeDirection {
        NONE, RIGHT, BOTTOM, BOTTOM_RIGHT
    }
}

 */