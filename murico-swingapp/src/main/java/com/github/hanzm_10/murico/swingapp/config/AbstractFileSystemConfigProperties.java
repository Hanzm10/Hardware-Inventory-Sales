package com.github.hanzm_10.murico.swingapp.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import com.github.hanzm_10.murico.swingapp.lib.io.FileLoadLeniency;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public abstract class AbstractFileSystemConfigProperties extends AbstractFileSystemConfig {
    protected Properties properties;

    public AbstractFileSystemConfigProperties() {
        super();

        try {
            properties = PropertiesIO.loadPropertiesFromFileSystem(getFullPath(),
                    FileLoadLeniency.CREATE_FILE_IF_MISSING);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load properties file: " + getFileName(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING,
                    "getFileName() or getFilePath() returned null or empty string.", e);
        }
    }

    public final synchronized Object[] bulkRemove(Object[] keys)
            throws FileNotFoundException, IOException {
        var oldProperties = new Properties(properties);

        var prevVals = new Object[keys.length];
        for (var i = 0; i < keys.length; i++) {
            prevVals[i] = properties.remove(keys[i]);
        }

        try {
            PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to save properties after bulk remove. Reverting changes.", e);
            properties = oldProperties;
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

    public final synchronized Object remove(Object key) throws FileNotFoundException, IOException {
        var prevVal = properties.remove(key);

        try {
            PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save properties after remove. Reverting changes.",
                    e);
            properties.setProperty((String) key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }

    public final synchronized Object setProperty(String key, String value)
            throws FileNotFoundException, IOException {
        var prevVal = properties.setProperty(key, value);

        try {
            PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to save properties after set property. Reverting changes.", e);
            properties.setProperty(key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }
}
