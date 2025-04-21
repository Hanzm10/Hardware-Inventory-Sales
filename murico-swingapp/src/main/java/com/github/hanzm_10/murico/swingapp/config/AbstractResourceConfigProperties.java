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
