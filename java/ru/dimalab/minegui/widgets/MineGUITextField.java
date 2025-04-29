package ru.dimalab.minegui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.function.Consumer;

public class MineGUITextField implements MineGUIWidget {
    private final int x, y, width, height;

    private String text = "";
    private String placeholder = "Введите текст";
    private int maxLength = 50;

    private boolean focused = false;
    private int cursorPosition = 0;
    private long lastBlinkTime = System.currentTimeMillis();
    private boolean cursorVisible = true;
    private int selectionStart = -1;
    private int selectionEnd = -1;
    private boolean hasSelectionFlag = false;

    private int textColor = 0xFFFFFF;
    private int placeholderColor = 0x888888;
    private int backgroundColor = FastColor.ARGB32.color(255, 40, 40, 40);
    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int borderColorFocused = FastColor.ARGB32.color(255, 0, 120, 255);

    private int scrollbarHeight = 3;
    private int scrollbarTrackColor = FastColor.ARGB32.color(255, 60, 60, 60); // Цвет фона скроллбара
    private int scrollbarThumbColor = FastColor.ARGB32.color(255, 120, 120, 120); // Цвет ползунка скроллбара
    private int minThumbWidth = 8;

    private int scrollOffset = 0;
    private int maxScrollOffset = 0;

    private Consumer<String> onTextChanged;

