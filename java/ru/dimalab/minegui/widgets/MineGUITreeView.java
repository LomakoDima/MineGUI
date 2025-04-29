package ru.dimalab.minegui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class MineGUITreeView implements MineGUIWidget {
    private final List<TreeNode> rootNodes = new ArrayList<>();
    private final int x;
    private final int y;

    private MineGUITreeView(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.rootNodes.addAll(builder.rootNodes);
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int offsetY = 0;
        for (TreeNode node : rootNodes) {
            offsetY += node.render(graphics, font, x, y + offsetY, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        for (TreeNode node : rootNodes) {
            if (node.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private final List<TreeNode> rootNodes = new ArrayList<>();
        private int x;
        private int y;

        public Builder at(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder addRootNode(TreeNode node) {
            rootNodes.add(node);
            return this;
        }

        public MineGUITreeView build() {
            return new MineGUITreeView(this);
        }
    }

    public static class TreeNode {
        private final String label;
        private final List<TreeNode> children = new ArrayList<>();
        private boolean expanded;
        private int x, y;

        public TreeNode(String label) {
            this.label = label;
            this.expanded = false;
        }

        public TreeNode addChild(TreeNode child) {
            children.add(child);
            return this;
        }

        public int render(GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY) {
            this.x = x;
            this.y = y;
            graphics.drawString(font, (expanded ? "[-] " : "[+] ") + label, x, y, 0xFFFFFFFF);
            int offsetY = 10;

            if (expanded) {
                for (TreeNode child : children) {
                    offsetY += child.render(graphics, font, x + 10, y + offsetY, mouseX, mouseY);
                }
            }
            return offsetY;
        }

        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 10) {
                expanded = !expanded;
                return true;
            }

            for (TreeNode child : children) {
                if (child.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            return false;
        }
    }
}

