package com.murico.app.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import com.murico.app.utils.io.FileLoader;

/**
 * Abstract class for managing settings.
 * <p>
 * This class provides methods to get, set, and remove properties from a properties file.
 * </p>
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public abstract class AbstractSettings {
  /**
   * Returns the name of the properties file.
   * <p>
   * This method should be implemented by subclasses to provide the specific file name.
   * </p>
   *
   * @return the name of the properties file
   */
  abstract protected String getFileName();

  /**
   * Returns the external file path for the properties file.
   * <p>
   * This method should be implemented by subclasses to provide the specific file path.
   * </p>
   *
   * @return the file path for the properties file
   */
  abstract protected String getFilePath();

  /**
   * Returns whether the properties file is read-only.
   * <p>
   * This method should be implemented by subclasses to provide the specific read-only status.
   * </p>
   *
   * @return true if the properties file is read-only, false otherwise
   */
  abstract protected boolean isFileReadOnly();

  /**
   * Returns whether the properties file is editable by the user.
   * <p>
   * This method should be implemented by subclasses to provide the specific editability status.
   * </p>
   *
   * @return true if the properties file is editable by the user, false otherwise
   */
  abstract protected boolean editableByUser();

  protected final Properties defaultProperties = new Properties();
  protected final Properties properties = new Properties();

  public static final Character READ_ONLY_PREFIX = '_';

  public AbstractSettings() {
    load();
  }

  /**
   * Returns the value of the specified property.
   * <p>
   * This method first checks the default properties, then the external properties.
   * </p>
   *
   * @param key the property key
   * @return the property value, or null if not found
   */
  public String getProperty(String key) {
    var value = defaultProperties.getProperty(key);

    if (value == null) {
      value = properties.getProperty(key);
    }

    return value;
  }

  /**
   * Returns the value of the specified property, or a default value if not found.
   * <p>
   * This method first checks the default properties, then the external properties.
   * </p>
   *
   * @param key the property key
   * @param defaultValue the default value to return if the property is not found
   * @return the property value, or the default value if not found
   */
  public String getProperty(String key, String defaultValue) {
    var value = defaultProperties.getProperty(key);

    if (value == null) {
      value = properties.getProperty(key);
    }

    return (value != null) ? value : defaultValue;
  }

  /**
   * Returns the value of the specified property as an integer.
   * <p>
   * This method first checks the default properties, then the external properties.
   * </p>
   *
   * @param key the property key
   * @return the property value as an integer
   * @throws NumberFormatException if the property value cannot be parsed as an integer
   */
  public int getIntProperty(String key) {
    var value = defaultProperties.getProperty(key);

    if (value == null) {
      value = properties.getProperty(key);
    }

    return Integer.parseInt(value);
  }

  /**
   * Returns the value of the specified property as an integer, or a default value if not found.
   * <p>
   * This method first checks the default properties, then the external properties.
   * </p>
   *
   * @param key the property key
   * @param defaultValue the default value to return if the property is not found
   * @return the property value as an integer, or the default value if not found
   */
  public int getIntProperty(String key, int defaultValue) {
    var value = defaultProperties.getProperty(key);

    if (value == null) {
      value = properties.getProperty(key);
    }

    return (value != null) ? Integer.parseInt(value) : defaultValue;
  }

  /**
   * Sets the value of the specified property.
   * <p>
   * This method first checks if the file is read-only, and if so, throws an exception.
   * </p>
   *
   * @param key the property key
   * @param value the property value
   * @throws UnsupportedOperationException if the file is read-only
   */
  public void setProperty(String key, String value) throws UnsupportedOperationException {
    if (isFileReadOnly()) {
      throw new UnsupportedOperationException(getClass().getCanonicalName()
          + ": cannot set property '" + key + "' in read-only file: " + getFilePath());
    }

    throwIfNotEditableByUser();
    properties.setProperty(key, value);
  }

  /**
   * Sets the value of the specified property as an integer.
   * <p>
   * This method first checks if the file is read-only, and if so, throws an exception.
   * </p>
   *
   * @param key the property key
   * @param value the property value as an integer
   * @throws UnsupportedOperationException if the file is read-only or if the property is not
   *         editable by the user
   */
  public void setProperty(String key, int value) throws UnsupportedOperationException {
    // Check if the file is read-only
    if (isFileReadOnly()) {
      throw new UnsupportedOperationException(getClass().getCanonicalName()
          + ": cannot set property '" + key + "' in read-only file: " + getFilePath());
    }

    throwIfNotEditableByUser();
    properties.setProperty(key, String.valueOf(value));
  }

  /**
   * Removes the specified property.
   * <p>
   * This method first checks if the file is read-only, and if so, throws an exception.
   * </p>
   *
   * @param key the property key
   * @throws UnsupportedOperationException if the file is read-only or if the property is not
   *         editable by the user
   */
  public void removeProperty(String key) throws UnsupportedOperationException {
    if (isFileReadOnly()) {
      throw new UnsupportedOperationException(getClass().getCanonicalName()
          + ": cannot remove property '" + key + "' in read-only file: " + getFilePath());
    }
    
    throwIfNotEditableByUser();
    properties.remove(key);
  }

  /**
   * Saves the properties to the file.
   * <p>
   * This method first checks if the file is read-only, and if so, throws an exception.
   * </p>
   *
   * @throws UnsupportedOperationException if the file is read-only
   */
  public void save() {
    if (isFileReadOnly()) {
      throw new UnsupportedOperationException(getClass().getCanonicalName()
          + ": cannot save properties in read-only file: " + getFilePath());
    }

    try (var outputStream = new FileOutputStream(getFilePath())) {
      properties.store(outputStream,
          "DO NOT EDIT THOSE PREFIXED WITH _ (UNDERLINE) CHARACTER. THEY ARE READ ONLY PROPERTIES.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void load() {
    try {
      FileLoader.loadFileFromResourcesToProperties(getFileName(), defaultProperties);
    } catch (IllegalArgumentException | AssertionError | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (!isFileReadOnly()) {
      try {
        FileLoader.loadFileFromConfigurationDirectoryToProperties(getFileName(), properties);
      } catch (FileNotFoundException e) {
        createFile();
      } catch (AssertionError | IOException | IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  private void createFile() {
    try (var outputStream = new FileOutputStream(getFilePath())) {
      defaultProperties.store(outputStream,
          "DO NOT EDIT THOSE PREFIXED WITH _ (UNDERLINE) CHARACTER. THEY ARE READ ONLY PROPERTIES.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void throwIfNotEditableByUser() {
    if (!editableByUser()) {
      var className = getClass().getCanonicalName();

      throw new UnsupportedOperationException(className + ": to edit " + className
          + " properties, please call the respective setter methods for the properties you want to edit.");
    }
  }

}
