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

import java.io.File;
import com.github.hanzm_10.murico.core.config.AppConfig;
import com.github.hanzm_10.murico.os.OS;

public class Directories {
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String LOGS_DIRECTORY;
    public static final String CONFIG_DIRECTORY;

    /** Does not include the file extension. */
    public static final String GLOBAL_CONFIG_FILE_PATH;

    public static final String APP_DATA;

    static {
        if (OS.isWindows) {
            APP_DATA = USER_HOME + File.separator + "AppData" + File.separator + "Local" + File.separator
                    + AppConfig.APP_TITLE;
        } else if (OS.isMac) {
            APP_DATA = USER_HOME + File.separator + "Library" + File.separator + "Application Support" + File.separator
                    + AppConfig.APP_TITLE;
        } else if (OS.isLinux) {
            APP_DATA = USER_HOME + File.separator + "." + AppConfig.APP_TITLE;
        } else {
            APP_DATA = USER_HOME + File.separator + "etc" + File.separator + AppConfig.APP_TITLE;
        }

        CONFIG_DIRECTORY = APP_DATA + File.separator + "config";
        LOGS_DIRECTORY = APP_DATA + File.separator + "logs";
        GLOBAL_CONFIG_FILE_PATH = CONFIG_DIRECTORY + File.separator + Constants.CONFIG_FILE_NAME;
    }
}
