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
package com.github.hanzm_10.murico.os;

import java.util.Locale;

public final class OS {
	public static final String USER_HOME = System.getProperty("user.home");
	public static final String OS_NAME = System.getProperty("os.name");
	public static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
	public static final String JAVA_HOME = System.getProperty("java.home");

	public static final boolean isWindows;
	public static final boolean isOS2;
	public static final boolean isMac;
	public static final boolean isLinux;
	public static final boolean isUnix;

	public static final boolean isFileSystemCaseSensitive;

	static {
		var _OS_NAME = OS_NAME.toLowerCase(Locale.ENGLISH);

		isWindows = _OS_NAME.startsWith("windows");
		isOS2 = _OS_NAME.startsWith("os/2") || _OS_NAME.startsWith("os2");
		isMac = _OS_NAME.startsWith("mac");
		isLinux = _OS_NAME.startsWith("linux");
		isUnix = !isWindows && !isOS2;
		isFileSystemCaseSensitive = isUnix && !isMac;
	}

	private OS() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
