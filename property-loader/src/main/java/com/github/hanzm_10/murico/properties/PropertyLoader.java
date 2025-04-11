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
package com.github.hanzm_10.murico.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.utils.LogUtils;

/** Loads .properties files */
public class PropertyLoader {
	public enum LoadMode {
		STRICT, ALLOW_MISSING;
	}

	public enum LoadType {
		/** Loads the properties file from the classpath */
		CLASS_PATH,
		/** Loads the properties file from the file system */
		FILE_SYSTEM;
	}

	private static final String REFERENCE_PREFIX = "%";

	private static final Logger LOGGER = LogUtils.getLogger(PropertyLoader.class);

	public static String getReferencePrefix() {
		return String.valueOf(REFERENCE_PREFIX);
	}

	public static Properties loadProperties(final Class<?> clazz, final String name) {
		return loadProperties(clazz, name, "", LoadMode.STRICT, LoadType.CLASS_PATH);
	}

	public static Properties loadProperties(final Class<?> clazz, final String name, final LoadMode mode) {
		return loadProperties(clazz, name, "", mode, LoadType.CLASS_PATH);
	}

	public static Properties loadProperties(final Class<?> clazz, final String name, final String path) {
		return loadProperties(clazz, name, path, LoadMode.STRICT, LoadType.CLASS_PATH);
	}

	public static Properties loadProperties(final Class<?> clazz, final String name, final String path,
			final LoadMode mode, final LoadType type) {
		System.out.println(clazz.getName());
		final var properties = new Properties();
		var p = path + name + ".properties";

		switch (type) {
			case CLASS_PATH : {
				try (var inputStream = clazz.getResourceAsStream(p)) {
					properties.load(inputStream);
				} catch (final Exception e) {
					if (mode == LoadMode.STRICT) {
						LOGGER.log(Level.SEVERE, "Could not load properties file: " + p + " " + e.getMessage(), e);
					}
				}
			}
				break;
			case FILE_SYSTEM : {
				try (var fileStream = new FileInputStream(new File(p))) {
					properties.load(fileStream);
				} catch (final Exception e) {
					if (mode == LoadMode.STRICT) {
						LOGGER.log(Level.SEVERE, "Could not load properties file: " + p + " " + e.getMessage(), e);
					}
				}
			}
				break;
		}

		return properties;
	}
}
