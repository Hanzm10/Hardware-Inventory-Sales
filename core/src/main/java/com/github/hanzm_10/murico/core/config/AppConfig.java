/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.core.config;

import java.awt.Color;
import java.util.Properties;
import com.github.hanzm_10.murico.core.constants.Constants;
import com.github.hanzm_10.murico.core.constants.PropertyKey;
import com.github.hanzm_10.murico.io.FileIO;

public class AppConfig {
    private static final Properties properties;
    public static final String APP_TITLE;
    public static final String APP_VERSION;
    public static final int PREFERRED_WIDTH;
    public static final int PREFERRED_HEIGHT;
    public static final Color COLOR_FOREGROUND;
    public static final Color COLOR_FOREGROUND_DARK;
    public static final Color COLOR_BACKGROUND;
    public static final Color COLOR_BACKGROUND_DARK;
    public static final Color COLOR_PRIMARY;
    public static final Color COLOR_PRIMARY_DARK;
    public static final Color COLOR_PRIMARY_FOREGROUND;
    public static final Color COLOR_PRIMARY_FOREGROUND_DARK;
    public static final Color COLOR_SECONDARY;
    public static final Color COLOR_SECONDARY_DARK;
    public static final Color COLOR_SECONDARY_FOREGROUND;
    public static final Color COLOR_SECONDARY_FOREGROUND_DARK;
    public static final Color COLOR_BORDER;
    public static final Color COLOR_PLACEHOLDER;

    static {
        properties = FileIO.loadProperties(AppConfig.class, Constants.CONFIG_FILE_NAME, "/");

        APP_TITLE = properties.getProperty(PropertyKey.Murico.APP_TITLE);
        APP_VERSION = properties.getProperty(PropertyKey.Murico.APP_VERSION);
        PREFERRED_WIDTH = Integer.parseInt(properties.getProperty(PropertyKey.Murico.PREFERRED_WIDTH));
        PREFERRED_HEIGHT = Integer.parseInt(properties.getProperty(PropertyKey.Murico.PREFERRED_HEIGHT));
        COLOR_FOREGROUND =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_FOREGROUND));
        COLOR_FOREGROUND_DARK =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_FOREGROUND_DARK));
        COLOR_BACKGROUND =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_BACKGROUND));
        COLOR_BACKGROUND_DARK =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_BACKGROUND_DARK));
        COLOR_PRIMARY = Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_PRIMARY));
        COLOR_PRIMARY_DARK =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_PRIMARY_DARK));
        COLOR_PRIMARY_FOREGROUND =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_PRIMARY_FOREGROUND));
        COLOR_PRIMARY_FOREGROUND_DARK = Color
                .decode(properties.getProperty(PropertyKey.Murico.COLOR_PRIMARY_FOREGROUND_DARK));
        COLOR_SECONDARY = Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_SECONDARY));
        COLOR_SECONDARY_DARK =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_SECONDARY_DARK));
        COLOR_SECONDARY_FOREGROUND =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_SECONDARY_FOREGROUND));
        COLOR_SECONDARY_FOREGROUND_DARK = Color
                .decode(properties.getProperty(PropertyKey.Murico.COLOR_SECONDARY_FOREGROUND_DARK));
        COLOR_BORDER = Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_BORDER));
        COLOR_PLACEHOLDER =
                Color.decode(properties.getProperty(PropertyKey.Murico.COLOR_PLACEHOLDER));
    }
}
