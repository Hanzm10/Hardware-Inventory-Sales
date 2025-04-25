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
package com.github.hanzm_10.murico.swingapp.lib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;
import com.github.hanzm_10.murico.swingapp.lib.utils.DateUtils;

public final class PropertiesIO {
    private static final Logger LOGGER = MuricoLogger.getLogger(PropertiesIO.class);
    public static final String PROPERTIES_FILE_EXTENSION = ".properties";

    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", FileLoadLeniency.MANDATORY);
    }

    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name,
            FileLoadLeniency leniency)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", leniency);
    }

    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name,
            @NotNull final String path)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, path, FileLoadLeniency.MANDATORY);
    }

    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name,
            @NotNull final String path, FileLoadLeniency leniency)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Properties file name cannot be null or empty");
        }

        if (path == null) {
            throw new IllegalArgumentException("Properties file path cannot be null");
        }

        var filePath = path + name + PROPERTIES_FILE_EXTENSION;
        LOGGER.info("Loading properties file: " + filePath);

        try (var inputStream = clazz.getResourceAsStream(filePath)) {
            if (properties != null) {
                properties.load(inputStream);
            } else {
                switch (leniency) {
                    case MANDATORY -> {
                        throw new FileNotFoundException("Properties file not found: " + filePath);
                    }
                    case LOG_MISSING -> {
                        LOGGER.warning("Properties file not found: " + filePath);
                    }
                    case CREATE_FILE_IF_MISSING -> {
                        // DO nothing since we cannot write to a resource file
                        // as we don't know the path of the clazz on runtime.
                    }
                    case ALLOW_MISSING -> {
                        LOGGER.info("Properties file not found: " + filePath);
                    }
                }
            }
        } catch (NullPointerException e) {
            // DO nothing
        }
    }

    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz,
            @NotNull final String name) throws IllegalArgumentException,
    FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name);

        return properties;
    }

    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz,
            @NotNull final String name, @NotNull final String path)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name, path);

        return properties;
    }

    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz,
            @NotNull final String name, @NotNull final String path, FileLoadLeniency leniency)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name, path, leniency);

        return properties;
    }

    public static final Properties loadPropertiesFromFileSystem(@NotNull final String filePath)
            throws FileNotFoundException, IOException {
        var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties);
        return properties;
    }

    public static final Properties loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final FileLoadLeniency leniency) throws FileNotFoundException, IOException {
        var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties, leniency);
        return properties;
    }

    public static final void loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final Properties properties) throws FileNotFoundException, IOException {
        loadPropertiesFromFileSystem(filePath, properties, FileLoadLeniency.MANDATORY);
    }

    public static final void loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final Properties properties, @NotNull final FileLoadLeniency leniency)
                    throws FileNotFoundException, IOException {
        try (var inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            switch (leniency) {
                case MANDATORY -> {
                    throw new FileNotFoundException("Properties file not found: " + filePath);
                }
                case LOG_MISSING -> {
                    LOGGER.warning("Properties file not found: " + filePath);
                }
                case CREATE_FILE_IF_MISSING -> {
                    new File(filePath).createNewFile();
                }
                case ALLOW_MISSING -> {
                    LOGGER.info("Properties file not found: " + filePath);
                }
            }
        }
    }

    public static final void savePropertiesInFileSystem(@NotNull final Properties properties,
            @NotNull final String filePath)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        savePropertiesInFileSystem(properties, filePath, null);
    }

    public static final void savePropertiesInFileSystem(@NotNull final Properties properties,
            @NotNull final String filePath, @NotNull final String headerComment)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        var comment = DateUtils.getDateWithFormat();

        if (headerComment != null && !headerComment.isEmpty()) {
            comment = headerComment + "\n" + comment;
        }

        try (var outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, comment);
        } catch (SecurityException e) {
            LOGGER.severe("Denied access to file: " + filePath);
        } catch (ClassCastException e) {
            // TODO: A way to clean the properties file if it is corrupted.
            LOGGER.log(Level.SEVERE, "Configuration file in " + filePath + " contains invalid data. ", e);
        }
    }

    private PropertiesIO() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
