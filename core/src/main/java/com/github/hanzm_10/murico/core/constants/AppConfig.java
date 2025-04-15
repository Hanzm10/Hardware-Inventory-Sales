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
package com.github.hanzm_10.murico.core.constants;

import com.github.hanzm_10.murico.core.common.ReadonlyProperties;
import com.github.hanzm_10.murico.properties.PropertyLoader;

/** Read-only configuration class for application settings. */
public class AppConfig extends ReadonlyProperties {
    private static final long serialVersionUID = 1L;

    private static AppConfig instance;
    public static final String CONFIG_FILE = "/murico";
    public static final String KEY_APP_TITLE = "app.title";
    public static final String KEY_APP_VERSION = "app.version";
    public static final String KEY_PREFERRED_WIDTH = "app.preferred.width";
    public static final String KEY_PREFERRED_HEIGHT = "app.preferred.height";

    // Method to get the singleton instance
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }

        return instance;
    }

    private AppConfig() {
        super();
        PropertyLoader.loadProperties(this, AppConfig.class, CONFIG_FILE);
    }

    /**
     * Get the application title.
     *
     * @return The application title.
     */
    public String getAppTitle() {
        return getProperty(KEY_APP_TITLE);
    }

    /**
     * Get the application version.
     *
     * @return The application version.
     */
    public String getAppVersion() {
        return getProperty(KEY_APP_VERSION);
    }
}
