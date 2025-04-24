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
package com.github.hanzm_10.murico.swingapp.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.github.hanzm_10.murico.swingapp.lib.io.FileLoadLeniency;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public abstract class AbstractFileSystemConfigProperties extends AbstractFileSystemConfig {
	protected final Properties properties = new Properties();

	public AbstractFileSystemConfigProperties() {
		super();

		try {
			PropertiesIO.loadPropertiesFromFileSystem(getFullPath(), properties,
					FileLoadLeniency.CREATE_FILE_IF_MISSING);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to load properties file: " + getFileName(), e);
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.WARNING, "getFileName() or getFilePath() returned null or empty string.", e);
		}
	}

	/**
	 * Removes key-value pairs from the properties file. The operations are as
	 * follows:
	 *
	 * <ol>
	 * <li>For each key in the input array, remove the key-value pair from the
	 * properties object in memory.
	 * <li>Save the properties object to the file system.
	 * <li>Return the previous values of the keys that were removed.
	 * </ol>
	 *
	 * <strong>Note:</strong>
	 *
	 * <p>
	 * If an operation fails, the properties object in memory will be reverted to
	 * its original state before the operation was performed, and no changes will be
	 * made to the properties file.
	 *
	 * @param keys
	 * @return The previous values of the keys that were removed. Their order is the
	 *         same as the order of the keys in the input array.
	 * @throws FileNotFoundException
	 *             Error thrown by
	 *             {@link PropertiesIO#savePropertiesInFileSystem(Properties, String, String)
	 *             PropertiesIO.savePropertiesInFileSystem}
	 * @throws IOException
	 *             Error thrown by
	 *             {@link PropertiesIO#savePropertiesInFileSystem(Properties, String, String)
	 *             PropertiesIO.savePropertiesInFileSystem}
	 */
	public final synchronized Object[] bulkRemove(Object[] keys) throws FileNotFoundException, IOException {
		var prevVals = new Object[keys.length];

		for (var i = 0; i < keys.length; i++) {
			prevVals[i] = properties.remove(keys[i]);
		}

		try {
			PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to save properties after bulk remove. Reverting changes.", e);

			for (var i = 0; i < keys.length; i++) {
				properties.setProperty((String) keys[i], (String) prevVals[i]);
			}

			throw e;
		}

		return prevVals;
	}

	@Override
	public final String getFileExtension() {
		return ".properties";
	}

	public final synchronized String getProperty(final String key) {
		return properties.getProperty(key);
	}

	public final synchronized String getProperty(final String key, final String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Removes a key-value pair from the properties file. The operations are as
	 * follows:
	 *
	 * <ol>
	 * <li>Remove the key-value pair from the properties object in memory.
	 * <li>Save the properties object to the file system.
	 * <li>Return the previous value of the key that was removed.
	 * </ol>
	 *
	 * <strong>Note:</strong>
	 *
	 * <p>
	 * If an operation fails, the properties object in memory will be reverted to
	 * its original state before the operation was performed, and no changes will be
	 * made to the properties file.
	 *
	 * @param key
	 * @return The previous value of the key that was removed. If the key did not
	 *         exist, null is returned.
	 * @throws FileNotFoundException
	 *             Error thrown by
	 *             {@link PropertiesIO#savePropertiesInFileSystem(Properties, String, String)
	 *             PropertiesIO.savePropertiesInFileSystem}
	 * @throws IOException
	 *             Error thrown by
	 *             {@link PropertiesIO#savePropertiesInFileSystem(Properties, String, String)
	 *             PropertiesIO.savePropertiesInFileSystem}
	 */
	public final synchronized Object remove(Object key) throws FileNotFoundException, IOException {
		var prevVal = properties.remove(key);

		try {
			PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to save properties after remove. Reverting changes.", e);
			properties.setProperty((String) key, (String) prevVal);
			throw e;
		}

		return prevVal;
	}

	/**
	 * Sets a key-value pair in the properties file. The operations are as follows:
	 *
	 * <ol>
	 * <li>Set the key-value pair in the properties object in memory.
	 * <li>Save the properties object to the file system.
	 * <li>Return the previous value of the key that was set. If the key did not
	 * exist, null is returned.
	 * </ol>
	 *
	 * <strong>Note:</strong>
	 *
	 * <p>
	 * If an operation fails, the properties object in memory will be reverted to
	 * its original state before the operation was performed, and no changes will be
	 * made to the properties file.
	 *
	 * @param key
	 * @param value
	 * @return The previous value of the key that was set. If the key did not exist,
	 *         null is returned.
	 * @throws FileNotFoundException
	 *             Error thrown by
	 *             {@link PropertiesIO#savePropertiesInFileSystem(Properties, String, String)
	 *             PropertiesIO.savePropertiesInFileSystem}
	 * @throws IOException
	 *             Error thrown by
	 *             {@link PropertiesIO#savePropertiesInFileSystem(Properties, String, String)
	 *             PropertiesIO.savePropertiesInFileSystem}
	 */
	public final synchronized Object setProperty(String key, String value) throws FileNotFoundException, IOException {
		var prevVal = properties.setProperty(key, value);

		try {
			PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to save properties after set property. Reverting changes.", e);
			properties.setProperty(key, (String) prevVal);
			throw e;
		}

		return prevVal;
	}
}
