package ru.dimalab.minegui.loloshka;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ru.dimalab.minegui.utils.MineGUIColorPalette;

public class Dialog {
    private final Component characterName;
    private final Component message;
    private final MineGUIColorPalette nameColor;
    private final MineGUIColorPalette messageColor;
    private final int displayTime;

    private final ResourceLocation texture;

    public Dialog(ResourceLocation texture, Component characterName, Component message,
                  MineGUIColorPalette nameColor, MineGUIColorPalette messageColor,
                  int displayTime) {
        this.texture = texture;
        this.characterName = characterName;
        this.message = message;
        this.nameColor = nameColor;
        this.messageColor = messageColor;
        this.displayTime = displayTime;
    }

    public ResourceLocation getTexture() {return texture;}
    public Component getCharacterName() { return characterName; }
    public Component getMessage() { return message; }
    public int getNameColor() { return nameColor.getColor(); }
    public int getMessageColor() { return messageColor.getColor(); }
    public int getDisplayTime() { return displayTime; }
}
