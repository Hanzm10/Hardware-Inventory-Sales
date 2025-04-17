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

import java.util.Properties;

import com.github.hanzm_10.murico.core.constants.Constants;
import com.github.hanzm_10.murico.core.constants.PropertyKey;

import com.github.weisj.darklaf.properties.PropertyLoader;

public class AppConfig {
	private static final Properties properties;
	public static final String APP_TITLE;
	public static final String APP_VERSION;
	public static final int PREFERRED_WIDTH;
	public static final int PREFERRED_HEIGHT;

	static {
		properties = PropertyLoader.loadProperties(AppConfig.class, Constants.CONFIG_FILE_NAME, "/");

		APP_TITLE = properties.getProperty(PropertyKey.Murico.APP_TITLE);
		APP_VERSION = properties.getProperty(PropertyKey.Murico.APP_VERSION);
		PREFERRED_WIDTH = Integer.parseInt(properties.getProperty(PropertyKey.Murico.PREFERRED_WIDTH));
		PREFERRED_HEIGHT = Integer.parseInt(properties.getProperty(PropertyKey.Murico.PREFERRED_HEIGHT));
	}
}
