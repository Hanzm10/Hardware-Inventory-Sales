package com.murico.app.config;

import com.murico.app.utils.io.FileLoader;

/**
 * A read-only configuration class that extends the AbstractSettings class.
 */
public class AppSettings extends AbstractSettings {
  private static AppSettings instance = null;
  private static final String CONFIG_FILE = "config.properties";

  private String appTitle;

  private AppSettings() {
    super();

    try {
      FileLoader.loadFileFromResourcesToProperties(CONFIG_FILE, properties);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE, e);
    }
  }

  /**
   * Returns the singleton instance of AppSettings.
   *
   * @return the singleton instance of AppSettings
   */
  public static AppSettings getInstance() {
    if (instance == null) {
      instance = new AppSettings();
    }

    return instance;
  }

  /**
   * Returns the application title.
   *
   * @return the application title
   */
  public String getAppTitle() {
    if (appTitle == null) {
      appTitle = properties.getProperty("app.title");
    }
    return appTitle;
  }

  @Override
  void save() {
    // do nothing
  }

}
