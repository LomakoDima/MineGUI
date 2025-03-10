package ru.dimalab.minegui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import ru.dimalab.minegui.utils.TextStyle;
import ru.dimalab.minegui.widgets.MineGUIButton;
import ru.dimalab.minegui.widgets.MineGUIText;

public class ExampleScreen extends MineGUIScreen {
    public ExampleScreen(Minecraft mc, String title) {
        super(mc, title);
    }

    @Override
    protected void initWidgets() {
        MineGUIButton gradientButton = new MineGUIButton.Builder()
                .setPosition(50, 50)
                .setSize(120, 30)
                .setText(Component.literal("Gradient"))
                .setOnClick(() -> System.out.println("Gradient Button Clicked!"))
                .setGradientColors(0xFF4A90E2, 0xFF0057A3, 0xFF76C7C0, 0xFF3B8D99)
                .setTextColor(0xFFFFFFFF)
                .build();

        MineGUIButton solidButton = new MineGUIButton.Builder()
                .setPosition(50, 90)
                .setSize(120, 30)
                .setText(Component.literal("Solid Color"))
                .setOnClick(() -> System.out.println("Solid Button Clicked!"))
                .setSolidColor(0xFFFF5733, 0xFFC70039)
                .setTextColor(0xFFFFFFFF)
                .build();

        MineGUIButton button = new MineGUIButton.Builder()
                .setPosition(50, 230)
                .setSize(120, 30)
                .setText(Component.literal("Click Me"))
                .setHoverText(Component.literal("Hovered!"))
                .setTextStyle(TextStyle.BOLD)
                .setHoverTextStyle(TextStyle.UNDERLINE)
                .setTextSize(9.0f)
                .setHoverTextSize(12.0f)  // Увеличиваем текст при наведении
                .setOnClick(() -> System.out.println("Button Clicked!"))
                .setSolidColor(0xFF4A90E2, 0xFF0057A3)
                .setTextColor(0xFFFFFFFF)
                .build();

        MineGUIText textWidget = new MineGUIText.Builder()
                .setText("Hello, World!")
                .setPosition(50, 150)
                .setGradientColors(0xFF4A90E2, 0xFF0057A3)
                .build();

        addWidgets(gradientButton, textWidget, solidButton, button);
    }
}
