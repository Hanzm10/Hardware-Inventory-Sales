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
package com.github.hanzm_10.murico.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import com.github.hanzm_10.murico.utils.MuricoLogUtils;

/**
 * FileIO.java
 *
 * <p>
 * This class provides methods for File Input/Output operations.
 */
public final class FileIO {
    /**
     * LoadType enum defines the types of loading properties files.
     */
    public enum LoadType {
        ALLOW_MISSING,
        CREATE_FILE_IF_MISSING,
        MANDATORY
    }

    public static final Logger LOGGER = MuricoLogUtils.getLogger(FileIO.class);

    public static final void createDirectoryIfNotExists(final String directoryPath) {
        var directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                LOGGER.info("Directory created: " + directoryPath);
            } else {
                LOGGER.severe("Failed to create directory: " + directoryPath);
            }
        }
    }

    public static void createFileIfNotExists(final String filePath) {
        var file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.severe("Failed to create file: " + e.getMessage());
            }
        }
    }

    /**
     * Loads properties from a file located in the same package as the given class.
     *
     * @param clazz the class whose package will be used to locate the properties file
     * @param properties the Properties object to load the properties into
     * @param name the name of the properties file (without extension)
     */
    public static void loadProperties(final Class<?> clazz, final Properties properties,
            final String name) {
        loadProperties(clazz, properties, name, LoadType.ALLOW_MISSING);
    }

    /**
     * Loads properties from a file located in the same package as the given class.
     *
     * @param clazz the class whose package will be used to locate the properties file
     * @param properties the Properties object to load the properties into
     * @param name the name of the properties file (without extension)
     * @param loadType the type of loading (ALLOW_MISSING, CREATE_FILE_IF_MISSING, MANDATORY)
     */
    public static void loadProperties(final Class<?> clazz, final Properties properties,
            final String name, LoadType loadType) {
        loadProperties(clazz, properties, name, "", loadType);
    }


    /**
     * Loads properties from a file located in the specified path.
     *
     * @param clazz the class whose package will be used to locate the properties file
     * @param properties the Properties object to load the properties into
     * @param name the name of the properties file (without extension)
     * @param path the path to the properties file
     * @param loadType the type of loading (ALLOW_MISSING, CREATE_FILE_IF_MISSING, MANDATORY)
     */
    public static void loadProperties(final Class<?> clazz, final Properties properties,
            final String name, final String path, LoadType loadType) {
        var filePath = path + name + ".properties";

        try (var inputStream = clazz.getResourceAsStream(filePath)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else if (loadType == LoadType.MANDATORY) {
                LOGGER.severe("Properties file not found: " + filePath);
            }
        } catch (IOException e) {
            if (loadType == LoadType.MANDATORY) {
                LOGGER.severe("Failed to load properties from file: " + e.getMessage());
            }
        }
    }

    /**
     * Loads properties from a file located in the same package as the given class.
     *
     * @param clazz the class whose package will be used to locate the properties file
     * @param name the name of the properties file (without extension)
     * @return a Properties object containing the loaded properties
     */
    public static final Properties loadProperties(final Class<?> clazz, final String name) {
        var properties = new Properties();
        loadProperties(clazz, properties, name, LoadType.ALLOW_MISSING);
        return properties;
    }

    /**
     * Loads properties from a file located in the specified path.
     *
     * @param clazz the class whose package will be used to locate the properties file
     * @param name the name of the properties file (without extension)
     * @param path the path to the properties file
     * @return a Properties object containing the loaded properties
     */
    public static final Properties loadProperties(final Class<?> clazz, final String name,
            final String path) {
        var properties = new Properties();
        loadProperties(clazz, properties, name, path, LoadType.ALLOW_MISSING);
        return properties;
    }

    /**
     * Loads properties from a file located in the specified path.
     *
     * @param clazz the class whose package will be used to locate the properties file
     * @param name the name of the properties file (without extension)
     * @param path the path to the properties file
     * @param loadType the type of loading (ALLOW_MISSING, CREATE_FILE_IF_MISSING, MANDATORY)
     * @return a Properties object containing the loaded properties
     */
    public static final Properties loadProperties(final Class<?> clazz, final String name,
            final String path, LoadType loadType) {
        var properties = new Properties();
        loadProperties(clazz, properties, name, path, loadType);
        return properties;
    }

    /**
     * Loads properties from an external file.
     * 
     * Provides a default load type of ALLOW_MISSING.
     *
     * @param properties the Properties object to load the properties into
     * @param filePath the path to the properties file
     */
    public static void loadProperties(final Properties properties, final String filePath) {
        loadProperties(properties, filePath, LoadType.ALLOW_MISSING);
    }

    /**
     * Loads properties from an external file.
     *
     * @param properties the Properties object to load the properties into
     * @param filePath the path to the properties file
     * @param loadType the type of loading (ALLOW_MISSING, CREATE_FILE_IF_MISSING, MANDATORY)
     */
    public static void loadProperties(final Properties properties, final String filePath,
            LoadType loadType) {
        var filePathWithExtension = filePath + ".properties";

        if (loadType == LoadType.CREATE_FILE_IF_MISSING) {
            FileIO.createFileIfNotExists(filePathWithExtension);
        }

        try (var inputStream = new FileInputStream(filePathWithExtension)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            if (loadType == LoadType.MANDATORY) {
                LOGGER.severe("Failed to load properties from file: " + e.getMessage());
            }
        }
    }

    /**
     * Saves properties to an external file.
     *
     * @param properties the Properties object to save
     * @param filePath the path to the properties file
     */
    public static void saveProperties(final Properties properties, final String filePath) {
        saveProperties(properties, filePath, null);
    }

    /**
     * Saves properties to an external file.
     *
     * @param properties the Properties object to save
     * @param filePath the path to the properties file
     * @param comments comments to be added to the properties file
     */
    public static void saveProperties(final Properties properties, final String filePath, final String comments) {
        try (var outputStream = new FileOutputStream(filePath + ".properties")) {
            properties.store(outputStream, comments);
        } catch (IOException e) {
            LOGGER.severe("Failed to save properties to file: " + e.getMessage());
        }
    }

    private FileIO() {
        throw new UnsupportedOperationException("Utility class");
    }
}
