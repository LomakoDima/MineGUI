package ru.dimalab.minegui.utils;

public enum MineGUIColorPalette {
    // Reds
    RED(0xFFFF0000),
    DARK_RED(0xFF8B0000),
    FIREBRICK(0xFFB22222),
    CRIMSON(0xFFDC143C),
    INDIAN_RED(0xFFCD5C5C),
    LIGHT_CORAL(0xFFF08080),
    SALMON(0xFFFA8072),
    DARK_SALMON(0xFFE9967A),
    LIGHT_SALMON(0xFFFFA07A),
    ROSE(0xFFFF007F),
    CHERRY(0xFFDE3163),
    MAROON(0xFF800000),

    // Oranges
    ORANGE(0xFFFFA500),
    DARK_ORANGE(0xFFFF8C00),
    CORAL(0xFFFF7F50),
    TOMATO(0xFFFF6347),
    ORANGE_RED(0xFFFF4500),
    GOLD(0xFFFFD700),
    AMBER(0xFFFFBF00),
    PEACH(0xFFFFE5B4),
    APRICOT(0xFFFBCEB1),
    PUMPKIN(0xFFFF7518),

    // Yellows
    YELLOW(0xFFFFFF00),
    LIGHT_YELLOW(0xFFFFFFE0),
    LEMON_CHIFFON(0xFFFFFACD),
    LIGHT_GOLDENROD(0xFFFAFAD2),
    MUSTARD(0xFFFFDB58),
    CANARY(0xFFFFFF99),
    SUNFLOWER(0xFFFFD300),

    // Greens
    GREEN(0xFF008000),
    DARK_GREEN(0xFF006400),
    LIME(0xFF00FF00),
    LIME_GREEN(0xFF32CD32),
    SPRING_GREEN(0xFF00FF7F),
    SEA_GREEN(0xFF2E8B57),
    MEDIUM_SEA_GREEN(0xFF3CB371),
    EMERALD(0xFF50C878),
    MINT(0xFF98FF98),
    NEON_GREEN(0xFF39FF14),
    FOREST_GREEN(0xFF228B22),
    OLIVE(0xFF808000),

    // Blues
    BLUE(0xFF0000FF),
    DARK_BLUE(0xFF00008B),
    MIDNIGHT_BLUE(0xFF191970),
    NAVY(0xFF000080),
    ROYAL_BLUE(0xFF4169E1),
    SKY_BLUE(0xFF87CEEB),
    LIGHT_SKY_BLUE(0xFF87CEFA),
    DEEP_SKY_BLUE(0xFF00BFFF),
    CYAN(0xFF00FFFF),
    TEAL(0xFF008080),
    AQUAMARINE(0xFF7FFFD4),
    AZURE(0xFFF0FFFF),

    // Purples
    PURPLE(0xFF800080),
    MEDIUM_PURPLE(0xFF9370DB),
    BLUE_VIOLET(0xFF8A2BE2),
    INDIGO(0xFF4B0082),
    PLUM(0xFFDDA0DD),
    ORCHID(0xFFDA70D6),
    LAVENDER(0xFFE6E6FA),
    MAGENTA(0xFFFF00FF),
    VIOLET(0xFFEE82EE),
    MAUVE(0xFFE0B0FF),

    // Browns
    BROWN(0xFFA52A2A),
    SADDLE_BROWN(0xFF8B4513),
    SIENNA(0xFFA0522D),
    CHOCOLATE(0xFFD2691E),
    BEIGE(0xFFF5F5DC),
    TAN(0xFFD2B48C),
    COPPER(0xFFB87333),
    BRONZE(0xFFCD7F32),

    // Grays
    BLACK(0xFF000000),
    DIM_GRAY(0xFF696969),
    GRAY(0xFF808080),
    DARK_GRAY(0xFFA9A9A9),
    SILVER(0xFFC0C0C0),
    LIGHT_GRAY(0xFFD3D3D3),
    WHITE(0xFFFFFFFF),

    TRANSPARENT(0x00000000);

    private final int color;

    MineGUIColorPalette(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getHexString() {
        return String.format("#%08X", color);
    }

    public int[] getRGB() {
        return new int[]{
                (color >> 16) & 0xFF, // Red
                (color >> 8) & 0xFF,  // Green
                color & 0xFF          // Blue
        };
    }

    public int getAlpha() {
        return (color >> 24) & 0xFF;
    }

    public boolean isTransparent() {
        return this == TRANSPARENT;
    }

    public static MineGUIColorPalette fromHex(int hex) {
        for (MineGUIColorPalette c : values()) {
            if (c.color == hex) {
                return c;
            }
        }
        throw new IllegalArgumentException("Цвет не найден: " + Integer.toHexString(hex));
    }

    public static MineGUIColorPalette closestMatch(int hex) {
        MineGUIColorPalette closest = null;
        int minDistance = Integer.MAX_VALUE;

        int r1 = (hex >> 16) & 0xFF;
        int g1 = (hex >> 8) & 0xFF;
        int b1 = hex & 0xFF;

        for (MineGUIColorPalette c : values()) {
            int r2 = (c.color >> 16) & 0xFF;
            int g2 = (c.color >> 8) & 0xFF;
            int b2 = c.color & 0xFF;

            int distance = (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);

            if (distance < minDistance) {
                minDistance = distance;
                closest = c;
            }
        }
        return closest;
    }

}
