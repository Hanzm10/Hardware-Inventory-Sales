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

import java.io.File;
import java.util.logging.Logger;
import javax.swing.UIManager;
import com.github.hanzm_10.murico.core.GlobalUncaughtExceptionHandler;
import com.github.hanzm_10.murico.io.MuricoConfiguration;
import com.github.hanzm_10.murico.lookandfeel.MuricoLookAndFeel;
import com.github.hanzm_10.murico.utils.LogUtils;

public class Murico {
    private static final Logger LOGGER = LogUtils.getLogger(Murico.class);

    private static void checkSession() {
        LOGGER.info("Checking user session...");
    }

    private static void initialize() {
        LOGGER.info("Initializing Murico application...");
        initializeFileSystem();
        checkSession();
        LOGGER.info("Murico application initialized successfully.");
    }

    private static void initializeFileSystem() {
        LOGGER.info("Initializing file system...");

        var configDir = new File(MuricoConfiguration.CONFIG_DIRECTORY);

        if (!configDir.exists()) {
            configDir.mkdirs();
            LOGGER.info("Configuration directory created: " + configDir.getAbsolutePath());
        } else {
            LOGGER.info("Configuration directory already exists: " + configDir.getAbsolutePath());
        }

        var logDir = new File(MuricoConfiguration.LOGS_DIRECTORY);

        if (!logDir.exists()) {
            logDir.mkdirs();
            LOGGER.info("Log directory created: " + logDir.getAbsolutePath());
        } else {
            LOGGER.info("Log directory already exists: " + logDir.getAbsolutePath());
        }

        LOGGER.info("File system initialized successfully.");
    }

    public static void main(String[] args) {
        LOGGER.info("Starting Murico...");

        try {
            LOGGER.info("Setting Look and Feel...");
            UIManager.setLookAndFeel(new MuricoLookAndFeel());
            LOGGER.info("Look and Feel set to Murico Look and Feel.");
        } catch (Exception e) {
            LOGGER.severe("Failed to set Look and Feel: " + e.getMessage());
        }

        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
        initialize();
    }
}
