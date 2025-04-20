package com.github.hanzm_10.murico.swingapp.lib.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public final class FileUtils {
    private static final Logger LOGGER = MuricoLogger.getLogger(FileUtils.class);

    public static final void createDirectoryIfNotExists(@NotNull final String directoryPath) {
        var directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                LOGGER.severe("Failed to create directory: " + directoryPath);
            }
        }
    }

    public static void createFileIfNotExists(@NotNull final String filePath) {
        var file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create file in " + filePath, e);
            }
        }
    }
}
