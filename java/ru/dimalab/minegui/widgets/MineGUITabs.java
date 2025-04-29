package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MineGUITabs implements MineGUIWidget {
    private final int x, y;
    private final int width;
    private final int tabHeight;
    private final int tabWidth;
    private final List<MineGUITab> tabs;
    private int selectedIndex = 0;
    private Consumer<MineGUITab> onTabSelected;

    public MineGUITabs(int x, int y, int width, int tabHeight, int tabWidth, List<MineGUITab> tabs, Consumer<MineGUITab> onTabSelected) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.tabHeight = tabHeight;
        this.tabWidth = tabWidth;
        this.tabs = tabs;
        this.onTabSelected = onTabSelected;
    }

    public static class Builder {
        private int x, y, width, tabHeight, tabWidth;
        private final List<MineGUITab> tabs = new ArrayList<>();
        private Consumer<MineGUITab> onTabSelected;

        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setSize(int width, int tabHeight, int tabWidth) {
            this.width = width;
            this.tabHeight = tabHeight;
            this.tabWidth = tabWidth;
            return this;
        }

        public Builder addTab(String label, int index, MineGUIColorPalette background, MineGUIColorPalette active, MineGUIColorPalette text) {
            tabs.add(new MineGUITab(label, index, background, active, text));
            return this;
        }

        public Builder setOnTabSelected(Consumer<MineGUITab> onTabSelected) {
            this.onTabSelected = onTabSelected;
            return this;
        }

        public MineGUITabs build() {
            return new MineGUITabs(x, y, width, tabHeight, tabWidth, tabs, onTabSelected);
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int currentX = x;
        for (MineGUITab tab : tabs) {
            boolean isActive = (tab.getIndex() == selectedIndex);
            tab.render1(graphics, font, currentX, y, tabWidth, tabHeight, isActive);
            currentX += tabWidth + 4;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        for (MineGUITab tab : tabs) {
            if (tab.mouseClicked(mouseX, mouseY, button)) {
                selectedIndex = tab.getIndex();
                if (onTabSelected != null) {
                    onTabSelected.accept(tab);
                }
                return true;
            }
        }
        return false;
    }

    public static class MineGUITab implements MineGUIWidget {
        private final String label;
        private final int index;
        private boolean active = false;
        private int x, y, width, height;
        private int backgroundColor;
        private int activeColor;
        private int textColor;

        public MineGUITab(String label, int index, MineGUIColorPalette background, MineGUIColorPalette active, MineGUIColorPalette text) {
            this.label = label;
            this.index = index;
            this.backgroundColor = background.getColor();
            this.activeColor = active.getColor();
            this.textColor = text.getColor();
        }

        @Override
        public void render1(GuiGraphics graphics, Font font, int x, int y, int width, int height, boolean active) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.active = active;
            int color = active ? activeColor : backgroundColor;
            graphics.fill(x, y, x + width, y + height, color);
            int textX = x + (width - font.width(label)) / 2;
            int textY = y + (height - font.lineHeight) / 2;
            graphics.drawString(font, Component.literal(label), textX, textY, textColor, false);
        }

        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

        public String getLabel() {
            return label;
        }

        public int getIndex() {
            return index;
        }
    }
}
