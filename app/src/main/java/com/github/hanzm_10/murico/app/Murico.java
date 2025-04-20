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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import com.github.hanzm_10.murico.app.managers.SessionManager;
import com.github.hanzm_10.murico.app.view.MuricoAppMainWindow;
import com.github.hanzm_10.murico.config.app.ApplicationConfig;
import com.github.hanzm_10.murico.constants.MuricoDirectories;
import com.github.hanzm_10.murico.constants.PropertyKey;
import com.github.hanzm_10.murico.database.AbstractSQLFactoryDAO;
import com.github.hanzm_10.murico.exceptions.handlers.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.io.FileIO;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

public class Murico {
    private static final Logger LOGGER = MuricoLogUtils.getLogger(Murico.class);

    private static void initialize() {
        initializeFileSystem();
        MuricoLightLaf.setup();

        var sessionUid = ApplicationConfig.getInstance().getProperty(PropertyKey.Session.UID);

        if (sessionUid != null) {
            verifySessionUid(sessionUid);
        }

        new MuricoAppMainWindow();
    }

    private static void initializeFileSystem() {
        FileIO.createDirectoryIfNotExists(MuricoDirectories.LOGS_DIRECTORY);
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
        SwingUtilities.invokeLater(Murico::initialize);
    }

    private static void verifySessionUid(final String sessionUid) {
        var factory = AbstractSQLFactoryDAO.getSQLFactoryDAO(AbstractSQLFactoryDAO.MYSQL);
        var sessionDAO = factory.getSessionDAO();
        var sessionExists = sessionDAO.sessionExists(sessionUid);

        if (!sessionExists) {
            try {
                ApplicationConfig.getInstance().remove(PropertyKey.Session.UID);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to remove session uid from config file.", e);
            }

            return;
        }

        var session = sessionDAO.getSessionByUid(sessionUid);

        if (session.isExpired()) {
            try {
                ApplicationConfig.getInstance().remove(PropertyKey.Session.UID);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return;
        }

        var userDAO = factory.getUserDAO();
        var user = userDAO.getUserById(session._userId());

        if (user == null) {
            try {
                ApplicationConfig.getInstance().remove(PropertyKey.Session.UID);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to remove session uid from config file.", e);
            }
            return;
        }

        SessionManager.getInstance().setSession(session, user);
    }
}
