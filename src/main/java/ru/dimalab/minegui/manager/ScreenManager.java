package ru.dimalab.minegui.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import ru.dimalab.minegui.MineGUI;

import java.util.Stack;

public class ScreenManager {

    private static final Stack<Screen> screenStack = new Stack<>();

    private ScreenManager() {
    }

    public static void openScreen(Screen screen) {
        screenStack.push(screen);
        Minecraft.getInstance().setScreen(screen);
    }

    public static void closeScreen() {
        if (!screenStack.isEmpty()) {
            screenStack.pop();
        }

        if (!screenStack.isEmpty()) {
            Minecraft.getInstance().setScreen(screenStack.peek());
        } else {
            Minecraft.getInstance().setScreen(null);
        }
    }

    public static void clearScreens() {
        screenStack.clear();
        Minecraft.getInstance().setScreen(null);
    }

    public static Screen getCurrentScreen() {
        if (screenStack.isEmpty()) return null;
        return screenStack.peek();
    }

    public static void printStack() {
        screenStack.forEach(screen -> MineGUI.LOGGER.debug(screen.getClass().getSimpleName()));
    }
}
