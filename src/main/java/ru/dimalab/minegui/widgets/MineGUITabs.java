package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MineGUITabs {
    private final int x, y;
    private final int width;
    private final int tabHeight;
    private final int tabWidth;

    private final List<MineGUITab> tabs = new ArrayList<>();
    private int selectedIndex = 0;

    private Consumer<MineGUITab> onTabSelected;

    public MineGUITabs(int x, int y, int width, int tabHeight, int tabWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.tabHeight = tabHeight;
        this.tabWidth = tabWidth;
    }

    public void addTab(String label) {
        MineGUITab tab = new MineGUITab(label, tabs.size());
        tabs.add(tab);
    }

    public void setOnTabSelected(Consumer<MineGUITab> onTabSelected) {
        this.onTabSelected = onTabSelected;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedLabel() {
        return tabs.get(selectedIndex).getLabel();
    }

    public void render(GuiGraphics graphics, Font font) {
        int currentX = x;

        for (MineGUITab tab : tabs) {
            boolean isActive = (tab.getIndex() == selectedIndex);
            tab.render(graphics, font, currentX, y, tabWidth, tabHeight, isActive);
            currentX += tabWidth + 4; // Отступ между вкладками
        }
    }

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
}
