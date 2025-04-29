package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class MineGUIAccordion {
    private final int x, y, width;
    private final List<MineGUIAccordionSection> sections = new ArrayList<>();
    private boolean singleOpen = true;

    public MineGUIAccordion(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void addSection(MineGUIAccordionSection section) {
        sections.add(section);
    }

    public void setSingleOpen(boolean singleOpen) {
        this.singleOpen = singleOpen;
    }

    public void render(GuiGraphics graphics, Font font) {
        int currentY = y;

        for (MineGUIAccordionSection section : sections) {
            section.render(graphics, font, currentY);
            currentY += section.getFullHeight() + 4;
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        int currentY = y;

        for (MineGUIAccordionSection section : sections) {
            if (section.mouseClicked(mouseX, mouseY, currentY)) {
                if (singleOpen) {
                    sections.forEach(s -> s.setExpanded(false));
                }
                section.setExpanded(!section.isExpanded());
                return true;
            }

            currentY += section.getFullHeight() + 4;
        }

        return false;
    }
}
