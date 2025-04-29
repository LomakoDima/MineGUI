package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.function.Consumer;

public class MineGUIAccordionSection {
    private final String title;
    private final int x, width, headerHeight;

    private boolean expanded = false;
    private int contentHeight = 0;
    private Consumer<GuiGraphics> contentRenderer;

    private int backgroundColor = FastColor.ARGB32.color(255, 60, 60, 60);
    private int headerColor = FastColor.ARGB32.color(255, 80, 80, 80);
    private int activeHeaderColor = FastColor.ARGB32.color(255, 0, 120, 255);
    private int textColor = 0xFFFFFF;

    public MineGUIAccordionSection(String title, int x, int width, int headerHeight) {
        this.title = title;
        this.x = x;
        this.width = width;
        this.headerHeight = headerHeight;
    }

    public void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }

    public void setContentRenderer(Consumer<GuiGraphics> renderer) {
        this.contentRenderer = renderer;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getFullHeight() {
        return headerHeight + (expanded ? contentHeight : 0);
    }

    public void render(GuiGraphics graphics, Font font, int y) {
        int currentHeaderColor = expanded ? activeHeaderColor : headerColor;

        graphics.fill(x, y, x + width, y + headerHeight, currentHeaderColor);
        graphics.renderOutline(x, y, width, headerHeight, FastColor.ARGB32.color(255, 100, 100, 100));


        graphics.drawString(font, Component.literal(title), x + 5, y + (headerHeight - font.lineHeight) / 2, textColor, false);


        String arrow = expanded ? "▼" : "►";
        int arrowWidth = font.width(arrow);
        graphics.drawString(font, Component.literal(arrow), x + width - arrowWidth - 8, y + (headerHeight - font.lineHeight) / 2, textColor, false);

        if (expanded && contentRenderer != null) {

            graphics.fill(x, y + headerHeight, x + width, y + headerHeight + contentHeight, backgroundColor);
            graphics.renderOutline(x, y + headerHeight, width, contentHeight, FastColor.ARGB32.color(255, 80, 80, 80));

            contentRenderer.accept(graphics);
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int y) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + headerHeight;
    }
}
