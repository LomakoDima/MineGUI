package ru.dimalab.minegui.utils.json;

import com.google.gson.annotations.SerializedName;

public class WidgetData {
    @SerializedName("type")
    public String type;
    @SerializedName("x")
    public int x;
    @SerializedName("y")
    public int y;
    // Общие поля для всех виджетов

    // Поля для MineGUIText
    public String text;
    public String color;
    public String[] gradientColors;
    public float scale;

    // Поля для MineGUIButton
    public String buttonText;
    public int buttonColor;
}
