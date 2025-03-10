package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.function.Consumer;

public class MineGUITextField {
    private final int x, y, width, height;

    private String text = "";
    private String placeholder = "Введите текст";

    private int maxLength = 50;

    private boolean focused = false;
    private int cursorTickCounter = 0;
    private boolean cursorVisible = true;

    private int textColor = 0xFFFFFF;
    private int placeholderColor = 0x888888;
    private int backgroundColor = FastColor.ARGB32.color(255, 40, 40, 40);
    private int borderColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int borderColorFocused = FastColor.ARGB32.color(255, 0, 120, 255);

    private Consumer<String> onTextChanged;

    public MineGUITextField(int x, int y, int width, int height, String placeholder) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
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
            cursorTickCounter = 0;
            cursorVisible = true;
        }
    }

    public boolean isFocused() {
        return focused;
    }


    public void tick() {
        if (focused) {
            cursorTickCounter++;
            if (cursorTickCounter >= 15) { // чем меньше, тем быстрее мигает
                cursorTickCounter = 0;
                cursorVisible = !cursorVisible;
            }
        } else {
            cursorVisible = false;
        }
    }


    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int border = focused ? borderColorFocused : borderColor;


        graphics.fill(x, y, x + width, y + height, backgroundColor);
        graphics.renderOutline(x, y, width, height, border);

        int textX = x + 4;
        int textY = y + (height - font.lineHeight) / 2;

        String displayedText = text.isEmpty() && !focused ? placeholder : text;
        int color = text.isEmpty() && !focused ? placeholderColor : textColor;

        graphics.drawString(font, Component.literal(displayedText), textX, textY, color, false);


        if (focused && cursorVisible) {
            int cursorX = textX + font.width(text);
            int cursorTop = textY - 1;
            int cursorBottom = textY + font.lineHeight + 1;

            graphics.fill(cursorX, cursorTop, cursorX + 1, cursorBottom, textColor);
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        boolean wasFocused = focused;
        focused = isInside(mouseX, mouseY);

        if (focused && !wasFocused && text.isEmpty()) {
            // Можно добавить очистку placeholder при фокусе, если хочешь
        }

        return focused;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (!focused) return false;

        if (maxLength > 0 && text.length() >= maxLength) return false;

        if (isAllowedCharacter(codePoint)) {
            text += codePoint;
            triggerChange();
            return true;
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        switch (keyCode) {
            case 259: // Backspace
                if (!text.isEmpty()) {
                    text = text.substring(0, text.length() - 1);
                    triggerChange();
                }
                return true;

            case 257: // Enter
            case 335: // Numpad Enter
            case 256: // Escape
                focused = false;
                return true;

            default:
                return false;
        }
    }

    private boolean isInside(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private boolean isAllowedCharacter(char c) {
        return String.valueOf(c).matches("[\\p{L}\\p{N}\\p{P}\\p{Z}]");
    }
}
