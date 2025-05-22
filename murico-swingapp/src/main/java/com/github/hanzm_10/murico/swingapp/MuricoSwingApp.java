/**
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.lookandfeel.MuricoLightFlatLaf;
import com.github.hanzm_10.murico.swingapp.constants.Directories;
import com.github.hanzm_10.murico.swingapp.constants.Metadata;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.exceptions.handlers.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.swingapp.lib.io.FileUtils;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.service.database.SessionService;
import com.github.hanzm_10.murico.swingapp.ui.MainFrame;
import com.github.hanzm_10.murico.swingapp.ui.components.ProgressWindow;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class MuricoSwingApp {
	private static final Logger LOGGER = MuricoLogger.getLogger(MuricoSwingApp.class);

	private static ProgressWindow PROGRESS_WINDOW;
	public static boolean IS_DEVELOPMENT = Metadata.APP_ENV.equals("development");

	private static void createAndShowGUI() {
		SwingUtilities.invokeLater(() -> {
			var mainFrame = new MainFrame();
			mainFrame.setVisible(true);
		});
	}

	public static void main(String[] args) {
		var isSuccessful = MuricoLightFlatLaf.setup();

		if (!isSuccessful) {
			LOGGER.log(Level.SEVERE, "Failed to setup MuricoLightFlatLaf for some reason");
			LOGGER.log(Level.SEVERE, "Using default L&F");
		}

		SwingUtilities.invokeLater(() -> {
			PROGRESS_WINDOW = new ProgressWindow("Initializing Murico...");
			PROGRESS_WINDOW.setVisible(true);
		});

		try {
			if (!Metadata.APP_ENV.equals("development")) {
				LogManager.getLogManager().reset();
				MuricoLogger.setLevel(Level.OFF);
			}

			var migratorFactory = AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.MYSQL);
			var migrator = migratorFactory.getMigrator();
			var seeder = migratorFactory.getSeeder();

			SwingUtilities.invokeLater(() -> {
				PROGRESS_WINDOW.setText("Creating data structures...");
			});

			migrator.migrate();

			SwingUtilities.invokeLater(() -> {
				PROGRESS_WINDOW.setText("Initializing data...");
			});

			seeder.seed();

			Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
			FileUtils.createDirectoryIfNotExists(Directories.CONFIG_DIRECTORY);
			FileUtils.createDirectoryIfNotExists(Directories.LOGS_DIRECTORY);
			SessionService.checkPreviousSessionAndStoreInSessionManager();

			createAndShowGUI();
		} catch (IOException | SQLException e) {
			SwingUtilities.invokeLater(() -> {
				LOGGER.log(Level.SEVERE, "Failed to start application", e);

				JOptionPane.showMessageDialog(null,
						"Sorry, but the application cannot start. \n\nPress ok to show the user manual", "Error",
						JOptionPane.ERROR_MESSAGE);
				new ReadmeWindow().setVisible(true);
			});

			LOGGER.log(Level.SEVERE, "Failed to load previous session", e);
			AbandonedConnectionCleanupThread.checkedShutdown();
		} finally {
			SwingUtilities.invokeLater(() -> {
				PROGRESS_WINDOW.dispose();
				PROGRESS_WINDOW = null;
			});
		}
	}
}
