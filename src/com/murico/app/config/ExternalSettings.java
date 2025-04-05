package com.murico.app.config;

import java.io.File;
import com.murico.app.utils.io.FileLoader;

/**
 * Singleton class for managing external configuration properties.
 * <p>
 * This class loads properties from a configuration file and provides methods to access them.
 * </p>
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class ExternalSettings extends AbstractSettings {
  private static ExternalSettings instance;

  public static final String SESSION_ID_KEY = READ_ONLY_PREFIX + "session.uid";

  private ExternalSettings() {
    super();
  }

  public static synchronized ExternalSettings getInstance() {
    if (instance == null) {
      instance = new ExternalSettings();
    }
    return instance;
  }

  @Override
  protected String getFileName() {
    return "settings.properties";
  }

  @Override
  protected String getFilePath() {
    return FileLoader.getConfigurationDirectory() + File.separator + getFileName();
  }

  @Override
  protected boolean isFileReadOnly() {
    return false;
  }

  @Override
  protected boolean editableByUser() {
    return true;
  }

}
