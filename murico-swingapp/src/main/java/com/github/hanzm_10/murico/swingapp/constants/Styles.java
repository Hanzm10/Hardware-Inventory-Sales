/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.constants;

import java.awt.Color;
import java.util.Properties;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class Styles {
    private static final Logger LOGGER = MuricoLogger.getLogger(Styles.class);

    public static Color PRIMARY_COLOR;
    public static Color PRIMARY_FOREGROUND_COLOR;
    public static Color PRIMARY_COLOR_DARK;
    public static Color PRIMARY_FOREGROUND_COLOR_DARK;
    public static Color SECONDARY_COLOR;
    public static Color SECONDARY_FOREGROUND_COLOR;
    public static Color SECONDARY_COLOR_DARK;
    public static Color SECONDARY_FOREGROUND_COLOR_DARK;
    public static Color TERTIARY_COLOR;
    public static Color TERTIARY_FOREGROUND_COLOR;
    public static Color TERTIARY_COLOR_DARK;
    public static Color TERTIARY_FOREGROUND_COLOR_DARK;
    public static Color CONFIRM_COLOR;
    public static Color CONFIRM_FOREGROUND_COLOR;
    public static Color CONFIRM_COLOR_DARK;
    public static Color CONFIRM_FOREGROUND_COLOR_DARK;
    public static Color DANGER_COLOR;
    public static Color DANGER_FOREGROUND_COLOR;
    public static Color DANGER_COLOR_DARK;
    public static Color DANGER_FOREGROUND_COLOR_DARK;
    public static Color WARNING_COLOR;
    public static Color WARNING_FOREGROUND_COLOR;
    public static Color WARNING_COLOR_DARK;
    public static Color WARNING_FOREGROUND_COLOR_DARK;
    public static Color SUCCESS_COLOR;
    public static Color SUCCESS_FOREGROUND_COLOR;
    public static Color SUCCESS_COLOR_DARK;
    public static Color SUCCESS_FOREGROUND_COLOR_DARK;

    static {
        var properties = new Properties();

        try {
            PropertiesIO.loadProperties(Styles.class, properties, "styles");
        } catch (Exception e) {
            LOGGER.warning("Failed to load styles.properties: " + e.getMessage());
        }

        PRIMARY_COLOR = Color.decode(properties.getProperty("primary.color"));
        PRIMARY_FOREGROUND_COLOR = Color.decode(properties.getProperty("primary.foreground.color"));
        PRIMARY_COLOR_DARK = Color.decode(properties.getProperty("primary.color.dark"));
        PRIMARY_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("primary.foreground.color.dark"));

        SECONDARY_COLOR = Color.decode(properties.getProperty("secondary.color"));
        SECONDARY_FOREGROUND_COLOR = Color.decode(properties.getProperty("secondary.foreground.color"));
        SECONDARY_COLOR_DARK = Color.decode(properties.getProperty("secondary.color.dark"));
        SECONDARY_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("secondary.foreground.color.dark"));

        TERTIARY_COLOR = Color.decode(properties.getProperty("tertiary.color"));
        TERTIARY_FOREGROUND_COLOR = Color.decode(properties.getProperty("tertiary.foreground.color"));
        TERTIARY_COLOR_DARK = Color.decode(properties.getProperty("tertiary.color.dark"));
        TERTIARY_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("tertiary.foreground.color.dark"));

        CONFIRM_COLOR = Color.decode(properties.getProperty("confirm.color"));
        CONFIRM_FOREGROUND_COLOR = Color.decode(properties.getProperty("confirm.foreground.color"));
        CONFIRM_COLOR_DARK = Color.decode(properties.getProperty("confirm.color.dark"));
        CONFIRM_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("confirm.foreground.color.dark"));

        DANGER_COLOR = Color.decode(properties.getProperty("danger.color"));
        DANGER_FOREGROUND_COLOR = Color.decode(properties.getProperty("danger.foreground.color"));
        DANGER_COLOR_DARK = Color.decode(properties.getProperty("danger.color.dark"));
        DANGER_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("danger.foreground.color.dark"));

        WARNING_COLOR = Color.decode(properties.getProperty("warning.color"));
        WARNING_FOREGROUND_COLOR = Color.decode(properties.getProperty("warning.foreground.color"));
        WARNING_COLOR_DARK = Color.decode(properties.getProperty("warning.color.dark"));
        WARNING_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("warning.foreground.color.dark"));

        SUCCESS_COLOR = Color.decode(properties.getProperty("success.color"));
        SUCCESS_FOREGROUND_COLOR = Color.decode(properties.getProperty("success.foreground.color"));
        SUCCESS_COLOR_DARK = Color.decode(properties.getProperty("success.color.dark"));
        SUCCESS_FOREGROUND_COLOR_DARK = Color.decode(properties.getProperty("success.foreground.color.dark"));

    }
}
