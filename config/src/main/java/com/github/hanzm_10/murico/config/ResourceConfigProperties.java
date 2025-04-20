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
package com.github.hanzm_10.murico.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;

public abstract class ResourceConfigProperties extends ResourceConfig {
    protected Properties properties;

    public ResourceConfigProperties() {
        super();

        properties = new Properties();

        try (var inputStream = getResourceClass().getResourceAsStream(getFilePath() + getFileName())) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                LOGGER.warning("Configuration file not found: " + getFilePath() + getFileName());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load properties from file: " + getFilePath() + getFileName(), e);
        }
    }

    /**
     * Bulk removes properties from this configuration. Saves the updated properties
     * to the configuration file. <br>
     * <br>
     * If saving the properties fails, the {@link #properties} object will remain
     * unchanged.
     *
     * @param keys
     * @return An array of previous values for the removed properties. The order of
     *         the values in the array corresponds to the order of the keys in the
     *         input array.
     * @throws FileNotFoundException
     *             If the configuration file is not found.
     * @throws IOException
     *             If something goes wrong while saving the properties.
     */
    public final synchronized Object[] bulkRemove(Object[] keys) throws FileNotFoundException, IOException {
        var oldProperties = new Properties(properties);

        var prevVals = new Object[keys.length];
        for (var i = 0; i < keys.length; i++) {
            prevVals[i] = properties.remove(keys[i]);
        }

        try {
            saveProperties();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after bulk remove. Reverting changes.", e);
            properties = oldProperties;
            throw e;
        }

        return prevVals;
    }

    @Override
    public final String getFileExtension() {
        return "properties";
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

    /*
     * Removes the property with the specified key from this configuration. Saves
     * the updated properties to the configuration file. <br><br> If saving the
     * properties fails, the {@link #properties} object will remain unchanged.
     *
     * @param key
     *
     * @return The previous value associated with the key, or null if there was
     * none.
     *
     * @throws FileNotFoundException If the configuration file is not found.
     *
     * @throws IOException If something goes wrong while saving the properties.
     */
    public final synchronized Object remove(Object key) throws FileNotFoundException, IOException {
        var prevVal = properties.remove(key);

        try {
            saveProperties();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after remove. Reverting changes.", e);
            properties.setProperty((String) key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }

    protected final synchronized void saveProperties() throws FileNotFoundException, IOException {
        URI uri = null;
        var path = getFilePath() + getFileName();

        try {
            var url = getResourceClass().getResource(path);

            if (url == null) {
                throw new FileNotFoundException("Resource not found: " + path + " in " + getResourceClass());
            }

            uri = url.toURI();
        } catch (URISyntaxException e) {
            LOGGER.severe("Failed to get resource URL: " + e.getMessage());
            return;
        }

        var file = new File(uri);

        try (var outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, getHeaderComment());
        } catch (SecurityException e) {
            LOGGER.severe("Denied access to file: " + file.getAbsolutePath());
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "Configuration file in " + file.getAbsolutePath() + " contains invalid data. ", e);
        }
    }

    /**
     * Sets the property with the specified key to the specified value in this
     * configuration. Saves the updated properties to the configuration file. <br>
     * <br>
     * If saving the properties fails, the {@link #properties} object will remain
     * unchanged.
     *
     * @param key
     * @param value
     * @return The previous value associated with the key, or null if there was
     *         none.
     * @throws FileNotFoundException
     *             If the configuration file is not found.
     * @throws IOException
     *             If something goes wrong while saving the properties.
     */
    public final synchronized Object setProperty(String key, String value) throws FileNotFoundException, IOException {
        var prevVal = properties.setProperty(key, value);

        try {
            saveProperties();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after set property. Reverting changes.", e);
            properties.setProperty(key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }
}