    public MineGUITextField(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.placeholder = builder.placeholder;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setText(String newText) {
        if (newText == null) newText = "";
        if (maxLength > 0 && newText.length() > maxLength) {
            text = newText.substring(0, maxLength);
        } else {
            text = newText;
        }
        cursorPosition = text.length();
        clearSelection();
        updateScrollOffset();
        triggerChange();
    }

    public String getText() {
        return text;
    }

    public void setOnTextChanged(Consumer<String> onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    private void triggerChange() {
        if (onTextChanged != null) {
            onTextChanged.accept(text);
        }
    }

    public void setFocused(boolean focus) {
        if (focused != focus) {
            focused = focus;
            cursorVisible = true;
            lastBlinkTime = System.currentTimeMillis();
            if (!focused) clearSelection();
        }
    }

    public boolean isFocused() {
        return focused;
    }

    private void updateScrollOffset() {
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        int innerWidth = width - 8;
        maxScrollOffset = Math.max(0, textWidth - innerWidth);
        scrollOffset = Math.min(scrollOffset, maxScrollOffset);
        scrollOffset = Math.max(scrollOffset, 0);
    }

    @Override
    public void tick() {
        if (focused) {
            long now = System.currentTimeMillis();
            if (now - lastBlinkTime > 500) {
                cursorVisible = !cursorVisible;
                lastBlinkTime = now;
            }
        } else {
            cursorVisible = false;
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int border = focused ? borderColorFocused : borderColor;

        updateScrollOffset();

        int textWidth = font.width(text);
        maxScrollOffset = Math.max(0, textWidth - (width - 8));
        scrollOffset = Math.min(scrollOffset, maxScrollOffset);
        scrollOffset = Math.max(scrollOffset, 0);

        graphics.fill(x, y, x + width, y + height, backgroundColor);
        graphics.renderOutline(x, y, width, height, border);

        if (maxScrollOffset > 0) {

            int trackX = x + 1;
            int trackY = y + height - scrollbarHeight - 1;
            int trackWidth = width - 2;


            graphics.fill(trackX, trackY, trackX + trackWidth, trackY + scrollbarHeight, scrollbarTrackColor);

            int innerWidth = width - 8;
            int totalTextWidth = font.width(text);

            int thumbWidth = (int) ((double) trackWidth * innerWidth / totalTextWidth);
            thumbWidth = Math.max(minThumbWidth, thumbWidth);
            thumbWidth = Math.min(trackWidth, thumbWidth);

            double scrollRatio = (maxScrollOffset == 0) ? 0.0 : (double) scrollOffset / maxScrollOffset;
            int thumbX = trackX + (int) ((trackWidth - thumbWidth) * scrollRatio);

            graphics.fill(thumbX, trackY, thumbX + thumbWidth, trackY + scrollbarHeight, scrollbarThumbColor);
        }

        int clipY1 = y + 2;
        int clipY2 = y + height - (maxScrollOffset > 0 ? scrollbarHeight + 2 : 2);
        if (clipY2 > clipY1) {
            graphics.enableScissor(x + 2, clipY1, x + width - 2, clipY2);
        }


        int textX = x + 4 - scrollOffset;
        int textY = y + (height - (maxScrollOffset > 0 ? scrollbarHeight : 0) - font.lineHeight) / 2;

        String displayedText = text.isEmpty() && !focused ? placeholder : text;
        int color = text.isEmpty() && !focused ? placeholderColor : textColor;

        graphics.drawString(font, Component.literal(displayedText), textX, textY, color, false);

        if (hasSelection()) {
            renderTextSelection(graphics, font, textX, textY);
        }

        if (focused && cursorVisible) {
            int cursorX = textX + font.width(text.substring(0, cursorPosition));
            graphics.fill(cursorX, textY - 1, cursorX + 1, textY + font.lineHeight + 1, 0xFFFFFFFF);
        }

        if (clipY2 > clipY1) {
            graphics.disableScissor();
        }
    }

    private void renderTextSelection(GuiGraphics graphics, Font font, int textX, int textY) {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);

        String visibleText = font.plainSubstrByWidth(text, width - 8);
        int visibleStart = Math.min(start, visibleText.length());
        int visibleEnd = Math.min(end, visibleText.length());

        String before = visibleText.substring(0, visibleStart);
        String selected = visibleText.substring(visibleStart, visibleEnd);

        int x1 = textX + font.width(before);
        int x2 = x1 + font.width(selected);
        graphics.fill(x1, textY - 1, x2, textY + font.lineHeight + 1, 0x8033B5E5);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isInside(mouseX, mouseY)) {
            if (maxScrollOffset > 0) {
                int trackY = y + height - scrollbarHeight - 1;
                if (mouseY >= trackY && mouseY <= trackY + scrollbarHeight) {
                    int trackX = x + 1;
                    int trackWidth = width - 2;
                    int innerWidth = width - 8;
                    int totalTextWidth = Minecraft.getInstance().font.width(text);
                    int thumbWidth = Math.max(minThumbWidth, (int) ((double) trackWidth * innerWidth / totalTextWidth));
                    thumbWidth = Math.min(trackWidth, thumbWidth);
                    double scrollRatio = (maxScrollOffset == 0) ? 0.0 : (double) scrollOffset / maxScrollOffset;
                    int thumbX = trackX + (int) ((trackWidth - thumbWidth) * scrollRatio);

                    if (mouseX < thumbX) { // Клик левее ползунка
                        scrollOffset = Math.max(0, scrollOffset - innerWidth);
                    } else if (mouseX > thumbX + thumbWidth) { // Клик правее ползунка
                        scrollOffset = Math.min(maxScrollOffset, scrollOffset + innerWidth);
                    }
                    resetCursorBlink();
                    setFocused(true);
                    return true;
                }
            }
            focused = true;
            Font font = net.minecraft.client.Minecraft.getInstance().font;
            int clickX = mouseX - x - 4;
            int newPosition = font.plainSubstrByWidth(text, clickX).length();

            boolean shift = Screen.hasShiftDown();
            int previousPosition = cursorPosition;

            cursorPosition = Math.min(newPosition, text.length());

            if (shift) {
                if (!hasSelectionFlag) {
                    selectionStart = previousPosition;
                    hasSelectionFlag = true;
                }
                selectionEnd = cursorPosition;
            } else {
                clearSelection();
            }

            resetCursorBlink();
            scrollToCursor();
            return true;
        }
        focused = false;
        return false;
    }

    private void scrollToCursor() {
        Font font = Minecraft.getInstance().font;
        int cursorXOffset = font.width(text.substring(0, cursorPosition)); // Смещение курсора от начала текста
        int visibleWidth = width - 8;

        if (cursorXOffset < scrollOffset) {
            scrollOffset = cursorXOffset;
        }

        else if (cursorXOffset > scrollOffset + visibleWidth) {
            scrollOffset = cursorXOffset - visibleWidth;
        }

        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScrollOffset));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (focused && maxScrollOffset > 0 && isInside((int)mouseX, (int)mouseY)) {
            scrollOffset = (int) Math.max(0, Math.min(maxScrollOffset, scrollOffset - deltaY * 15));
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!focused) return false;

