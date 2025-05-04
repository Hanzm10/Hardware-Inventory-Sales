package com.github.hanzm_10.murico.swingapp.lib.utils;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

public class ColorUtils {
    public static Color changeAlpha(@NotNull
        final Color color, float alpha) {
        var newAlpha = (int)(255 * alpha);

        newAlpha = newAlpha > 255 ? 255 : newAlpha < 0 ? 0 : newAlpha;

        var red = color.getRed();
        var green = color.getGreen();
        var blue = color.getBlue();

        return new Color(red, green, blue, newAlpha);
    }
}

