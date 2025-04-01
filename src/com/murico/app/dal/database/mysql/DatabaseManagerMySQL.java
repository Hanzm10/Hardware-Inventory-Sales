package com.murico.app.dal.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.murico.app.config.DatabaseSettings;

public class DatabaseManagerMySQL {
  private static DatabaseManagerMySQL instance;

  private DatabaseManagerMySQL() {}

  public static synchronized DatabaseManagerMySQL getInstance() {
    if (instance == null) {
      instance = new DatabaseManagerMySQL();
    }
    return instance;
  }

  public Connection connect() throws SQLException {
    var dbSettings = DatabaseSettings.getInstance();

    return DriverManager.getConnection(dbSettings.getDbUrl(), dbSettings.getDbUser(),
        dbSettings.getDbPassword());
  }

  public void disconnect(Connection conn) throws SQLException {
    if (conn != null) {
      conn.close();
    }
  }

}
