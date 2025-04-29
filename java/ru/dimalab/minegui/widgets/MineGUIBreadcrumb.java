package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.ArrayList;
import java.util.List;

public class MineGUIBreadcrumb implements MineGUIWidget {
    private int x;
    private int y;
    private final List<String> paths;

    public MineGUIBreadcrumb(int x, int y) {
        this.x = x;
        this.y = y;
        this.paths = new ArrayList<>();
    }

    public void addPath(String path) {
        paths.add(path);
    }

    public void removeLastPath() {
        if (!paths.isEmpty()) {
            paths.remove(paths.size() - 1);
        }
    }

    public void clear() {
        paths.clear();
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        StringBuilder breadcrumb = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            breadcrumb.append(paths.get(i));
            if (i < paths.size() - 1) {
                breadcrumb.append(" > ");
            }
        }
        graphics.drawString(font, breadcrumb.toString(), getX(), getY(), 0xFFFFFF);
    }
}
