package com.spixsoftware.spixlibrary.main.utils;

public class ColorUtils {

    public static String intColorToHex(int intColor) {
        return String.format("#%06X", 0xFFFFFF & intColor);
    }
}
