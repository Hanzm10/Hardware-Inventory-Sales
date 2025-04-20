package com.github.hanzm_10.murico.swingapp.constants;

import java.io.IOException;
import java.util.Properties;
import com.github.hanzm_10.murico.swingapp.lib.io.PropertiesIO;

public final class Metadata {
    private static final Properties properties = new Properties();

    public static final String APP_TITLE;
    public static final String APP_VERSION;

    static {
        try {
            PropertiesIO.loadProperties(Metadata.class, properties, "metadata");
        } catch (IllegalArgumentException e) {
            // DO nothing
        } catch (IOException e) {
            throw new RuntimeException("Failed to load metadata.properties", e);
        }

        APP_TITLE = properties.getProperty(PropertyKey.Metadata.APP_TITLE);
        APP_VERSION = properties.getProperty(PropertyKey.Metadata.APP_VERSION);
    }

    private Metadata() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
