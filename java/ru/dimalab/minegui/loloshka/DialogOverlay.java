package ru.dimalab.minegui.loloshka;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import ru.dimalab.minegui.utils.MineGUIColorPalette;

import java.util.List;

public class DialogOverlay {
    private static final int BACKGROUND_COLOR = MineGUIColorPalette.BLACK.getColor() & 0x80FFFFFF;
    private static final int PADDING = 8;
    private static final int TEXTURE_SIZE = 24;
    private static final int ICON_PADDING = 4;
    private static final int TEXT_SPACING = 4;

    public static void renderOverlay(GuiGraphics guiGraphics) {
        Dialog dialog = DialogManager.getCurrentDialog();
        if (dialog == null) return;

        Minecraft minecraft = Minecraft.getInstance();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        ResourceLocation texture = dialog.getTexture();

        Component formattedMessage = Component.literal("")
                .append(Component.literal("[").withStyle(style -> style.withColor(dialog.getNameColor())))
                .append(dialog.getCharacterName().copy().withStyle(style -> style.withColor(dialog.getNameColor())))
                .append(Component.literal("]: ").withStyle(style -> style.withColor(dialog.getNameColor())))
                .append(dialog.getMessage().copy().withStyle(style -> style.withColor(dialog.getMessageColor())));

        var font = minecraft.font;

        List<FormattedCharSequence> messageLines = font.split(formattedMessage, 780);
        int textWidth = messageLines.stream().mapToInt(font::width).max().orElse(180);
        int textHeight = messageLines.size() * (font.lineHeight + TEXT_SPACING);

        boolean hasIcon = texture != null;

        int iconWidth = hasIcon ? TEXTURE_SIZE + ICON_PADDING : 0;
        int boxWidth = PADDING + iconWidth + textWidth + PADDING;
        int boxHeight = PADDING + Math.max(TEXTURE_SIZE, textHeight) + PADDING;

        int x = (screenWidth - boxWidth) / 2;
        int y = screenHeight - 100;

        guiGraphics.fill(x, y, x + boxWidth, y + boxHeight, BACKGROUND_COLOR);

        if (hasIcon) {
            guiGraphics.blit(
                    texture,
                    x + PADDING, // X позиция
                    y + (boxHeight - TEXTURE_SIZE) / 2, // Центрируем по вертикали
                    0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE
            );
        }

        int textX = x + PADDING + iconWidth;
        int textY = y + PADDING;
        for (FormattedCharSequence line : messageLines) {
            guiGraphics.drawString(font, line, textX, textY, dialog.getMessageColor(), false);
            textY += font.lineHeight + TEXT_SPACING;
        }

        DialogManager.update();
    }
}