        if (hasSelection()) {
            deleteSelection();
        }

        if (maxLength > 0 && text.length() >= maxLength) return false;

        text = text.substring(0, cursorPosition) + codePoint + text.substring(cursorPosition);
        cursorPosition++;
        triggerChange();
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
                triggerChange();
                resetCursorBlink();
                return true;

            case 263:
                moveCursor(-1, shift, ctrl);
                return true;

            case 262:
                moveCursor(1, shift, ctrl);
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
                if (ctrl) copy();
                return true;

            case 86:
                if (ctrl) paste();
                return true;
        }
        return false;
    }

    private void moveCursor(int direction, boolean shift, boolean ctrl) {
        int newPosition = cursorPosition;

        if (ctrl) {
            if (direction < 0) {
                while (newPosition > 0 && Character.isWhitespace(text.charAt(newPosition - 1))) newPosition--;
                while (newPosition > 0 && !Character.isWhitespace(text.charAt(newPosition - 1))) newPosition--;
            } else {
                while (newPosition < text.length() && Character.isWhitespace(text.charAt(newPosition))) newPosition++;
                while (newPosition < text.length() && !Character.isWhitespace(text.charAt(newPosition))) newPosition++;
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

        Font font = Minecraft.getInstance().font;
        int cursorWidth = font.width(text.substring(0, cursorPosition));
        int visibleWidth = width - 8;

        if (cursorWidth - scrollOffset > visibleWidth) {
            scrollOffset = cursorWidth - visibleWidth;
        } else if (cursorWidth - scrollOffset < 0) {
            scrollOffset = cursorWidth;
        }
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScrollOffset));
    }

    private void deleteSelection() {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);
        text = text.substring(0, start) + text.substring(end);
        cursorPosition = start;
        clearSelection();
        triggerChange();
    }

    private void copy() {
        if (hasSelection()) {
            String selected = text.substring(Math.min(selectionStart, selectionEnd), Math.max(selectionStart, selectionEnd));
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(selected), null);
            } catch (Exception e) {
                net.minecraft.client.Minecraft.getInstance().keyboardHandler.setClipboard(selected);
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

            if (clipboardText != null) {
                clipboardText = clipboardText.replace("\r", "").split("\n")[0];
                if (maxLength > 0 && text.length() + clipboardText.length() > maxLength) {
                    int available = maxLength - text.length();
                    clipboardText = clipboardText.substring(0, available);
                }
                if (hasSelection()) deleteSelection();
                text = text.substring(0, cursorPosition) + clipboardText + text.substring(cursorPosition);
                cursorPosition += clipboardText.length();
                triggerChange();
            }
        } catch (Exception ignored) {}
    }

    private boolean hasSelection() {
        return selectionStart != -1 && selectionEnd != -1 && selectionStart != selectionEnd;
    }

    private void clearSelection() {
        selectionStart = -1;
        selectionEnd = -1;
        hasSelectionFlag = false;
    }

    private void resetCursorBlink() {
        cursorVisible = true;
        lastBlinkTime = System.currentTimeMillis();
    }

    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static class Builder {
        private int x, y, width, height;
        private String placeholder = "Введите текст";
        private int maxLength = 50;

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

        public Builder setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public MineGUITextField build() {
            return new MineGUITextField(this);
        }
    }
}


