package com.dev.nicehash.enums;

/**
 * Created by agladkov on 17.06.18.
 * Contains themes for application, combined with dark and light
 */
public enum Theme {
    DarkOrange(0, 2), LightOrange(1, 2), DarkBlue(2, 0), LightBlue(3, 0), DarkGreen(4, 1), LightGreen(5, 1),
    DarkRed(6, 3), LightRed(7, 3), DarkPurple(8, 4), LightPurple(9, 4);

    private final int value;
    private final int color;

    Theme(int value, int color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }
}
