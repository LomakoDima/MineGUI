/* | В процессе разработки | MineGUICodeEditor |

package ru.dimalab.minegui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MineGUICodeEditor implements MineGUIWidget {
    private static final int LINE_NUMBER_MARGIN = 40;
    private static final int TEXT_MARGIN = 4;
    private static final int CURSOR_BLINK_INTERVAL = 15;
    private static final Map<String, TextColor> SYNTAX_COLORS = new HashMap<>();

    static {
        SYNTAX_COLORS.put("KEYWORD", TextColor.fromRgb(0xFFCC66));
        SYNTAX_COLORS.put("STRING", TextColor.fromRgb(0x99CC99));
        SYNTAX_COLORS.put("NUMBER", TextColor.fromRgb(0xF99157));
        SYNTAX_COLORS.put("COMMENT", TextColor.fromRgb(0x999999));
    }

    private final List<CodeLine> lines = new ArrayList<>();
    private int x;
    private int y;
    private int width;
    private int height;
    private int cursorX;
    private int cursorY;
    private boolean hasFocus;
    private int cursorBlink;
    private int scrollOffsetY;
    private int visibleLines;
    private final Font font;
    private final Pattern syntaxPattern;

    public MineGUICodeEditor(Font font, int x, int y, int width, int height) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visibleLines = height / font.lineHeight;
        lines.add(new CodeLine(""));

        String keywords = "public|private|protected|class|interface|void|int|float|double|boolean|return|if|else|for|while";
        this.syntaxPattern = Pattern.compile(
                "(?<COMMENT>//.*)|(?<STRING>\"([^\"\\\\]|\\\\.)*\")|(?<KEYWORD>" + keywords + ")|(?<NUMBER>\\d+\\.?\\d*)"
        );
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        renderBackground(graphics);
        renderLineNumbers(graphics);
        renderTextContent(graphics);
        renderCursor(graphics);
    }

    private void renderBackground(GuiGraphics graphics) {
        graphics.fill(x, y, x + width, y + height, 0xFF2D2D2D);
        graphics.fill(x + LINE_NUMBER_MARGIN, y, x + LINE_NUMBER_MARGIN + 1, y + height, 0xFF404040);
    }

    private void renderLineNumbers(GuiGraphics graphics) {
        int startLine = scrollOffsetY / font.lineHeight;
        int yPos = y + TEXT_MARGIN - (scrollOffsetY % font.lineHeight);

        for (int i = 0; i < visibleLines + 1; i++) {
            int lineNumber = startLine + i + 1;
            if (lineNumber > lines.size()) break;

            String number = String.format("%3d", lineNumber);
            graphics.drawString(font, number,
                    x + LINE_NUMBER_MARGIN - font.width(number) - TEXT_MARGIN,
                    yPos + i * font.lineHeight,
                    0xFF888888
            );
        }
    }

    private void renderTextContent(GuiGraphics graphics) {
        int startLine = scrollOffsetY / font.lineHeight;
        int yPos = y + TEXT_MARGIN - (scrollOffsetY % font.lineHeight);

        for (int i = 0; i < visibleLines + 1; i++) {
            int lineIndex = startLine + i;
            if (lineIndex >= lines.size()) break;

            CodeLine line = lines.get(lineIndex);
            int xPos = x + LINE_NUMBER_MARGIN + TEXT_MARGIN;
            for (CodeToken token : line.tokens) {
                graphics.drawString(font, token.text, xPos, yPos + i * font.lineHeight, token.color.getValue());
                xPos += font.width(token.text);
            }
        }
    }

    private void renderCursor(GuiGraphics graphics) {
        if (hasFocus && (cursorBlink / CURSOR_BLINK_INTERVAL) % 2 == 0) {
            CodeLine line = lines.get(cursorY);
            int textWidth = font.width(line.getText().substring(0, cursorX));
            int cursorPosX = x + LINE_NUMBER_MARGIN + TEXT_MARGIN + textWidth;
            int cursorPosY = y + TEXT_MARGIN + (cursorY * font.lineHeight) - scrollOffsetY;

            if (cursorPosY >= y && cursorPosY + font.lineHeight <= y + height) {
                graphics.fill(cursorPosX, cursorPosY, cursorPosX + 1, cursorPosY + font.lineHeight, 0xFFFFFFFF);
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            hasFocus = true;
            updateCursorPosition(mouseX, mouseY);
            return true;
        }
        hasFocus = false;
        return false;
    }

    private boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private void updateCursorPosition(int mouseX, int mouseY) {
        int textAreaX = mouseX - (x + LINE_NUMBER_MARGIN + TEXT_MARGIN);
        int textAreaY = mouseY - y + scrollOffsetY - TEXT_MARGIN;

        cursorY = Mth.clamp(textAreaY / font.lineHeight, 0, lines.size() - 1);
        CodeLine line = lines.get(cursorY);
        cursorX = font.plainSubstrByWidth(line.getText(), textAreaX).length();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!hasFocus) return false;

        switch (keyCode) {
            case 264: cursorY = Math.min(lines.size() - 1, cursorY + 1); break;
            case 265: cursorY = Math.max(0, cursorY - 1); break;
            case 263: cursorX = Math.max(0, cursorX - 1); break;
            case 262: cursorX = Math.min(lines.get(cursorY).getText().length(), cursorX + 1); break;
            case 257: splitLine(); break;
            case 259: deleteChar(); break;
        }
        clampCursor();
        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!hasFocus) return false;
        insertChar(codePoint);
        return true;
    }

    private void insertChar(char c) {
        CodeLine line = lines.get(cursorY);
        line.setText(line.getText().substring(0, cursorX) + c + line.getText().substring(cursorX));
        cursorX++;
        processSyntaxHighlighting(cursorY);
    }

    private void deleteChar() {
        CodeLine line = lines.get(cursorY);
        if (cursorX > 0) {
            line.setText(line.getText().substring(0, cursorX - 1) + line.getText().substring(cursorX));
            cursorX--;
        } else if (cursorY > 0) {
            CodeLine prevLine = lines.get(cursorY - 1);
            prevLine.setText(prevLine.getText() + line.getText());
            lines.remove(cursorY);
            cursorY--;
            cursorX = prevLine.getText().length();
        }
        processSyntaxHighlighting(cursorY);
    }

    private void splitLine() {
        CodeLine current = lines.get(cursorY);
        String newLineText = current.getText().substring(cursorX);
        current.setText(current.getText().substring(0, cursorX));
        lines.add(cursorY + 1, new CodeLine(newLineText));
        cursorY++;
        cursorX = 0;
        processSyntaxHighlighting(cursorY - 1);
        processSyntaxHighlighting(cursorY);
    }

    private void processSyntaxHighlighting(int lineIndex) {
        CodeLine line = lines.get(lineIndex);
        line.tokens.clear();

        Matcher matcher = syntaxPattern.matcher(line.getText());
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                addDefaultToken(line, line.getText().substring(lastEnd, matcher.start()));
            }

            for (String group : SYNTAX_COLORS.keySet()) {
                if (matcher.group(group) != null) {
                    line.tokens.add(new CodeToken(matcher.group(group), SYNTAX_COLORS.get(group)));
                    lastEnd = matcher.end();
                    break;
                }
            }
        }

        if (lastEnd < line.getText().length()) {
            addDefaultToken(line, line.getText().substring(lastEnd));
        }
    }

    private void addDefaultToken(CodeLine line, String text) {
        line.tokens.add(new CodeToken(text, TextColor.fromRgb(0xFFFFFF)));
    }

    private void clampCursor() {
        cursorY = Mth.clamp(cursorY, 0, lines.size() - 1);
        cursorX = Mth.clamp(cursorX, 0, lines.get(cursorY).getText().length());
    }

    @Override
    public void tick() {
        cursorBlink = (cursorBlink + 1) % (CURSOR_BLINK_INTERVAL * 2);
        updateScroll();
    }

    private void updateScroll() {
        int cursorScreenY = cursorY * font.lineHeight - scrollOffsetY;
        if (cursorScreenY < 0) {
            scrollOffsetY += cursorScreenY;
        } else if (cursorScreenY + font.lineHeight > height) {
            scrollOffsetY += cursorScreenY + font.lineHeight - height;
        }
        scrollOffsetY = Mth.clamp(scrollOffsetY, 0, Math.max(0, lines.size() * font.lineHeight - height));
    }

    // Inner classes
    private static class CodeLine {
        private String text;
        private final List<CodeToken> tokens = new ArrayList<>();

        public CodeLine(String text) {
            this.text = text;
        }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    private static class CodeToken {
        public final String text;
        public final TextColor color;

        public CodeToken(String text, TextColor color) {
            this.text = text;
            this.color = color;
        }
    }

    // Getters/Setters
    public List<String> getText() {
        List<String> result = new ArrayList<>();
        for (CodeLine line : lines) result.add(line.getText());
        return result;
    }

    public void setText(List<String> text) {
        lines.clear();
        for (String line : text) {
            CodeLine codeLine = new CodeLine(line);
            lines.add(codeLine);
            processSyntaxHighlighting(lines.size() - 1);
        }
        clampCursor();
    }

    // Positionable/Resizable implementation
    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public void setPosition(int x, int y) { this.x = x; this.y = y; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
}

 */