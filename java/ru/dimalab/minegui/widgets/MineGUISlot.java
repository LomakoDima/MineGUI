/*

package ru.dimalab.minegui.widgets;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import ru.dimalab.minegui.screens.tooltips.Tooltip;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.util.Optional;

public class MineGUISlot implements MineGUIWidget {
    private final Slot slot;
    private int x, y;
    private int width = 18, height = 18;

    public MineGUISlot(Slot slot) {
        this.slot = slot;
        this.x = slot.x;
        this.y = slot.y;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        graphics.blit(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/inventory.png"),
                x, y, 7, 83, 18, 18);
        ItemStack stack = slot.getItem();
        if (!stack.isEmpty()) {
            graphics.renderItem(stack, x + 1, y + 1);
            graphics.renderItemDecorations(Minecraft.getInstance().font, stack, x + 1, y + 1);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            Player player = Minecraft.getInstance().player;
            if (player != null && slot.mayPickup(player)) {
                ItemStack taken = slot.safeTake(1, slot.getMaxStackSize(), player);
                // тут можно добавить взаимодействие с курсором или другим слотом
            }
            return true;
        }
        return false;
    }


    private boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}

 */