package com.murico.app.config;

import java.io.File;
import com.murico.app.utils.io.FileLoader;

/**
 * A read-only configuration class that extends the AbstractSettings class.
 */
public class AppSettings extends AbstractSettings {
  private static AppSettings instance = null;

  private static final String KEY_APP_TITLE = "app.metadata.title";

  private String appTitle;

  private AppSettings() {
    super();
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
      appTitle = getProperty(KEY_APP_TITLE);
    }

    return appTitle;
  }

  @Override
  protected String getFileName() {
    return "config.properties";
  }

  @Override
  protected String getFilePath() {
    return FileLoader.getConfigurationDirectory() + File.separator + getFileName();
  }

  @Override
  protected boolean isFileReadOnly() {
    return true;
  }

  @Override
  protected boolean editableByUser() {
    return false;
  }

}
