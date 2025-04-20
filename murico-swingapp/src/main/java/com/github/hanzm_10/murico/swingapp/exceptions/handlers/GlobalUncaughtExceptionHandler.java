package com.github.hanzm_10.murico.swingapp.exceptions.handlers;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.github.hanzm_10.murico.swingapp.constants.Directories;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private static final Logger LOGGER =
            MuricoLogger.getLogger(GlobalUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            var fileHandler = new FileHandler(
                    Directories.LOGS_DIRECTORY + "/global_uncaught_exception_%u.log",
                    1024 * 1024, 5, true);
            fileHandler.setFormatter(new SimpleFormatter());

            LOGGER.addHandler(fileHandler);
            LOGGER.log(Level.SEVERE, "Uncaught exception in thread: " + t.getName(), e);

            fileHandler.close();
        } catch (Exception ex) {
            LOGGER.severe("Failed to save log of uncaught exception: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        });
    }
}