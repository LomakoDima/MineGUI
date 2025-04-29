package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FastColor;
import net.minecraftforge.event.RegisterCommandsEvent;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class MineGUIMultilineTextField implements MineGUIWidget {
    private final int x, y, width, height;
    private List<String> lines = new ArrayList<>();
    private List<Integer> lineStartIndices = new ArrayList<>();
    private int maxLines;
    private String text = "";
    private String placeholder;
    private boolean focused = false;
    private int textColor = 0xFFFFFF;
    private int placeholderColor = 0x888888;
    private int backgroundColor = FastColor.ARGB32.color(255, 40, 40, 40);
    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int borderColorFocused = FastColor.ARGB32.color(255, 0, 120, 255);
    private int cursorPosition = 0;
    private long lastBlinkTime = System.currentTimeMillis();
    private boolean cursorVisible = true;
    private int selectionStart = -1;
    private int selectionEnd = -1;
    private boolean hasSelectionFlag = false;



    public MineGUIMultilineTextField(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.placeholder = builder.placeholder;
        this.maxLines = builder.maxLines;
        updateLineData();
    }

    private void updateLineData() {
        Font font = net.minecraft.client.Minecraft.getInstance().font;
        lines.clear();
        lineStartIndices.clear();

        int currentIndex = 0;
        lineStartIndices.add(currentIndex);

        if (text.isEmpty()) {
            lines.add("");
            lineStartIndices.add(0);
        } else {
            String[] textLines = text.split("\\n", -1);

            for (String line : textLines) {
                List<FormattedText> wrappedLines = font.getSplitter().splitLines(Component.literal(line), width - 8, Component.literal(line).getStyle());
                for (FormattedText wrappedLine : wrappedLines) {
                    String wrappedString = wrappedLine.getString();
                    lines.add(wrappedString);
                    lineStartIndices.add(currentIndex);
                    currentIndex += wrappedString.length();
                }
                currentIndex += 1;
            }

            if (!text.endsWith("\n") && !textLines[textLines.length - 1].isEmpty()) {
                currentIndex--;
            }

            lineStartIndices.set(lineStartIndices.size() - 1, currentIndex);
        }

        if (lines.size() > maxLines) {
            int truncateIndex = lineStartIndices.get(maxLines);
            truncateIndex = Math.min(truncateIndex, text.length());
            String truncatedText = text.substring(0, truncateIndex);

            lines.clear();
            lineStartIndices.clear();
            currentIndex = 0;
            lineStartIndices.add(currentIndex);

            if (truncatedText.isEmpty()) {
                lines.add("");
                lineStartIndices.add(0);
            } else {
                String[] newTextLines = truncatedText.split("\\n", -1);

                for (String line : newTextLines) {
                    List<FormattedText> wrappedLines = font.getSplitter().splitLines(Component.literal(line), width - 8, Component.literal(line).getStyle());
                    for (FormattedText wrappedLine : wrappedLines) {
                        String wrappedString = wrappedLine.getString();
                        lines.add(wrappedString);
                        lineStartIndices.add(currentIndex);
                        currentIndex += wrappedString.length();
                    }
                    currentIndex += 1;
                }

                if (!truncatedText.endsWith("\n") && !newTextLines[newTextLines.length - 1].isEmpty()) {
                    currentIndex--;
                }

                lineStartIndices.set(lineStartIndices.size() - 1, currentIndex);
            }

            if (lines.size() > maxLines) {
                lines = lines.subList(0, maxLines);
                lineStartIndices = new ArrayList<>(lineStartIndices.subList(0, maxLines + 1));
            }

            text = truncatedText;
        }

        fixSelection();
    }

    private void fixSelection() {
        int textLength = text.length();
        selectionStart = Math.min(selectionStart, textLength);
        selectionEnd = Math.min(selectionEnd, textLength);
        cursorPosition = Math.min(cursorPosition, textLength);
        if (selectionStart == selectionEnd) {
            clearSelection();
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.fill(x, y, x + width, y + height, backgroundColor);
        graphics.renderOutline(x, y, width, height, focused ? borderColorFocused : borderColor);

        int textX = x + 4;
        int textY = y + 4;

        if (text.isEmpty() && !focused) {
            graphics.drawString(font, Component.literal(placeholder), textX, textY, placeholderColor, false);
        } else {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                graphics.drawString(font, Component.literal(line), textX, textY + i * font.lineHeight, textColor, false);
            }
        }

        if (hasSelection()) {
            renderTextSelection(graphics, font, textX, textY);
        }

        if (focused) {
            updateCursorBlinking();
            if (cursorVisible) {
                int line = getCursorLine();
                int lineStart = lineStartIndices.get(line);
                int posInLine = cursorPosition - lineStart;
                String lineText = lines.size() > line ? lines.get(line) : "";
                posInLine = Math.min(posInLine, lineText.length());

                int cursorX = textX + font.width(lineText.substring(0, posInLine));
                int cursorY = textY + line * font.lineHeight;
                graphics.fill(cursorX, cursorY, cursorX + 1, cursorY + font.lineHeight, 0xFFFFFFFF);
            }
        }
    }

    private void renderTextSelection(GuiGraphics graphics, Font font, int textX, int textY) {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        for (int line = 0; line < lines.size(); line++) {
            int lineStart = lineStartIndices.get(line);
            int lineEnd = line < lines.size() - 1 ? lineStartIndices.get(line + 1) : text.length();

            if (end < lineStart || start > lineEnd) continue;

            int selectStart = Math.max(start, lineStart);
            int selectEnd = Math.min(end, lineEnd);

            String lineText = lines.get(line);
            String before = lineText.substring(0, selectStart - lineStart);
            String selected = lineText.substring(selectStart - lineStart, selectEnd - lineStart);

            int x1 = textX + font.width(before);
            int x2 = textX + font.width(before + selected);
            int y1 = textY + line * font.lineHeight;
            int y2 = y1 + font.lineHeight;

            graphics.fill(x1, y1, x2, y2, 0x8033B5E5);
        }
    }

    private void updateCursorBlinking() {
        long now = System.currentTimeMillis();
        if (now - lastBlinkTime > 500) {
            cursorVisible = !cursorVisible;
            lastBlinkTime = now;
        }
    }

    private int getCursorLine() {
        for (int i = 0; i < lineStartIndices.size() - 1; i++) {
            if (cursorPosition <= lineStartIndices.get(i + 1)) {
                return i;
            }
        }
        return lines.size() - 1;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            focused = true;
            Font font = net.minecraft.client.Minecraft.getInstance().font;
            int clickY = mouseY - y - 4;
            int line = Math.min(clickY / font.lineHeight, lines.size() - 1);
            line = Math.max(line, 0);

            String lineText = lines.get(line);
            int clickX = mouseX - x - 4;
            int posInLine = font.plainSubstrByWidth(lineText, clickX).length();

            int previousCursorPosition = cursorPosition;
            cursorPosition = Math.min(lineStartIndices.get(line) + posInLine, text.length());

            boolean shift = Screen.hasShiftDown();

            if (shift) {
                if (!hasSelectionFlag) {
                    selectionStart = previousCursorPosition;
                    hasSelectionFlag = true;
                }
                selectionEnd = cursorPosition;
            } else {
                clearSelection();
            }

            resetCursorBlink();
            return true;
        }
        focused = false;
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!focused) return false;

        if (hasSelection()) {
            deleteSelection();
        }

        text = text.substring(0, cursorPosition) + codePoint + text.substring(cursorPosition);
        cursorPosition++;
        updateLineData();
        resetCursorBlink();
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        boolean shift = Screen.hasShiftDown();
        boolean ctrl = Screen.hasControlDown();

        switch (keyCode) {
            case 259:
                if (hasSelection()) {
                    deleteSelection();
                } else if (cursorPosition > 0) {
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                    cursorPosition--;
                }
                updateLineData();
                resetCursorBlink();
                return true;

            case 263:
                moveCursor(-1, shift, ctrl);
                return true;

            case 262:
                moveCursor(1, shift, ctrl);
                return true;

            case 257:
                insertNewLine();
                return true;

            case 65:
                if (ctrl) {
                    selectionStart = 0;
                    selectionEnd = text.length();
                    cursorPosition = text.length();
                    hasSelectionFlag = true;
                    return true;
                }
                break;

            case 67:
                if (ctrl) {
                    copy();
                    return true;
                }
                break;

            case 86:
                if (ctrl) {
                    paste();
                    return true;
                }
                break;
        }
        return false;
    }

    private void insertNewLine() {
        if (hasSelection()) {
            deleteSelection();
        }
        text = text.substring(0, cursorPosition) + "\n" + text.substring(cursorPosition);
        cursorPosition++;
        updateLineData();
        resetCursorBlink();
    }

    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private void moveCursor(int direction, boolean shift, boolean ctrl) {
        int newPosition = cursorPosition;

        if (ctrl) {
            if (direction < 0) {
                while (newPosition > 0 && Character.isWhitespace(text.charAt(newPosition - 1))) {
                    newPosition--;
                }
                while (newPosition > 0 && !Character.isWhitespace(text.charAt(newPosition - 1))) {
                    newPosition--;
                }
            } else {
                while (newPosition < text.length() && Character.isWhitespace(text.charAt(newPosition))) {
                    newPosition++;
                }
                while (newPosition < text.length() && !Character.isWhitespace(text.charAt(newPosition))) {
                    newPosition++;
                }
            }
        } else {
            newPosition = Math.max(0, Math.min(text.length(), cursorPosition + direction));
        }

        if (shift) {
            if (!hasSelectionFlag) {
                selectionStart = cursorPosition;
                hasSelectionFlag = true;
            }
            selectionEnd = newPosition;
        } else {
            clearSelection();
        }

        cursorPosition = newPosition;
        resetCursorBlink();
    }

    private void deleteSelection() {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);
        text = text.substring(0, start) + text.substring(end);
        cursorPosition = start;
        clearSelection();
        updateLineData();
    }

    private void resetCursorBlink() {
        cursorVisible = true;
        lastBlinkTime = System.currentTimeMillis();
    }

    private boolean hasSelection() {
        return selectionStart != -1 && selectionEnd != -1 && selectionStart != selectionEnd;
    }

    private void clearSelection() {
        selectionStart = -1;
        selectionEnd = -1;
        hasSelectionFlag = false;
    }

    private void copy() {
        if (hasSelection()) {
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            String selectedText = text.substring(start, end);

            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(selectedText), null);
            } catch (Exception e) {
                net.minecraft.client.Minecraft.getInstance().keyboardHandler.setClipboard(selectedText);
            }
        }
    }

    private void paste() {
        try {
            String clipboardText;
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(null);
                clipboardText = (String) contents.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
            } else {
                clipboardText = net.minecraft.client.Minecraft.getInstance().keyboardHandler.getClipboard();
            }

            if (clipboardText != null && !clipboardText.isEmpty()) {
                clipboardText = clipboardText.replace("\r", "");
                if (hasSelection()) {
                    deleteSelection();
                }
                text = text.substring(0, cursorPosition) + clipboardText + text.substring(cursorPosition);
                cursorPosition += clipboardText.length();
                updateLineData();
                resetCursorBlink();
            }
        } catch (Exception e) {
            // Handle exception
        }
    }

    public static class Builder {
        private int x, y, width, height;
        private String placeholder = "";
        private int maxLines = 10;

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public MineGUIMultilineTextField build() {
            return new MineGUIMultilineTextField(this);
        }
    }
}
