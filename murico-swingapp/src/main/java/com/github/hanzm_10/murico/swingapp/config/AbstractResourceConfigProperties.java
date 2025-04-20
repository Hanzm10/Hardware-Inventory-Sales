package com.github.hanzm_10.murico.swingapp.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import com.github.hanzm_10.murico.swingapp.lib.io.FileLoadLeniency;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public abstract class AbstractResourceConfigProperties extends AbstractResourceConfig {
    protected Properties properties;

    public AbstractResourceConfigProperties() {
        super();

        try {
            properties = PropertiesIO.loadProperties(getResourceClass(), getFileName(),
                    getFilePath(), FileLoadLeniency.MANDATORY);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load properties file: " + getFileName(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING,
                    "getFileName() or getFilePath() returned null or empty string.", e);
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
            PropertiesIO.saveProperties(getResourceClass(), properties, getFileName(),
                    getFilePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after bulk remove. Reverting changes.", e);
            properties = oldProperties;
            throw e;
        }

        return prevVals;
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
            PropertiesIO.saveProperties(getResourceClass(), properties, getFileName(),
                    getFilePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after remove. Reverting changes.", e);
            properties.setProperty((String) key, (String) prevVal);
            throw e;
        }

        return prevVal;
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
            PropertiesIO.saveProperties(getResourceClass(), properties, getFileName(),
                    getFilePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after set property. Reverting changes.", e);
            properties.setProperty(key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }
}
