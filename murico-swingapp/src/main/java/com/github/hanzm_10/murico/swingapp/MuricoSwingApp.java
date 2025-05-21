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
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import com.github.hanzm_10.murico.lookandfeel.MuricoLightFlatLaf;
import com.github.hanzm_10.murico.swingapp.constants.Directories;
import com.github.hanzm_10.murico.swingapp.constants.Metadata;
import com.github.hanzm_10.murico.swingapp.lib.database.AbstractMigratorFactory;
import com.github.hanzm_10.murico.swingapp.lib.exceptions.handlers.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.swingapp.lib.exceptions.interpreter.ErrorInterpreterRegistry;
import com.github.hanzm_10.murico.swingapp.lib.io.FileUtils;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.service.database.SessionService;
import com.github.hanzm_10.murico.swingapp.ui.MainFrame;
import com.github.hanzm_10.murico.swingapp.ui.labels.LabelFactory;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import net.miginfocom.swing.MigLayout;

public class MuricoSwingApp {
	static class ProgressWindow extends JWindow {
		JLabel label;

		public ProgressWindow(String text) {

			label = LabelFactory.createLabel(text, 20);
			var progressBar = new javax.swing.JProgressBar();

			label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			progressBar.setIndeterminate(true);

			var panel = new javax.swing.JPanel();

			panel.setLayout(new MigLayout("wrap", "[grow, center]", "[grow, center]"));

			panel.add(progressBar, "growx");
			panel.add(label, "grow");

			add(panel);

			setSize(300, 100);
			setLocationRelativeTo(null);
		}

		public void setText(String text) {
			label.setText(text);
			validate();
		}
	}

	private static ProgressWindow PROGRESS_WINDOW = new ProgressWindow("Initializing Murico...");
	private static final Logger LOGGER = MuricoLogger.getLogger(MuricoSwingApp.class);

	private static void createAndShowGUI() {
		new MainFrame();
	}

	private static boolean doDevelopmentSetup() throws SQLException {
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

		return true;
	}

	public static void main(String[] args) {
		var isSuccessful = MuricoLightFlatLaf.setup();

		if (!isSuccessful) {
			LOGGER.log(Level.SEVERE, "Failed to setup MuricoLightFlatLaf for some reason");
			LOGGER.log(Level.SEVERE, "Using default L&F");
		}

		SwingUtilities.invokeLater(() -> {
			PROGRESS_WINDOW.setVisible(true);
		});

		try {
			if (Metadata.APP_ENV.equals("development")) {
				if (!doDevelopmentSetup()) {
					LOGGER.log(Level.SEVERE, "Failed to setup development environment");
					return;
				}
			} else {
				MuricoLogger.setLevel(Level.OFF);
			}

			Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
			FileUtils.createDirectoryIfNotExists(Directories.CONFIG_DIRECTORY);
			FileUtils.createDirectoryIfNotExists(Directories.LOGS_DIRECTORY);
			SessionService.checkPreviousSessionAndStoreInSessionManager();
			createAndShowGUI();
		} catch (IOException | SQLException e) {
			// TODO: Probably don't start the app if this happens
			SwingUtilities.invokeLater(() -> {
				var msg = new ErrorInterpreterRegistry().interpret(e);

				JOptionPane.showMessageDialog(null,
						"The following error has occured:\n\n" + msg + " \n\nPress ok to show the user manual", "Error",
						JOptionPane.ERROR_MESSAGE);
				new ReadmeWindow().setVisible(true);
			});

			LOGGER.log(Level.SEVERE, "Failed to load previous session", e);
			AbandonedConnectionCleanupThread.checkedShutdown();
		} finally {
			SwingUtilities.invokeLater(() -> {
				PROGRESS_WINDOW.dispose();
			});
		}
	}
}
