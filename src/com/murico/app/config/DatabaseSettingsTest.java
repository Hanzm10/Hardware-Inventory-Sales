package com.murico.app.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DatabaseSettingsTest {

  @Test
  @DisplayName("Test Database Settings")
  void test() {
    var settings = DatabaseSettings.getInstance();

    assertNotNull(settings.getDbUser(), "Database user should not be null");
    assertNotNull(settings.getDbPassword(), "Database password should not be null");
    assertNotNull(settings.getDbUrl(), "Database URL should not be null");
    assertNotNull(settings.getDbName(), "Database name should not be null");
  }

}
