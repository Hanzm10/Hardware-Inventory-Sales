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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.github.hanzm_10.murico.utils.MuricoLogUtils;

/**
 * FileIO.java
 *
 * <p>
 * This class provides methods for File Input/Output operations.
 */
public final class FileIO {
	public static final Logger LOGGER = MuricoLogUtils.getLogger(FileIO.class);

	public static final void createDirectoryIfNotExists(final String directoryPath) {
		var directory = new File(directoryPath);
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				LOGGER.info("Directory created: " + directoryPath);
			} else {
				LOGGER.severe("Failed to create directory: " + directoryPath);
			}
		}
	}

	public static void createFileIfNotExists(final String filePath) {
		var file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LOGGER.severe("Failed to create file: " + e.getMessage());
			}
		}
	}

	public static void loadProperties(final Properties properties, final String filePath) {
		loadProperties(properties, filePath, false);
	}

	public static void loadProperties(final Properties properties, final String filePath,
			final boolean createFileIfNotExists) {
		var filePathWithExtension = filePath + ".properties";
		if (createFileIfNotExists) {
			FileIO.createFileIfNotExists(filePathWithExtension);
		}

		try (var inputStream = new FileInputStream(filePathWithExtension)) {
			properties.load(inputStream);
		} catch (IOException e) {
			LOGGER.severe("Failed to load properties from file: " + e.getMessage());
		}
	}

	public static void saveProperties(final Properties properties, final String filePath) {
		saveProperties(properties, filePath, null);
	}

	public static void saveProperties(final Properties properties, final String filePath, final String comments) {
		try (var outputStream = new FileOutputStream(filePath + ".properties")) {
			properties.store(outputStream, comments);
		} catch (IOException e) {
			LOGGER.severe("Failed to save properties to file: " + e.getMessage());
		}
	}

	private FileIO() {
		throw new UnsupportedOperationException("Utility class");
	}
}
