package com.github.hanzm_10.murico.swingapp.constants;

import java.io.File;
import com.github.hanzm_10.murico.swingapp.lib.platform.SystemInfo;

public final class Directories {
    public static final String LOGS_DIRECTORY;

    public static final String APP_DATA_DIRECTORY;
    public static final String ROAMING_DIRECTORY;
    public static final String APP_DIRECTORY;

    static {
        if (SystemInfo.isWindows) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "AppData";
            ROAMING_DIRECTORY = APP_DATA_DIRECTORY + File.separator + "Roaming";
        } else if (SystemInfo.isMac) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "Library";
            ROAMING_DIRECTORY = APP_DATA_DIRECTORY + File.separator + "Application Support";
        } else if (SystemInfo.isLinux) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME;
            ROAMING_DIRECTORY = APP_DATA_DIRECTORY + File.separator + ".config";
        } else {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "etc";
            ROAMING_DIRECTORY = APP_DATA_DIRECTORY;
        }

        APP_DIRECTORY = ROAMING_DIRECTORY + File.separator + Metadata.APP_TITLE;
        LOGS_DIRECTORY = APP_DIRECTORY + File.separator + "logs";
    }

    private Directories() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
