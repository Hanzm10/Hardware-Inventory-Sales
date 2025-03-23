package utils;

import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private static final Properties properties = new Properties();
    private Settings() {}

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
    }

    static {
        InputStream is = Settings.class.getClassLoader().getResourceAsStream("config.properties");

        assert is != null;

        try {
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
