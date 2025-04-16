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
package com.github.hanzm_10.murico.io;

import java.io.File;
import java.util.logging.Logger;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;
import com.github.weisj.darklaf.platform.SystemInfo;

/**
 * Configuration class for Murico application.
 *
 * <p>
 * This class provides constants for the application's configuration directory
 * and app data directory based on the operating system.
 */
public class MuricoConfiguration {
    private static final Logger LOGGER = MuricoLogUtils.getLogger(MuricoConfiguration.class);
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String DIRECTORY_NAME = "Murico";
    public static final String LOGS_DIRECTORY;
    public static final String CONFIG_DIRECTORY;
    public static final String GLOBAL_CONFIG_FILE_PATH;
    public static final String GLOBAL_CONFIG_FILE_NAME = "murico";
    public static final String APP_DATA;

    static {
        if (SystemInfo.isWindows) {
            APP_DATA = USER_HOME + File.separator + "AppData" + File.separator + "Local"
                    + File.separator
                    + DIRECTORY_NAME;
        } else if (SystemInfo.isMac) {
            APP_DATA =
                    USER_HOME + File.separator + "Library" + File.separator + "Application Support"
                    + File.separator + DIRECTORY_NAME;
        } else if (SystemInfo.isLinux) {
            APP_DATA = USER_HOME + File.separator + "." + DIRECTORY_NAME;
        } else {
            APP_DATA = USER_HOME + File.separator + "etc" + File.separator + DIRECTORY_NAME;
        }

        CONFIG_DIRECTORY = APP_DATA + File.separator + "config";
        LOGS_DIRECTORY = APP_DATA + File.separator + "logs";
        GLOBAL_CONFIG_FILE_PATH = CONFIG_DIRECTORY + File.separator + GLOBAL_CONFIG_FILE_NAME;
    }

    /** Creates the configuration directory if they do not exist. */
    public static void createConfigDirectory() {
        LOGGER.info("Creating configuration directory...");
        var configDir = new File(CONFIG_DIRECTORY);

        if (!configDir.exists()) {
            configDir.mkdirs();
            LOGGER.info("Configuration directory created: " + configDir.getAbsolutePath());
        } else {
            LOGGER.info("Configuration directory already exists: " + configDir.getAbsolutePath());
        }
    }

    /** Creates the logs directory if it does not exist. */
    public static void createLogsDirectory() {
        LOGGER.info("Creating logs directory...");
        var logsDir = new File(LOGS_DIRECTORY);

        if (!logsDir.exists()) {
            logsDir.mkdirs();
            LOGGER.info("Logs directory created: " + logsDir.getAbsolutePath());
        } else {
            LOGGER.info("Logs directory already exists: " + logsDir.getAbsolutePath());
        }
    }
}
