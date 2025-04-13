/** Copyright 2025
 *  - Aaron Ragudos
 *  - Hanz Mapua
 *  - Peter Dela Cruz
 *  - Jerick Remo
 *  - Kurt Raneses
 *
 *  Permission is hereby granted, free of charge, to any
 *  person obtaining a copy of this software and associated
 *  documentation files (the “Software”), to deal in the Software
 *  without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons
 *  to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 *  ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.github.hanzm_10.murico.app.constants.GlobalConfig;
import com.github.hanzm_10.murico.app.loading.InitialLoadingPage;
import com.github.hanzm_10.murico.app.managers.SessionManager;
import com.github.hanzm_10.murico.core.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.io.MuricoConfiguration;
import com.github.hanzm_10.murico.lookandfeel.MuricoLookAndFeel;
import com.github.hanzm_10.murico.utils.LogUtils;

public class Murico {
	private static class CheckSessionWorker extends SwingWorker<Void, String> {
		InitialLoadingPage splashScreen;

		public CheckSessionWorker(InitialLoadingPage splashScreen) {
			// Constructor
			LOGGER.info("Checking user session...");

			this.splashScreen = splashScreen;
		}

		@Override
		protected Void doInBackground() throws Exception {
			LOGGER.info("Initializing file system...");
			publish("Initializing file system...");
			Thread.sleep(1000); // Simulate delay for file system initialization
			initializeFileSystem();

			LOGGER.info("Checking user session...");
			publish("Checking user session...");
			Thread.sleep(1000); // Simulate delay for user session check
			var sessionUid = GlobalConfig.getInstance().getProperty(GlobalConfig.KEY_SESSION_UID);

			if (sessionUid == null) {
				LOGGER.warning("Session uid does not exist. Proceeding to login...");
				publish("Session uid does not exist. Proceeding to login...");
				Thread.sleep(1000); // Simulate delay for session removal

				return null;
			}

			var factory = AbstractSQLFactoryDAO.getSQLFactoryDAO(AbstractSQLFactoryDAO.MYSQL);
			var sessionDAO = factory.getSessionDAO();

			LOGGER.info("Verifying session uid...");
			publish("Verifying session uid...");
			Thread.sleep(1000); // Simulate delay for session verification
			var sessionExists = sessionDAO.sessionExists(sessionUid);

			if (!sessionExists) {
				LOGGER.warning("Session does not exist. Removing session uid...");
				publish("Session does not exist. Removing session uid...");
				Thread.sleep(1000); // Simulate delay for session removal
				GlobalConfig.getInstance().remove(GlobalConfig.KEY_SESSION_UID);

				return null;
			}

			LOGGER.info("Session uid is valid. Verifying session...");
			publish("Session uid is valid. Verifying session...");
			Thread.sleep(1000); // Simulate delay for session verification
			var session = sessionDAO.getSessionByUid(sessionUid);

			if (session == null) {
				LOGGER.log(Level.SEVERE, "Session should have existed but was not found.");
			} else if (session.isExpired()) {
				LOGGER.warning("Session has expired. Removing session uid...");
				publish("Session has expired. Removing session uid...");
				Thread.sleep(1000); // Simulate delay for session expiration
				GlobalConfig.getInstance().remove(GlobalConfig.KEY_SESSION_UID);
			} else {
				LOGGER.info("Session is valid. Updating application state...");
				publish("Session is valid. Updating application state...");
				Thread.sleep(1000); // Simulate delay for application state update
				SessionManager.getInstance().setSession(session);
			}

			return null;
		}

		@Override
		protected void done() {
			splashScreen.dispose();

			LOGGER.info("Murico application initialized successfully.");
		}

		@Override
		protected void process(java.util.List<String> chunks) {
			for (String message : chunks) {
				splashScreen.setProgressLabel(message);
			}
		}
	}

	private static final Logger LOGGER = LogUtils.getLogger(Murico.class);

	private static void initializeFileSystem() {
		MuricoConfiguration.createConfigDirectory();
		MuricoConfiguration.createLogsDirectory();
	}

	public static void main(String[] args) {
		LOGGER.info("Initializing Murico application...");

		try {
			LOGGER.info("Setting Look and Feel...");
			UIManager.setLookAndFeel(new MuricoLookAndFeel());
			LOGGER.info("Look and Feel set to Murico Look and Feel.");
		} catch (Exception e) {
			LOGGER.severe("Failed to set Look and Feel: " + e.getMessage());
		}

		Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());

		SwingUtilities.invokeLater(() -> {
			new CheckSessionWorker(new InitialLoadingPage()).execute();
		});
	}
}
