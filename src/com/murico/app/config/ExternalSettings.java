package com.murico.app.config;

import java.io.File;
import java.io.FileOutputStream;
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

  public static final Character NO_EDIT_PREFIX = '_';
  public static final String SESSION_ID_KEY = ExternalSettings.NO_EDIT_PREFIX + "session.uid";

  private final String propertiesFilePath =
      FileLoader.getConfigurationDirectory() + File.separator + "config.properties";

  private ExternalSettings() {
    super();

    try {
      // Load properties from the configuration directory
      FileLoader.loadFileFromConfigurationDirectoryToProperties("config.properties", properties);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static synchronized ExternalSettings getInstance() {
    if (instance == null) {
      instance = new ExternalSettings();
    }
    return instance;
  }

  @Override
  public void save() {
    // Save properties to the configuration directory
    try (var outputStream = new FileOutputStream(propertiesFilePath)) {
      properties.store(outputStream, "DO NOT EDIT THOSE PREFIXED WITH '_' . Auto-generated file");
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
