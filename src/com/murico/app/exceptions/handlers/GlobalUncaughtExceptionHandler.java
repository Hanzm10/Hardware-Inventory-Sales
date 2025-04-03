package com.murico.app.exceptions.handlers;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.murico.app.utils.io.FileLoader;

public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {

  private static final Logger logger =
      Logger.getLogger(GlobalUncaughtExceptionHandler.class.getName());

  public GlobalUncaughtExceptionHandler() {
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    try {
      var fileHandler =
          new FileHandler(
              FileLoader.getLogsDirectory() + File.separator + "global_uncaught_exception_%u.log",
              1024 * 1024, 5, true);

      fileHandler.setFormatter(new SimpleFormatter());

      logger.addHandler(fileHandler);
      logger.log(Level.SEVERE, "Uncaught exception in thread: " + t.getName(), e);

      fileHandler.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
      JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    });
  }


}
