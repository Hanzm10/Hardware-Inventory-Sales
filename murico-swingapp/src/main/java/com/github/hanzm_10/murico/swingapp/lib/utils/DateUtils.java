package com.github.hanzm_10.murico.swingapp.lib.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.NotNull;

public final class DateUtils {

    public enum DateFormat {
        ISO("yyyy-MM-dd'T'HH:mm:ss.SSS"), ISO_WITHOUT_TIME("yyyy-MM-dd"), ISO_TIME(
                "HH:mm:ss"), CUSTOM("yyyy-MM-dd HH:mm:ss");

        private final String format;

        DateFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    public static final String getDateWithFormat() {
        return getDateWithFormat(DateFormat.ISO);
    }

    /**
     * Returns the current date and time in the specified format.
     *
     * @param dateFormat The format to use.
     * @return The current date and time in the specified format.
     */
    public static final String getDateWithFormat(@NotNull DateFormat dateFormat) {
        var now = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern(dateFormat.format);
        var formattedDate = now.format(formatter);

        return formattedDate;
    }
}
