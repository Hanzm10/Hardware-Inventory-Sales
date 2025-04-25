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

import java.io.File;
import com.github.hanzm_10.murico.swingapp.lib.platform.SystemInfo;

public final class Directories {
    public static final String LOGS_DIRECTORY;
    public static final String CONFIG_DIRECTORY;

    public static final String APP_DATA_DIRECTORY;
    /**
     * The directory where the application is installed. This is the directory where the application
     * stores its data.
     */
    public static final String APP_DIRECTORY;

    static {
        if (SystemInfo.isWindows) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "AppData";
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + "Local" + File.separator
                    + Metadata.APP_TITLE;
        } else if (SystemInfo.isLinux || SystemInfo.isMac) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME;
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + "." + Metadata.APP_TITLE;
        } else {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "etc";
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + Metadata.APP_TITLE;
        }

        LOGS_DIRECTORY = APP_DIRECTORY + File.separator + "logs";
        CONFIG_DIRECTORY = APP_DIRECTORY + File.separator + "config";
    }

    private Directories() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
