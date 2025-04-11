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
package com.github.hanzm_10.murico.platform;

import java.util.Locale;
import com.github.hanzm_10.murico.utils.StringUtils;

/**
 * SystemInfo class provides information about the operating system. It retrieves the OS name using
 * the System.getProperty method. This class is part of the com.github.hanzm_10.murico.platform
 * package.
 * 
 * **Note**: This is prone to tampering. It is recommended to use a more secure method for higher
 * security applications.
 * 
 */
public class SystemInfo {
    /**
     * The name of the operating system. This is retrieved using the System.getProperty method with
     * the key "os.name". The value is as-is.
     */
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION =
            System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String JAVA_HOME = System.getProperty("java.home");

    public static final boolean IS_WINDOWS;
    public static final boolean IS_OS2;
    public static final boolean IS_MAC;
    public static final boolean IS_LINUX;
    public static final boolean IS_UNIX;

    public static final boolean IS_FILESYSTEM_CASE_SENSITIVE;

    public static final boolean IS_APPLE_JVM;
    public static final boolean IS_ORACLE_JVM;
    public static final boolean IS_SUN_JVM;

    static {
        var _OS_NAME = OS_NAME.toLowerCase(Locale.ENGLISH);

        IS_WINDOWS = _OS_NAME.startsWith("windows");
        IS_OS2 = _OS_NAME.startsWith("os/2") || _OS_NAME.startsWith("os2");
        IS_MAC = _OS_NAME.startsWith("mac");
        IS_LINUX = _OS_NAME.startsWith("linux");
        IS_UNIX = !IS_WINDOWS && !IS_OS2;
        IS_FILESYSTEM_CASE_SENSITIVE = IS_UNIX && !IS_MAC;
        IS_APPLE_JVM = isAppleJVM();
        IS_ORACLE_JVM = isOracleJVM();
        IS_SUN_JVM = isSunJVM();
    }

    public static String getJavaJvmVendor() {
        return System.getProperty("java.vm.vendor");
    }

    public static boolean isAppleJVM() {
        var vendor = getJavaJvmVendor();
        return vendor != null && StringUtils.containsIgnoreCase(vendor, "Apple");
    }

    public static boolean isOracleJVM() {
        var vendor = getJavaJvmVendor();
        return vendor != null && StringUtils.containsIgnoreCase(vendor, "Oracle");
    }

    public static boolean isSunJVM() {
        var vendor = getJavaJvmVendor();
        return vendor != null && StringUtils.containsIgnoreCase(vendor, "Sun")
                && StringUtils.containsIgnoreCase(vendor, "Microsystems");
    }
}
