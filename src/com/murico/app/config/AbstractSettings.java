package com.murico.app.config;

import java.util.Properties;

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
  protected Properties properties;

  public AbstractSettings() {
    this.properties = new Properties();
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public int getIntProperty(String key) {
    return Integer.parseInt(properties.getProperty(key));
  }

  public int getIntProperty(String key, int defaultValue) {
    var value = properties.getProperty(key);
    return (value != null) ? Integer.parseInt(value) : defaultValue;
  }

  public void setProperty(String key, String value) {
    properties.setProperty(key, value);
  }

  public void setProperty(String key, int value) {
    properties.setProperty(key, String.valueOf(value));
  }

  public void removeProperty(String key) {
    properties.remove(key);
  }

  abstract void save();
}
