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
package com.github.hanzm_10.murico.swingapp.config;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.github.hanzm_10.murico.swingapp.lib.io.FileLoadLeniency;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public abstract class AbstractResourceConfigProperties extends AbstractResourceConfig {
	protected Properties properties;

	public AbstractResourceConfigProperties() {
		super();

		var classNameString = getResourceClass().getName();

		LOGGER.config("Initializing configuration in resource for path: " + getFileDirectory() + getFileName()
				+ " for class: " + classNameString);

		try {
			properties = PropertiesIO.loadProperties(getResourceClass(), getFileName(), getFileDirectory(),
					FileLoadLeniency.MANDATORY);

			LOGGER.config("Successfully loaded configuration from resource for path: " + getFileDirectory()
					+ getFileName() + " for class: " + classNameString);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to load properties file: " + getFileName(), e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, "getFileName() or getFilePath() returned null or empty string.", e);
		}
	}

	@Override
	public final String getFileExtension() {
		return ".properties";
	}

	/**
	 * Returns the value of the property with the specified key in this
	 * configuration.
	 *
	 * @param key
	 * @return The value of the property with the specified key, or null if the key
	 *         is not found.
	 */
	public final synchronized String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * Returns the value of the property with the specified key in this
	 * configuration. If the key is not found, the default value is returned.
	 *
	 * @param key
	 * @param defaultValue
	 * @return The value of the property with the specified key, or the default
	 *         value if the key is not found.
	 */
	public final synchronized String getProperty(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
