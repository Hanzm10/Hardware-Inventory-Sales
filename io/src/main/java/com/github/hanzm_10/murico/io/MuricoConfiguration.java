package com.github.hanzm_10.murico.io;

import java.io.File;
import com.github.hanzm_10.murico.platform.SystemInfo;

/**
 * Configuration class for Murico application.
 * <p>
 * This class provides constants for the application's configuration directory and app data
 * directory based on the operating system.
 * </p>
 */
public class MuricoConfiguration {
    public static final String DIRECTORY_NAME = "Murico";
    public static final String LOGS_DIRECTORY;
    public static final String CONFIG_DIRECTORY;
    public static final String APP_DATA;

    static {
        if (SystemInfo.IS_WINDOWS) {
            APP_DATA = SystemInfo.USER_HOME + File.separator + "AppData" + File.separator + "Local"
                    + File.separator + DIRECTORY_NAME;
        } else if (SystemInfo.IS_MAC) {
            APP_DATA = SystemInfo.USER_HOME + File.separator + "Library" + File.separator
                    + "Application Support" + File.separator + DIRECTORY_NAME;
        } else if (SystemInfo.IS_LINUX) {
            APP_DATA = SystemInfo.USER_HOME + File.separator + "." + DIRECTORY_NAME;
        } else {
            APP_DATA =
                    SystemInfo.USER_HOME + File.separator + "etc" + File.separator + DIRECTORY_NAME;
        }

        CONFIG_DIRECTORY = APP_DATA + File.separator + "config";
        LOGS_DIRECTORY = APP_DATA + File.separator + "logs";
    }
}
