/**
 * Copyright 2025 - Aaron Ragudos - Hanz Mapua - Peter Dela Cruz - Jerick Remo - Kurt Raneses
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, LOGGER.info, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.app;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import com.github.hanzm_10.murico.app.loading.InitialLoadingScreen;
import com.github.hanzm_10.murico.app.loading.SplashScreenFactory;
import com.github.hanzm_10.murico.app.managers.SessionManager;
import com.github.hanzm_10.murico.core.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.core.constants.GlobalConfig;
import com.github.hanzm_10.murico.core.model.Session;
import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.io.MuricoConfiguration;
import com.github.hanzm_10.murico.lookandfeel.MuricoLookAndFeel;
import com.github.hanzm_10.murico.utils.LogUtils;

public class Murico {
    private static class CheckSessionWorker extends SwingWorker<Void, String> {
        Timer showSplashScreenDelayTimer;
        JWindow splashScreen;

        public CheckSessionWorker(JWindow splashScreen, Timer showSplashScreenDelayTimer) {
            this.splashScreen = splashScreen;
            this.showSplashScreenDelayTimer = showSplashScreenDelayTimer;

            showSplashScreenDelayTimer.start();
        }

        @Override
        protected Void doInBackground() throws Exception {
            LOGGER.info("Initializing file system...");
            initializeFileSystem();

            LOGGER.info("Getting session uid from GlobalConfig...");
            var sessionUid = GlobalConfig.getInstance().getProperty(GlobalConfig.KEY_SESSION_UID);

            if (sessionUid != null) {
                LOGGER.info("Verifying session uid...");
                verifySessionUid(sessionUid);
            } else {
                LOGGER.info("Session uid does not exist. Proceeding to login...");
            }

            return null;
        }

        @Override
        protected void done() {
            LOGGER.info("Murico application initialized successfully.");
            showSplashScreenDelayTimer.stop();
            splashScreen.dispose();
            new MuricoAppWindow();
        }

        private void verifySession(Session session) throws Exception {
            if (session == null) {
                LOGGER.log(Level.SEVERE, "Session should have existed but was not found.");
                // Show a dialog here to inform the user that something went wrong
            } else if (session.isExpired()) {
                LOGGER.info("Session has expired. Removing session uid...");
                GlobalConfig.getInstance().remove(GlobalConfig.KEY_SESSION_UID);
            } else {
                LOGGER.info("Session is valid. Updating application state...");
                SessionManager.getInstance().setSession(session);
            }
        }

        private void verifySessionUid(String sessionUid) throws Exception {
            var factory = AbstractSQLFactoryDAO.getSQLFactoryDAO(AbstractSQLFactoryDAO.MYSQL);
            var sessionDAO = factory.getSessionDAO();
            var sessionExists = sessionDAO.sessionExists(sessionUid);

            if (sessionExists) {
                LOGGER.info("Session uid is valid. Verifying session...");
                var session = sessionDAO.getSessionByUid(sessionUid);

                verifySession(session);
            } else {
                LOGGER.info("Session does not exist. Removing session uid...");
                GlobalConfig.getInstance().remove(GlobalConfig.KEY_SESSION_UID);
            }
        }
    }

    private static final Logger LOGGER = LogUtils.getLogger(Murico.class);

    private static void initialize() {
        var splashScreen = new InitialLoadingScreen();
        var splashScreenWindow = SplashScreenFactory.createSplashScreenJWindow(splashScreen);
        var timer = new Timer(200, _ -> splashScreenWindow.setVisible(true));

        timer.setRepeats(false);

        var worker = new CheckSessionWorker(splashScreenWindow, timer);

        worker.execute();
    }

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
        SwingUtilities.invokeLater(Murico::initialize);
    }
}
