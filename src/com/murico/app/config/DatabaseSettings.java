package com.murico.app.config;

import java.io.IOException;
import java.util.Properties;
import com.murico.app.utils.io.FileLoader;

public class DatabaseSettings {
  private static DatabaseSettings instance;
  private final Properties properties;
  private String dbUser;
  private String dbPassword;
  private String dbUrl;
  private String dbName;

  private DatabaseSettings() {
    this.properties = new Properties();

    try {
      FileLoader.loadFileFromResourcesToProperties("database.properties", properties);
    } catch (IllegalArgumentException | NullPointerException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.setConfigDefaults();
  }

  public static synchronized DatabaseSettings getInstance() {
    if (instance == null) {
      instance = new DatabaseSettings();
    }

    return instance;
  }

  public String getDbUser() {
    return dbUser;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public String getDbUrl() {
    return dbUrl;
  }

  public String getDbName() {
    return dbName;
  }

  private void setConfigDefaults() {
    this.dbUser = properties.getProperty("db.user");
    this.dbPassword = properties.getProperty("db.password");
    this.dbUrl = properties.getProperty("db.url");
    this.dbName = properties.getProperty("db.name");

    assert dbUser != null : "Database user not found in configuration";
    assert dbPassword != null : "Database password not found in configuration";
    assert dbUrl != null : "Database URL not found in configuration";
    assert dbName != null : "Database name not found in configuration";
  }
}
