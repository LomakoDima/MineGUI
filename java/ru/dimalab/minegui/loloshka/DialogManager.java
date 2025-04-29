package ru.dimalab.minegui.loloshka;

import ru.dimalab.minegui.MineGUI;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DialogManager {
    private static final Queue<Dialog> dialogQueue = new LinkedList<>();
    private static Dialog currentDialog;
    private static long displayStartTime;

    public static void showDialog(Dialog dialog) {
        dialogQueue.add(dialog);
        if (currentDialog == null) {
            nextDialog();
        }
    }

    public static void update() {
        if (currentDialog != null &&
                System.currentTimeMillis() - displayStartTime > currentDialog.getDisplayTime()) {
            nextDialog();
        }
    }

    private static void nextDialog() {
        currentDialog = dialogQueue.poll();
        if (currentDialog != null) {
            displayStartTime = System.currentTimeMillis();
        }
    }

    public static Dialog getCurrentDialog() {
        return currentDialog;
    }

    public static List<Dialog> getAllDialogs() {
        List<Dialog> allDialogs = new LinkedList<>();
        if (currentDialog != null) {
            allDialogs.add(currentDialog);
        }
        allDialogs.addAll(dialogQueue);
        MineGUI.LOGGER.debug("All dialogs requested (total: {})", allDialogs.size());
        return allDialogs;
    }
}
