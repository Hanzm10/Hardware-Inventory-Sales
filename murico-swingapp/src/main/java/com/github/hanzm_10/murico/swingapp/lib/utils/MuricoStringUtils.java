package com.github.hanzm_10.murico.swingapp.lib.utils;

import java.util.regex.Pattern;

public class MuricoStringUtils {
    public static boolean containsIgnoreCase(final String str, final String pattern) {
        return Pattern.compile(pattern, Pattern.LITERAL | Pattern.CASE_INSENSITIVE).matcher(str)
                .find();
    }

    public static String repeat(final String s, final int count) {
        if (count < 0) {
            return "";
        }

        if (count == 1) {
            return s;
        }

        var stringBuilder = new StringBuilder(s.length() * count);
        stringBuilder.append(s.repeat(count));

        return stringBuilder.toString();
    }

    private MuricoStringUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
}