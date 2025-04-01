package com.murico.app.dal.database.mysql;

import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DatabaseManagerMySQLTest {

  @Test
  @DisplayName("Test MySQL Database Connection")
  void test() throws SQLException {
    var dbManager = DatabaseManagerMySQL.getInstance();
    var conn = dbManager.connect();
    assert conn != null : "Connection should not be null";
    dbManager.disconnect(conn);
  }

}
