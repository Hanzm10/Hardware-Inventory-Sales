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
import javax.swing.SwingUtilities;
import com.github.hanzm_10.murico.lookandfeel.MuricoLightFlatLaf;
import com.github.hanzm_10.murico.swingapp.constants.Directories;
import com.github.hanzm_10.murico.swingapp.exceptions.handlers.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.swingapp.lib.io.FileUtils;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.service.database.SessionService;
import com.github.hanzm_10.murico.swingapp.ui.MainFrame;

public class MuricoSwingApp {
    private static final Logger LOGGER = MuricoLogger.getLogger(MuricoSwingApp.class);

    private static void createAndShowGUI() {
        var isSuccessful = MuricoLightFlatLaf.setup();

        if (!isSuccessful) {
            LOGGER.log(Level.SEVERE, "Failed to setup MuricoLightFlatLaf for some reason");
            LOGGER.log(Level.SEVERE, "Using default L&F");
        }

        var _ = new MainFrame();
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
        SwingUtilities.invokeLater(() -> {
            FileUtils.createDirectoryIfNotExists(Directories.CONFIG_DIRECTORY);
            FileUtils.createDirectoryIfNotExists(Directories.LOGS_DIRECTORY);

            try {
                SessionService.checkPreviousSessionAndStoreInSessionManager();
                createAndShowGUI();
            } catch (IOException | SQLException e) {
                // TODO: Probably don't start the app if this happens
                LOGGER.log(Level.SEVERE, "Failed to load previous session", e);
            }
        });
    }
}
