package com.murico.app.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.murico.app.config.AppSettings;
import com.murico.app.utils.OperatingSystem;

/**
 * Utility class for loading files from the classpath or configuration directory into a Properties
 * object.
 * <p>
 * This class provides methods to load properties files from the classpath and from a configuration
 * directory based on the operating system. The configuration directory is determined based on the
 * user's home directory and the application's title.
 * </p>
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class FileLoader {
  private static String configurationDirectory;
  private static String appDataDirectory;
  private static String logsDirectory;

  /**
   * Loads a properties file from the classpath into the provided Properties object.
   *
   * @param filePath The path to the properties file, relative to the classpath.
   * @param properties The Properties object to load the file into.
   * 
   * @throws IOException if an error occurred when reading from the input stream, or an I/O error
   *         occured.
   * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape
   *         sequence.
   */
  public static void loadFileFromResourcesToProperties(String filePath, Properties properties)
      throws IOException, IllegalArgumentException, NullPointerException {
    var fileStream = FileLoader.class.getClassLoader().getResourceAsStream(filePath);

    assert fileStream != null : "File not found: " + filePath;

    properties.load(fileStream);
    fileStream.close();
  }

  /**
   * Loads a properties file from the configuration directory into the provided Properties object.
   *
   * @param filePath The path to the properties file, relative to the configuration directory.
   * @param properties The Properties object to load the file into.
   * 
   * @throws NullPointerException - If child is null or if the file does not exist.
   * @throws AssertionError - If the file does not exist.
   * @throws IOException - If an I/O error occurs. FileNotFoundException - if the file does not
   *         exist, is a directory rather than a regular file, or for some other reason cannot be
   *         opened for reading.
   * @throws SecurityException - If a security manager exists and its checkRead method denies read
   *         access to the file.
   */
  public static void loadFileFromConfigurationDirectoryToProperties(String filePath,
      Properties properties) throws NullPointerException, AssertionError, IOException,
      FileNotFoundException, SecurityException {
    var file = new File(getConfigurationDirectory(), filePath);

    assert file != null : "File not found: " + filePath;

    if (!file.exists()) {
      throw new IOException("File not found: " + file.getAbsolutePath());
    }

    InputStream fileStream = new FileInputStream(file);

    assert fileStream != null : "File not found: " + filePath;

    properties.load(fileStream);
    fileStream.close();
  }

  /**
   * Returns the configuration directory for the application based on the operating system.
   *
   * @return The configuration directory path.
   * @throws IllegalStateException if the user home directory is not found.
   */
  public static String getConfigurationDirectory() throws IllegalStateException {
    if (configurationDirectory == null) {
      configurationDirectory = FileLoader.getAppDataDirectory() + File.separator + "config";
      }

    return configurationDirectory;
  }

  /**
   * Returns the logs directory for the application based on the operating system.
   *
   * @return The logs directory path.
   * @throws IllegalStateException if the user home directory is not found.
   */
  public static String getLogsDirectory() throws IllegalStateException {
    if (logsDirectory == null) {
      logsDirectory = FileLoader.getAppDataDirectory() + File.separator + "logs";
    }

    return logsDirectory;
  }

  /**
   * Returns the application data directory for the application based on the operating system.
   *
   * @return The application data directory path.
   * @throws IllegalStateException if the user home directory is not found.
   */
  public static String getAppDataDirectory() {
    if (appDataDirectory == null) {
      var userHome = System.getProperty("user.home");

      if (userHome == null) {
        throw new IllegalStateException("User home directory not found.");
      }

      appDataDirectory = switch (OperatingSystem.CURRENT_OS) {
        case Windows -> userHome + File.separator + "AppData" + File.separator + "Local"
            + File.separator + AppSettings.getInstance().getAppTitle();
        case MacOS -> userHome + File.separator + "Library" + File.separator + "Application Support"
            + File.separator + AppSettings.getInstance().getAppTitle();
        case Linux -> userHome + File.separator + "." + AppSettings.getInstance().getAppTitle();
      };
    }

    return appDataDirectory;
  };
}
