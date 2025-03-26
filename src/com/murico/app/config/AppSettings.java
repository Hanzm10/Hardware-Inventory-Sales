package com.murico.app.config;

import java.awt.*;
import java.io.InputStream;
import java.util.Properties;

public class AppSettings {

    private final Properties properties;

    private String appTitle;
    private String appFontFamily;

    private int appMainScreenWidth;
    private int appMainScreenHeight;

    private Color primaryColor;
    private Color primaryForegroundColor;

    private Color secondaryColor;
    private Color secondaryForegroundColor;

    private Font mainFont;
    private Font buttonsFont;

    private static AppSettings instance;

    private AppSettings() {
        this.properties = new Properties();

        this.loadConfiguration();
        this.setConfigDefaults();
    }

    public String getAppTitle() {
        return this.appTitle;
    }

    public int getAppMainScreenWidth() {
        return this.appMainScreenWidth;
    }

    public int getAppMainScreenHeight() {
        return this.appMainScreenHeight;
    }

    public Color getPrimaryColor() {
        return this.primaryColor;
    }

    public Color getPrimaryForegroundColor() {
        return this.primaryForegroundColor;
    }

    public Color getSecondaryColor() {
        return this.secondaryColor;
    }

    public Color getSecondaryForegroundColor() {
        return this.secondaryForegroundColor;
    }

    public String getAppFontFamily() {
        return this.appFontFamily;
    }

    public Font getMainFont() {
        return this.mainFont;
    }

    public Font getButtonsFont() {
        return this.buttonsFont;
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(this.getProperty(key));
    }

    private void setConfigDefaults() {
        this.appTitle = this.getProperty("app.title");

        this.appMainScreenWidth = this.getIntProperty("app.screen.width");
        this.appMainScreenHeight = this.getIntProperty("app.screen.height");
        this.appFontFamily = this.getProperty("app.font.family");

        this.primaryColor = Color.decode(this.getProperty("color.primary" +
                                                                  ".default"));
        this.primaryForegroundColor = Color.decode(this.getProperty("color.primary.foreground"));

        this.secondaryColor = Color.decode(this.getProperty("color.secondary" +
                                                                    ".default"
        ));
        this.secondaryForegroundColor = Color.decode(this.getProperty("color.secondary.foreground"));

        this.mainFont = new Font(this.appFontFamily, Font.PLAIN, 16);
        this.buttonsFont = new Font(this.appFontFamily, Font.BOLD, 14);
    }

    private void loadConfiguration() {
        InputStream configuration =
                AppSettings.class.getClassLoader().getResourceAsStream(
                        "config.properties");

        assert configuration != null;

        try {
            this.properties.load(configuration);
        } catch (Exception e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            System.exit(1);
        }
    }

    public static synchronized AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }

        return instance;
    }

}
