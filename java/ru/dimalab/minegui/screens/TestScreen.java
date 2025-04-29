/* Demonstration Widgets

package ru.dimalab.minegui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.manager.ScreenManager;
import ru.dimalab.minegui.widgets.*;

import java.util.ArrayList;
import java.util.List;

public class TestScreen extends Screen {
    private  final Minecraft minecraftInstance;
    private List<MineGUIButton> buttons = new ArrayList<>();
    private List<MineGUIText> textWidgets = new ArrayList<>();
    private final List<MineGUIImage> imageWidgets = new ArrayList<>();
    private MineGUITable tableWidget;
    private MineGUIMarquee marqueeWidget;
    private MineGUICheckBox soundCheckBox;
    private MineGUISlider volumeSlider;
    private MineGUITextField nameTextField;
    private MineGUIDropdown dropdownMenu;
    private MineGUIRadioGroup radioGroup;
    private MineGUIStatusIndicator statusIndicator;
    private MineGUILoaderSpinner loaderSpinner;
    private MineGUITabs tabs;
    private MineGUIProgressBar progressBar;
    private MineGUIAccordion accordion;
    private MineGUITimePicker timePicker;
    private MineGUISidebar sidebar;




    public TestScreen(Minecraft mc) {
        super(Component.translatable("custom.screen"));
        this.minecraftInstance = mc;
        this.minecraft = mc; // ⚠️ важно для корректной работы суперкласса
    }

    @Override
    public void tick() {
        super.tick();

        nameTextField.tick();

        loaderSpinner.tick();

        int current = progressBar.getValue();
        if (current < 100) {
            progressBar.setValue(current + 1); // Увеличиваем прогресс
        } else {
            progressBar.setValue(0); // Сброс прогресса
        }

        if (marqueeWidget != null) {
            marqueeWidget.tick(this.font);
        }
    }

    @Override
    protected void init() {
        super.init();
        buttons.add(new MineGUIButton.Builder()
                .setPosition(50, 50)
                .setSize(120, 30)
                .setText(Component.literal("Gradient"))
                .setOnClick(() -> System.out.println("Gradient Button Clicked!"))
                .setGradientColors(0xFF4A90E2, 0xFF0057A3, 0xFF76C7C0, 0xFF3B8D99)
                .setTextColor(0xFFFFFFFF)
                .build());

        buttons.add(new MineGUIButton.Builder()
                .setPosition(50, 90)
                .setSize(120, 30)
                .setText(Component.literal("Solid Color"))
                .setOnClick(() -> System.out.println("Solid Button Clicked!"))
                .setSolidColor(0xFFFF5733, 0xFFC70039) // Простой цвет + цвет при наведении
                .setTextColor(0xFFFFFFFF)
                .build());


        // textWidgets.add(new MineGUIText("Hello, Minecraft!", 150, 210, 0xFFFFFF));

        textWidgets.add(new MineGUIText("Пример длинного текста, который должен переноситься на новую строку автоматически.", 50, 150, 0xFF0000, 0x0000FF));

        // Изображение
        imageWidgets.add(new MineGUIImage(ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/flower.png"), 50, 200, 64, 64));

        tableWidget = new MineGUITable(50, 290, 3, 100, 20);
        tableWidget.setHeaders("ID", "Имя", "Очки");

        tableWidget.addRow("1", "Дима", "100");
        tableWidget.addRow("2", "Вася", "200");
        tableWidget.addRow("3", "Кирилл", "150");

        marqueeWidget = new MineGUIMarquee(
                "Добро пожаловать в MineGUI! Это бегущая строка :)",
                50, 390, 300,
                0xFFFFFF, // Белый цвет, если градиент выключен
                0xFF00FF, // Начальный цвет градиента
                0x00FFFF, // Конечный цвет градиента
                true,      // Использовать градиент?
                2.0f       // Скорость прокрутки в пикселях за тик
        );

        soundCheckBox = new MineGUICheckBox(50, 400, 16, Component.literal("Включить звук"), true);

        soundCheckBox.setOnToggle(isChecked -> {
            System.out.println("Звук " + (isChecked ? "включен" : "выключен"));
        });

// Кастомные цвета (необязательно)
        soundCheckBox.setColors(
                FastColor.ARGB32.color(255, 60, 60, 60),    // фон чекбокса
                FastColor.ARGB32.color(255, 0, 0, 0),       // граница
                FastColor.ARGB32.color(255, 0, 200, 0),     // цвет галочки
                0xFFFFFF                                    // цвет текста
        );

        volumeSlider = new MineGUISlider(50, 450, 300, 12, 0, 100, 1, 50, Component.literal("Громкость"));
        volumeSlider.setOnValueChanged(val -> {
            System.out.println("Громкость изменена на " + val);
        });

// Можно изменить цвета (необязательно)
        volumeSlider.setColors(
                FastColor.ARGB32.color(255, 60, 60, 60),    // фон
                FastColor.ARGB32.color(255, 76, 175, 80),   // заполнение
                FastColor.ARGB32.color(255, 255, 255, 255), // ползунок
                FastColor.ARGB32.color(255, 0, 0, 0),       // граница
                0xFFFFFF                                    // текст
        );

        nameTextField = new MineGUITextField(350, 100, 300, 20, "Введите имя");
        nameTextField.setMaxLength(100);

        nameTextField.setOnTextChanged(newText -> {
            MineGUI.LOGGER.info("Текст изменён: {}", newText);
        });


        dropdownMenu = new MineGUIDropdown(400, 200, 150, 20,
                List.of("Option 1", "Option 2", "Option 3", "Option 4"), "Option 1");

        dropdownMenu.setOnSelect(option -> {
            System.out.println("Вы выбрали: " + option);
        });



        radioGroup = new MineGUIRadioGroup();

        MineGUIRadioButton option1 = new MineGUIRadioButton(670, 200, 16, "Option 1", radioGroup);
        MineGUIRadioButton option2 = new MineGUIRadioButton(670, 220, 16, "Option 2", radioGroup);
        MineGUIRadioButton option3 = new MineGUIRadioButton(670, 240, 16, "Option 3", radioGroup);

        radioGroup.addButton(option1);
        radioGroup.addButton(option2);
        radioGroup.addButton(option3);

        radioGroup.setOnSelect(option -> {
            System.out.println("Вы выбрали: " + option);
        });




        statusIndicator = new MineGUIStatusIndicator(
                560, 300, 16,
                "Сервер онлайн",
                MineGUIStatusIndicator.StatusType.ACTIVE
        );





        loaderSpinner = new MineGUILoaderSpinner(780, 500, 20, 4); // x, y, радиус, ширина линии
        loaderSpinner.setColor(0xFF00FFFF); // Цвет (бирюзовый)
        loaderSpinner.setSpeed(8f); // Скорость (чем больше, тем быстрее)


        tabs = new MineGUITabs(190, 50, 400, 20, 100); // x, y, ширина блока, высота вкладки, ширина вкладки

        tabs.addTab("Главная");
        tabs.addTab("Настройки");
        tabs.addTab("О проекте");

        tabs.setOnTabSelected(tab -> {
            System.out.println("Вы выбрали вкладку: " + tab.getLabel());
        });


        progressBar = new MineGUIProgressBar(560, 50, 200, 20);
        progressBar.setMax(100);
        progressBar.setValue(50); // Стартовое значение 50%
        progressBar.setShowPercentText(true);

// Можно изменить цвета
        progressBar.setColors(
                FastColor.ARGB32.color(255, 60, 60, 60), // Фон
                FastColor.ARGB32.color(255, 0, 120, 255), // Прогресс
                FastColor.ARGB32.color(255, 100, 100, 100) // Рамка
        );

        accordion = new MineGUIAccordion(700, 320, 300); // x, y, ширина секций

// Первая секция
        MineGUIAccordionSection section1 = new MineGUIAccordionSection("Информация", 640, 300, 20);
        section1.setContentHeight(60);
        section1.setContentRenderer(graphics -> {
            graphics.drawString(font, "Тут может быть информация!", 55, section1.getFullHeight() - 50, 0xFFFFFF, false);
        });

        accordion.addSection(section1);

// Вторая секция
        MineGUIAccordionSection section2 = new MineGUIAccordionSection("Настройки", 640, 300, 20);
        section2.setContentHeight(80);
        section2.setContentRenderer(graphics -> {
            graphics.drawString(font, "Настройки:", 55, section2.getFullHeight() - 70, 0xFFFFFF, false);
            graphics.drawString(font, "- Опция 1", 55, section2.getFullHeight() - 55, 0xAAAAAA, false);
            graphics.drawString(font, "- Опция 2", 55, section2.getFullHeight() - 40, 0xAAAAAA, false);
        });

        accordion.addSection(section2);

// Третья секция
        MineGUIAccordionSection section3 = new MineGUIAccordionSection("Помощь", 640, 300, 20);
        section3.setContentHeight(40);
        section3.setContentRenderer(graphics -> {
            graphics.drawString(font, "FAQ и помощь здесь", 55, section3.getFullHeight() - 30, 0xFFFFFF, false);
        });

        accordion.addSection(section3);

// По умолчанию одна открыта
        accordion.setSingleOpen(true);


        timePicker = new MineGUITimePicker(50, 150, 150, 60);
        timePicker.setTime(12, 30); // Начальные часы и минуты
        timePicker.setOnTimeChanged((hours, minutes) -> {
            System.out.println("Выбрано время: " + String.format("%02d:%02d", hours, minutes));
        });

        sidebar = new MineGUISidebar(0, 0, 100, height); // x, y, width, height

// Добавляем пункты меню
        sidebar.addItem("Главная");
        sidebar.addItem("Настройки");
        sidebar.addItem("Профиль");
        sidebar.addItem("Выход");

        sidebar.setOnItemSelected(index -> {
            System.out.println("Выбран пункт меню: " + sidebar.items.get(index));
        });


    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null) {
            return; // Избегаем крэша, если Minecraft еще не инициализирован
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        for (MineGUIButton button : buttons) {
            button.render(guiGraphics, this.font, mouseX, mouseY);
        }
        for (MineGUIText textWidget : textWidgets) {
            textWidget.render(guiGraphics, this.font, mouseX, mouseY);
        }
        for (MineGUIImage imageWidget : imageWidgets) {
            imageWidget.render(guiGraphics);
        }

        for (MineGUIRadioButton button : radioGroup.getButtons()) {
            button.render(guiGraphics, this.font, mouseX, mouseY);
        }

        tableWidget.render(guiGraphics, this.font, mouseX, mouseY);

        marqueeWidget.render(guiGraphics, this.font);

        soundCheckBox.render(guiGraphics, this.font, mouseX, mouseY);

        volumeSlider.render(guiGraphics, this.font, mouseX, mouseY);

        nameTextField.render(guiGraphics, this.font, mouseX, mouseY);

        dropdownMenu.render(guiGraphics, this.font, mouseX, mouseY);

        statusIndicator.render(guiGraphics, this.font);

        loaderSpinner.render(guiGraphics);

        tabs.render(guiGraphics, this.font);

        progressBar.render(guiGraphics, this.font);

        accordion.render(guiGraphics, this.font);

        timePicker.render(guiGraphics, this.font, mouseX, mouseY);

        sidebar.render(guiGraphics, this.font, mouseX, mouseY);

        switch (tabs.getSelectedIndex()) {
            case 0 -> guiGraphics.drawString(this.font, "Контент Главной", 50, 100, 0xFFFFFF, false);
            case 1 -> guiGraphics.drawString(this.font, "Настройки", 50, 100, 0xFFFFFF, false);
            case 2 -> guiGraphics.drawString(this.font, "Информация о проекте", 50, 100, 0xFFFFFF, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (MineGUIButton btn : buttons) {
            btn.mouseClicked((int) mouseX, (int) mouseY, button);
        }
        if (dropdownMenu.mouseClicked((int) mouseX, (int) mouseY, button)) {
            return true;
        }
        for (MineGUIRadioButton radioButton : radioGroup.getButtons()) {
            if (radioButton.mouseClicked((int) mouseX, (int) mouseY, button)) {
                return true;
            }
        }

        if (sidebar.mouseClicked((int) mouseX, (int) mouseY, button)) {
            return true;
        }

        if (statusIndicator.getStatus() == MineGUIStatusIndicator.StatusType.ACTIVE) {
            statusIndicator.setStatus(MineGUIStatusIndicator.StatusType.ERROR);
            statusIndicator.setLabel("Сервер оффлайн");
        } else {
            statusIndicator.setStatus(MineGUIStatusIndicator.StatusType.ACTIVE);
            statusIndicator.setLabel("Сервер онлайн");
        }

        if (tabs.mouseClicked((int) mouseX, (int) mouseY, button)) {
            return true;
        }

        if (accordion.mouseClicked((int) mouseX, (int) mouseY, button)) {
            return true;
        }

        if (timePicker.mouseClicked((int) mouseX, (int) mouseY, button)) {
            return true;
        }

        soundCheckBox.onClick((int) mouseX, (int) mouseY);
        volumeSlider.mouseClicked((int) mouseX, (int) mouseY, button);
        nameTextField.mouseClicked((int) mouseX, (int) mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        volumeSlider.mouseReleased((int) mouseX, (int) mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        volumeSlider.mouseDragged((int) mouseX, (int) mouseY);
        timePicker.mouseMoved((int) mouseX, (int) mouseY);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return nameTextField.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 258) { // TAB
            sidebar.toggle();
            return true;
        }

        return nameTextField.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }



    @Override
    public void onClose() {
        ScreenManager.closeScreen();
    }


}

*/