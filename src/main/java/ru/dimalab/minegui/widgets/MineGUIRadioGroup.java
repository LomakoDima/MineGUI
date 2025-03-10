package ru.dimalab.minegui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MineGUIRadioGroup {
    private final List<MineGUIRadioButton> buttons = new ArrayList<>();
    private Consumer<String> onSelect;

    public MineGUIRadioGroup() {}

    public void addButton(MineGUIRadioButton button) {
        buttons.add(button);

        if (buttons.size() == 1) {
            selectButton(button);
        }
    }

    public void selectButton(MineGUIRadioButton button) {
        for (MineGUIRadioButton b : buttons) {
            b.setSelected(false);
        }
        button.setSelected(true);

        if (onSelect != null) {
            onSelect.accept(button.getLabel());
        }
    }

    public void setOnSelect(Consumer<String> onSelect) {
        this.onSelect = onSelect;
    }

    public String getSelectedOption() {
        return buttons.stream()
                .filter(MineGUIRadioButton::isSelected)
                .map(MineGUIRadioButton::getLabel)
                .findFirst()
                .orElse("");
    }

    public List<MineGUIRadioButton> getButtons() {
        return buttons;
    }
}
