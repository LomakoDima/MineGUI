package ru.dimalab.minegui.utils;

public enum MouseHelper {
    LEFT(0),
    RIGHT(1),
    MIDDLE(2),
    UNKNOWN(-1);

    private final int code;

    MouseHelper(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MouseHelper from(int button) {
        for (MouseHelper mb : values()) {
            if (mb.code == button) {
                return mb;
            }
        }
        return UNKNOWN;
    }

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isRight() {
        return this == RIGHT;
    }

    public boolean isMiddle() {
        return this == MIDDLE;
    }
}
