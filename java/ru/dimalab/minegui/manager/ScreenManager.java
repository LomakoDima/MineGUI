package ru.dimalab.minegui.manager;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;


import org.apache.logging.log4j.Logger;
import ru.dimalab.minegui.MineGUI;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScreenManager {

    private static final Logger LOGGER = LogManager.getLogger(ScreenManager.class);
    private static final Deque<Screen> screenStack = new ArrayDeque<>();
    private static final Lock stackLock = new ReentrantLock();

    private ScreenManager() {
    }

    public static void register() {
        LOGGER.info("ScreenManager registered successfully.");
    }


    public static void openScreen(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen cannot be null");
        }

        stackLock.lock();
        try {
            if (!screenStack.isEmpty() && screenStack.peek() == screen) {
                LOGGER.debug("Screen {} is already active", screen.getClass().getSimpleName());
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(screen);
            screenStack.push(screen);
            LOGGER.debug("Opened screen: {}", screen.getClass().getSimpleName());
        } catch (Exception e) {
            LOGGER.error("Failed to open screen: {}", screen.getClass().getSimpleName(), e);
        } finally {
            stackLock.unlock();
        }
    }

    public static void replaceScreen(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen cannot be null");
        }

        stackLock.lock();
        try {
            Minecraft mc = Minecraft.getInstance();

            if (!screenStack.isEmpty()) {
                screenStack.pop();
            }

            mc.setScreen(screen);
            screenStack.push(screen);
            LOGGER.debug("Replaced screen with: {}", screen.getClass().getSimpleName());
        } catch (Exception e) {
            LOGGER.error("Failed to replace screen: {}", screen.getClass().getSimpleName(), e);
        } finally {
            stackLock.unlock();
        }
    }

    public static void closeScreen() {
        stackLock.lock();
        try {
            if (screenStack.isEmpty()) {
                throw new IllegalStateException("Screen stack is empty");
            }

            Screen closed = screenStack.pop();
            LOGGER.debug("Closed screen: {}", closed.getClass().getSimpleName());

            Minecraft mc = Minecraft.getInstance();
            if (!screenStack.isEmpty()) {
                mc.setScreen(screenStack.peek());
            } else {
                mc.setScreen(null);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to close screen", e);
        } finally {
            stackLock.unlock();
        }
    }

    public static void clearScreens() {
        stackLock.lock();
        try {
            screenStack.clear();
            Minecraft.getInstance().setScreen(null);
            LOGGER.debug("Cleared all screens");
        } finally {
            stackLock.unlock();
        }
    }

    public static boolean containsScreen(Screen screen) {
        stackLock.lock();
        try {
            return screenStack.contains(screen);
        } finally {
            stackLock.unlock();
        }
    }

    @Nullable
    public static Screen getCurrentScreen() {
        stackLock.lock();
        try {
            return screenStack.peek();
        } finally {
            stackLock.unlock();
        }
    }

    public static int getStackDepth() {
        stackLock.lock();
        try {
            return screenStack.size();
        } finally {
            stackLock.unlock();
        }
    }

    public static void printStack() {
        stackLock.lock();
        try {
            LOGGER.debug("Screen stack ({}):", screenStack.size());
            screenStack.descendingIterator().forEachRemaining(screen ->
                    LOGGER.debug("  → {}", screen.getClass().getSimpleName())
            );
        } finally {
            stackLock.unlock();
        }
    }
}
