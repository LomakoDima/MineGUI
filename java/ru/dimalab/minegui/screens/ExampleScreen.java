package ru.dimalab.minegui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.manager.ScreenManager;
import ru.dimalab.minegui.screens.tooltips.TextTooltip;
import ru.dimalab.minegui.screens.tooltips.TooltipPosition;
import ru.dimalab.minegui.screens.tooltips.TooltipStyle;
import ru.dimalab.minegui.screens.utils.CollapsibleWindow;
import ru.dimalab.minegui.screens.utils.MineGUIGridBox;
import ru.dimalab.minegui.screens.utils.MineGUIHorizontalBox;
import ru.dimalab.minegui.utils.*;
import ru.dimalab.minegui.utils.math.interpolation.InterpolationType;
import ru.dimalab.minegui.widgets.*;
import ru.dimalab.minegui.widgets.MineGUIScrollbar;
import ru.dimalab.minegui.widgets.notifications.NotificationType;
import ru.dimalab.minegui.widgets.notifications.NotificationWidget;

import java.util.List;

public class ExampleScreen extends MineGUIScreen {
    public ExampleScreen(Minecraft mc, String title) {
        super(mc, title);
    }

    @Override
    protected void setupWidgets() {
        setGradientBackground(List.of(
                MineGUIColorPalette.BLUE_VIOLET,
                MineGUIColorPalette.GREEN,
                MineGUIColorPalette.BLUE
        ));
        //setBackgroundColor(MineGUIColorPalette.WHITE);
        setBackgroundOpacity(0.3f);
        //setBackgroundTexture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/l.png"));

        MineGUIHorizontalBox hBox = new MineGUIHorizontalBox(200, 50, 125);
        //MineGUIVerticalBox vBox = new MineGUIVerticalBox(200, 100, 10);


        MineGUIButton gradientButton = new MineGUIButton.Builder()
                //.setPosition(50, 50)
                .setButtonAlignment(Alignment.CENTER)
                .setSize(200, 20)
                .setText(Component.literal("Gradient"))
                .setImages("minegui:textures/gui/button.png", "minegui:textures/gui/button_hover.png")
                .setOnClick(ScreenManager::closeScreen)
                //.setOnClick(this::saveWidgets)
                //.setGradientColors(0xFF4A90E2, 0xFF0057A3, 0xFF76C7C0, 0xFF3B8D99)
                //.setTextColor(0xFFFFFFFF)
                //.setBorderColor(0xFFFFFFFF)
                //.setBorderThickness(1)
                .build();

        MineGUIButton solidButton = new MineGUIButton.Builder()
                //.setPosition(50, 90)
                .setSize(120, 30)
                .setText(Component.literal("Solid Color"))
                .setOnClick(() -> System.out.println("Solid Button Clicked!"))
                //.setSolidColor(0xFFFF5733, 0xFFC70039)
                //.setTextColor(0xFFFFFFFF)
                .build();

        MineGUIButton button1 = new MineGUIButton.Builder()
                //.setPosition(50, 140)
                .setSize(120, 30)
                .setText(Component.literal("Click Me"))
                //.setTextGradient(0xFF4A90E2, 0xFF0057A3)
                //.setHoverTextGradient(0xFFFF5733, 0xFFC70039)
                .build();

        MineGUIButton button = new MineGUIButton.Builder()
                //.setPosition(50, 200)
                .setSize(120, 30)
                .setText(Component.literal("Click Me"))
                .setHoverText(Component.literal("Hovered!"))
                .setTextStyle(TextStyle.ITALIC)
                .setHoverTextStyle(TextStyle.UNDERLINE)
                .setTextSize(9.0f)
                .setHoverTextSize(12.0f)  // Увеличиваем текст при наведении
                .setOnClick(() -> System.out.println("Button Clicked!"))
                .setSolidColor(MineGUIColorPalette.GOLD, MineGUIColorPalette.DARK_ORANGE)
                .setTextColor(MineGUIColorPalette.WHITE)
                .setTextAlignment(TextAlignment.LEFT)
                .build();

        MineGUIText textWidget = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 50)
                .setColor(MineGUIColorPalette.WHITE)
                .build();

