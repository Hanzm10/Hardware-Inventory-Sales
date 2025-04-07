module Hardware.Inventory.Sales {
  requires java.desktop;
  requires java.sql;
  requires java.management;
  requires java.base;

  requires com.jgoodies.forms;
  requires com.miglayout.core;
  requires com.miglayout.swing;

  requires org.junit.jupiter.api;
  requires muricolaf;

  exports com.murico.app;
}