        MineGUIText textWidget1 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 70)
                .setColor(MineGUIColorPalette.WHITE)
                .setMaxWidth(200)
                .build();

        MineGUIText textWidget12 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 110)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .build();

        MineGUIText textWidget123 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 130)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setMaxWidth(200)
                .build();

        MineGUIText textWidget1234 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 170)
                .setColor(MineGUIColorPalette.WHITE)
                .addStyle(TextStyle.STRIKETHROUGH)
                .build();

        MineGUIText textWidget12345 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 190)
                .setColor(MineGUIColorPalette.WHITE)
                .addStyle(TextStyle.UNDERLINE)
                .build();

        MineGUIText textWidget123456 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 210)
                .setColor(MineGUIColorPalette.WHITE)
                .addStyle(TextStyle.BOLD)
                .build();

        MineGUIText textWidget1234567 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 230)
                .setColor(MineGUIColorPalette.WHITE)
                .addStyle(TextStyle.ITALIC)
                .build();

        MineGUIText textWidget12345678 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 250)
                .setColor(MineGUIColorPalette.WHITE)
                .addStyle(TextStyle.OBFUSCATED)
                .build();

        MineGUIText textLetter = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически"))
                .setPosition(250, 270)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setLetterSpacing(4f)
                .build();

        MineGUIText exampleAnim1 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)" + " ( " + InterpolationType.LINEAR + " )"))
                .setPosition(250, 290)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setAnimation(AnimationType.SCALE_UP, InterpolationType.LINEAR, 1500)
                .build();

        MineGUIText exampleAnim2 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"  + " ( " + InterpolationType.QUADRATIC + " )"))
                .setPosition(250, 310)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setAnimation(AnimationType.SCALE_UP, InterpolationType.QUADRATIC, 1500)
                .build();

        MineGUIText exampleAnim3 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"  + " ( " + InterpolationType.EASE_IN + " )"))
                .setPosition(250, 330)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setAnimation(AnimationType.SCALE_UP, InterpolationType.EASE_IN, 1500)
                .build();

        MineGUIText exampleAnim4 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"  + " ( " + InterpolationType.EASE_OUT + " )"))
                .setPosition(250, 350)
                //.setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                //.setAnimation(AnimationType.SCALE_UP, InterpolationType.EASE_OUT, 1500)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .withOutline(true)
                .setOutlineColor(MineGUIColorPalette.BLACK)
                .setOutlineThickness(1)
                .build();

        MineGUIText exampleAnim5 = new MineGUIText.Builder()
                .setText(Component.literal("Это текст который должен переноситься автоматически, а если нет то значит и фиг с ним :)"))
                .setPosition(250, 370)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setAnimation(AnimationType.TYPEWRITER, InterpolationType.EASE_IN, 60)
                .build();

        MineGUIText bgText = new MineGUIText.Builder()
                .setText(Component.literal("Это текст с фоном  :)"))
                .setPosition(250, 390)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setBackgroundColor(MineGUIColorPalette.WHITE)
                .setBackgroundPadding(4)
                .build();

        MineGUIText mineText = new MineGUIText.Builder()
                .setPosition(250, 410)
                //.setAlignment(Alignment.CENTER)
                .setText(Component.literal("Увеличенный текст"))
                .setUseShadow(false)
                .setGradientColors(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.LIGHT_SALMON)
                .setScale(2f)
                .build();

        MineGUIImage mineGUIImage = new MineGUIImage.Builder()
                .position(20, 350)
                .size(128, 128)
                .texture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/button.png"))
                .border(true)                          // Включаем обводку
                .borderColor(0xFFFF0000)               // Красный цвет ARGB (полностью непрозрачный)
                .borderThickness(2)
                .build();

        //MineGUICountdown countdown = new MineGUICountdown(250, 300, 100, () -> {});

        MineGUISlider slider = new MineGUISlider.Builder()
                .setPosition(50, 100)
                .setSize(200, 10)
                .setRange(0, 100, 5)
                .setDefaultValue(50)
                .setLabel(Component.literal("Громкость"))
                .setColors(
                        MineGUIColorPalette.DARK_GRAY,
                        MineGUIColorPalette.EMERALD,
                        MineGUIColorPalette.GOLD,
                        MineGUIColorPalette.BLACK,
                        MineGUIColorPalette.WHITE
                )
                .setKnobTexture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/konb1.png"))
                .setKnobSize(10, 30)
                .setOnValueChanged(value -> MineGUI.LOGGER.info("Новое значение: {}", value))
                .build();


        MineGUITabs tabs = new MineGUITabs.Builder()
                .setPosition(20, 10)
                .setSize(200, 30, 100)
                .addTab("Главная", 0, MineGUIColorPalette.DARK_GRAY, MineGUIColorPalette.CHOCOLATE, MineGUIColorPalette.WHITE)
                .addTab("Настройки", 1, MineGUIColorPalette.DARK_GRAY, MineGUIColorPalette.CHOCOLATE, MineGUIColorPalette.WHITE)
                .addTab("О нас", 2, MineGUIColorPalette.DARK_GRAY, MineGUIColorPalette.CHOCOLATE, MineGUIColorPalette.WHITE)
                .setOnTabSelected(tab -> System.out.println("Выбрана вкладка: " + tab.getLabel()))
                .build();


        assert Minecraft.getInstance().player != null;
        MineGUIMarquee marquee = new MineGUIMarquee.Builder()
                .setText(String.valueOf(Minecraft.getInstance().player.getX()))
                .setPosition(50, 130)
                .setWidth(200)
                .setColor(MineGUIColorPalette.LIGHT_CORAL)
                .setSpeed(3f)
                .build();

        MineGUICheckBox checkBox = new MineGUICheckBox.Builder(10, 20, 16, Component.literal("Check me!"))
                .defaultValue(true)
                .checkTexture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/mark.png"))
                .texture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/bg.png"))
                .onToggle(checked -> {
                    MineGUI.LOGGER.info("Checkbox toggled! Checked: {}", checked);
                })
                .build();

        MineGUITable table = new MineGUITable.Builder()
                .position(600, 150)
                .columns(3)
                .columnWidth(100)
                .rowHeight(20)
                .headerColor(MineGUIColorPalette.DARK_BLUE)
                .rowColor1(MineGUIColorPalette.SILVER)
                .rowColor2(MineGUIColorPalette.LIGHT_GRAY)
                .textColor(MineGUIColorPalette.BLACK)
                .borderColor(MineGUIColorPalette.NAVY)
                .build();

        CollapsibleWindow collapsibleWindow = new CollapsibleWindow.Builder()
                .setPosition(20, 50)
                .setSize(200, 100)
                .setTitle("Collapsible Window")
                .build();

        ResourceLocation backgroundTexture = ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/progress_empty.png");
        ResourceLocation progressTexture = ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/progress_fill.png");

        //MineGUIProgressBar progressBar = new MineGUIProgressBar(10, 10, 200, 20);
        //progressBar.setBackgroundTexture(backgroundTexture, 0, 0);
        //progressBar.setProgressTexture(progressTexture, 0, 0);
        //progressBar.setMax(100);
        // progressBar.setValue(50);

        table.setHeaders("ID", "Name", "Status");

        table.addRow("1", "Alice", "Active");
        table.addRow("2", "Bob", "Inactive");
        table.addRow("3", "Charlie", "Pending");


        CollapsibleWindow window = new CollapsibleWindow.Builder()
                .setPosition(20, 300)
                .setSize(350, 100)
                .setTitle("Debug")
                .build();

        CollapsibleWindow window1 = new CollapsibleWindow.Builder()
                .setPosition(120, 300)
                .setSize(350, 100)
                .setTitle("Debug1")
                .build();

        MineGUIMultilineTextField mineGUIMultilineTextField = new MineGUIMultilineTextField.Builder()
                .setPosition(300, 350)
                .setSize(200, 100)
                .setMaxLines(10)
                .setPlaceholder("Введите любой текст...")
                .build();

        //MineGUIBlinkingCursor blinkingCursor = new MineGUIBlinkingCursor(200, 200);

        MineGUIImageSlider widget = new MineGUIImageSlider.Builder()
                .setPosition(250, 50)
                .setSize(500, 500)
                .addImage(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/l.png"))
                .addImage(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/l1.png"))
                .build();

        MineGUILine horizontalLine = new MineGUILine.Builder()
                .setPosition(100, 200)
                .setLength(100)
                .setThickness(1)
                .setColor(MineGUIColorPalette.ORANGE)
                .setHorizontal(true)
                .build();
        MineGUILine verticalLine = new MineGUILine.Builder()
                .setPosition(100, 200)
                .setLength(100)
                .setThickness(1)
                .setColor(MineGUIColorPalette.ORANGE_RED)
                .setHorizontal(false)
                .build();

        MineGUICountdown timer = new MineGUICountdown(300, 100, 100); // 5 минут, позиция (100,100)
        timer.setPosition(50, 50); // Перемещение
        timer.reset(60); // Сброс на 1 минуту

        MineGUIStopWatch watch = new MineGUIStopWatch.Builder()
                .setPosition(100, 100)
                .build();

        watch.toggle();


        MineGUIBreadcrumb breadcrumb = new MineGUIBreadcrumb(450, 20);

        breadcrumb.addPath("Home");
        breadcrumb.addPath("Library");
        breadcrumb.addPath("Books");
        breadcrumb.addPath("Fantasy");

        //MineGUIMultilineTextField1 multilineTextField1 = new MineGUIMultilineTextField1(100, 100, 200, 100, Minecraft.getInstance().font, 10);

        MineGUIMultilineTextField multilineTextField = new MineGUIMultilineTextField.Builder()
                .setPosition(100, 100)
                .setSize(200, 100)
                .setPlaceholder("Enter the text...")
                .setMaxLines(10)
                .build();

        MineGUIScrollbar scrollBar = new MineGUIScrollbar(200, 200, 200, MineGUIScrollbar.Orientation.HORIZONTAL);
        scrollBar.setRange(0, 500);

        MineGUIText newText = new MineGUIText.Builder()
                .setText(Component.literal("New Text"))
                .setPosition(200, 200)
                //.setAlignment(Alignment.BOTTOM_CENTER)
                .setUseShadow(true)
                .setColor(MineGUIColorPalette.WHITE)
                .build();

        MineGUIText text_ = new MineGUIText.Builder()
                .setText(Component.literal("Hello World"))
                .setPosition(120, 200)
                .setColor(MineGUIColorPalette.BLUE)
                .setBackgroundColor(MineGUIColorPalette.MAGENTA)
                .setBackgroundPadding(4)
                .build();

        MineGUITime timeWidget = new MineGUITime(
                Minecraft.getInstance().font,
                20, 100, // начальные координаты (будут пересчитаны в updatePosition)
                "HH:mm:ss"
        );

        MineGUIImage image = new MineGUIImage.Builder()
                .position(20, 20)
                .size(300, 300)
                .texture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/logo.png"))
                .build();

        //MineGUITextField mineGUITextField = new MineGUITextField(350, 50, 200, 20, "Enter the text...");




        TextTooltip tooltip = new TextTooltip(
                Component.literal("Нажми меня для действия!"),
                TooltipStyle.ORANGE,
                TooltipPosition.FOLLOW_CURSOR
        );

        MineGUIButton buttonWithTooltip = new MineGUIButton.Builder()
                //.setPosition(50, 50)
                .setSize(120, 30)
                .setText(Component.literal("Click Me"))
                .setTooltip(tooltip)
                //.setSolidColor(0xFF4A90E2, 0xFF0057A3)
                //.setTextColor(0xFFFFFFFF)
                .build();

        MineGUIStatusIndicator indicator = new MineGUIStatusIndicator.Builder()
                .setPosition(10, 10)
                .setSize(20)
                .setLabel("System Status")
                .setStatus(MineGUIStatusIndicator.StatusType.WARNING)
                .setTextColor(MineGUIColorPalette.WHITE)
                .build();

        Level level = Minecraft.getInstance().level;

        MineGUILine newLine = new MineGUILine.Builder()
                .setPosition(100, 200)
                .setLength(300)
                .setThickness(1)
                .setColor(MineGUIColorPalette.MAUVE)
                .setHorizontal(true)
                .setBorderStyle(BorderStyle.SOLID)
                .setGradientEnabled(false)
                .build();

        NotificationWidget notificationWidget = new NotificationWidget();

        MineGUIButton notificationButton = new MineGUIButton.Builder()
                //.setPosition(230, 290)
                .setSize(120, 30)
                .setText(Component.literal("Show Notification"))
                .setSolidColor(MineGUIColorPalette.LIGHT_CORAL, MineGUIColorPalette.LIGHT_SALMON)
                .setTextColor(MineGUIColorPalette.WHITE)
                .setOnClick(() -> {
                    notificationWidget.addNotification(Component.literal("Это уведомление!"), NotificationType.INFO, 4500);
                })
                .build();

        MineGUIButton button2 = new MineGUIButton.Builder()
                //.setPosition(100, 100)
                .setSize(200, 40)
                .setText(Component.literal("[User]"))
                .setGradientColors(MineGUIColorPalette.BLUE, MineGUIColorPalette.DARK_BLUE, MineGUIColorPalette.NEON_GREEN, MineGUIColorPalette.BRONZE)
                .setHoverTextGradient(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.BLUE)
                .setPressedSolidColor(MineGUIColorPalette.RED) // Цвет при нажатии
                .setTextAlignment(TextAlignment.CENTER)
                .build();

        MineGUIButton button3 = new MineGUIButton.Builder()
                .setOffset(250, 50)
                .setSize(200, 40)
                .setText(Component.literal("[User]"))
                .setGradientColors(MineGUIColorPalette.BLUE, MineGUIColorPalette.DARK_BLUE, MineGUIColorPalette.NEON_GREEN, MineGUIColorPalette.BRONZE)
                .setHoverTextGradient(MineGUIColorPalette.CRIMSON, MineGUIColorPalette.BLUE)
                .setPressedSolidColor(MineGUIColorPalette.RED) // Цвет при нажатии
                .setTextAlignment(TextAlignment.CENTER)
                .setTextColor(MineGUIColorPalette.WHITE)
                .setSolidColor(MineGUIColorPalette.ORANGE, MineGUIColorPalette.AQUAMARINE)
                .build();

        MineGUIDropdown dropdown = new MineGUIDropdown.Builder()
                .setPosition(150, 50)
                .setSize(200, 20)
                .setOptions(List.of("Option 1", "Option 2", "Option 3", "Option 4"))
                .setDefaultOption("Option 1")
                .setBackgroundColor(MineGUIColorPalette.OLIVE)
                .setTextColor(MineGUIColorPalette.SKY_BLUE)
                .setBorderColor(MineGUIColorPalette.ORANGE)
                .setBorderColorFocused(MineGUIColorPalette.AZURE)
                .setHoverColor(MineGUIColorPalette.CHERRY)
                .build();


        MineGUITextField newTextF = new MineGUITextField.Builder()
                .setPosition(20, 50)
                .setSize(200, 20)
                .setPlaceholder("Введите текст...")
                .build();

        MineGUIRadioGroup radioGroup = new MineGUIRadioGroup.Builder()
                .addButton(10, 10, 14, "Option 1")
                .addButton(10, 30, 14, "Option 2")
                .setOnSelect(selected -> System.out.println("Selected: " + selected))
                .build();

        MineGUIProgressBar pssBar = new MineGUIProgressBar.Builder()
                .setPosition(50, 50)
                .setSize(200, 20)
                .setMax(100)
                .setValue(50)
                .setBackgroundTexture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/progress_empty.png"), 0, 0)
                .setProgressTexture(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/progress_fill.png"), 0, 0)
                .build();

        MineGUITreeView treeView2 = new MineGUITreeView.Builder()
                .at(300, 300)
                .addRootNode(new MineGUITreeView.TreeNode("Root 1")
                        .addChild(new MineGUITreeView.TreeNode("Child 1.1"))
                        .addChild(new MineGUITreeView.TreeNode("Child 1.2")
                                .addChild(new MineGUITreeView.TreeNode("Subchild 1.2.1"))
                        )
                )
                .addRootNode(new MineGUITreeView.TreeNode("Root 2")
                        .addChild(new MineGUITreeView.TreeNode("Child 2.1"))
                )
                .build();



        MineGUIColorIndicator colorIndicator = new MineGUIColorIndicator(50, 50, 20, 0xFF3498DB);

        MineGUIGraph graph = new MineGUIGraph(200, 200, 100, 100);
        graph.setGraphColor(0xFF00FF00);
        graph.addDataPoint(25.5f);


        assert level != null;
        Villager creeper = EntityType.VILLAGER.create(level);

        if (creeper != null) {
            creeper.setNoAi(true);
            creeper.setInvulnerable(true);
            creeper.setSilent(true);

            MineGUIEntityWidget entityWidget = new MineGUIEntityWidget.Builder()
                    .position(400, 122)
                    .size(150, 200)
                    .scale(1f)
                    //.shadow(true)
                    .rotateOnMouse(true)
                    .offset(2, -5)
                    .entity(creeper)
                    //.borderColor(MineGUIColorPalette.EMERALD)
                    .background(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/entity_overlay.png"))
                    .backgroundSize(150, 210)
                    .backgroundPosition(400, 110)
                    .build();





            window.addChild(button1, 20, 5);




        hBox.addChild(gradientButton);
        hBox.addChild(solidButton);
        hBox.addChild(button);

        //setBackgroundTexture("minegui:textures/gui/l.png");


        //window1.addChild(hBox, 10, 2);

        // вертикальный контейнер
        //vBox.addChild(button);
        //vBox.addChild(gradientButton);
        //vBox.addChild(solidButton);



        addWidgets(gradientButton,// textWidget, /*solidButton, button1*/ textWidget1, textWidget123, textWidget1234,
                //textWidget12, textWidget12345, mineText,
                //textWidget123456, textWidget1234567, textWidget12345678, textLetter, exampleAnim1,
                //exampleAnim2, exampleAnim3, exampleAnim4, exampleAnim5, bgText, //mineGUIImage, /*canvasPanel*/
                //hBox, slider, tabs, marquee, checkBox, table, entityWidget, window, mineGUIMultilineTextField,
                //widget,
                //horizontalLine,
                //verticalLine, timer, stopwatch, treeView, breadcrumb, multilineTextField,
                //scrollBar,
                //newText, text_, horizontalLine, verticalLine, mineGUITextField, slider, newGridBox,
                //buttonWithTooltip, checkBox, progressBar, watch, indicator,
                //newLine
                //notificationWidget, notificationButton, button2, button,
                //button3, mineText, mineGUITextField,
                //collapsibleWindow, dropdown, timeWidget
                //image
                //vBox
                //newTextF, radioGroup, pssBar,
                //treeView2,
                colorIndicator,
                graph
        );
        }
    }
}
