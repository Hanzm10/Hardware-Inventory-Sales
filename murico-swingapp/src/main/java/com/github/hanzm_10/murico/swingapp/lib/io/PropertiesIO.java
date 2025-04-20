package com.github.hanzm_10.murico.swingapp.lib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import com.github.hanzm_10.murico.swingapp.lib.logger.MuricoLogger;

public final class PropertiesIO {
    private static final Logger LOGGER = MuricoLogger.getLogger(PropertiesIO.class);
    public static final String PROPERTIES_FILE_EXTENSION = ".propertie";

    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into the provided {@link Properties}
     * object.
     *
     * @param clazz The class to load the properties file from.
     * @param properties The properties object to load the properties into.
     * @param name The name of the properties file (without extension).
     *
     * @throws IllegalArgumentException If the name is null or empty.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", FileLoadLeniency.MANDATORY);
    }

    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into the provided {@link Properties}
     * object.
     *
     * @param clazz The class to load the properties file from.
     * @param properties The properties object to load the properties into.
     * @param name The name of the properties file (without extension).
     *
     * @throws IllegalArgumentException If the name is null or empty.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name,
            FileLoadLeniency leniency)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", leniency);
    }

    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into the provided {@link Properties}
     * object.
     *
     * @param clazz The class to load the properties file from.
     * @param properties The properties object to load the properties into.
     * @param name The name of the properties file (without extension).
     * @param path The path to the properties file (relative to the classpath).
     *
     * @throws IllegalArgumentException If the name is null or empty or if the path is null.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final void loadProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name,
            @NotNull final String path)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, path, FileLoadLeniency.MANDATORY);
    }

    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into the provided {@link Properties}
     * object.
     *
     * @param clazz The class to load the properties file from.
     * @param properties The properties object to load the properties into.
     * @param name The name of the properties file (without extension).
     * @param path The path to the properties file (relative to the classpath).
     * @param leniency The leniency level for loading the properties file.
     *
     * @throws IllegalArgumentException If the name is null or empty or if the path is null.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
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
                        // as we don't know the path of the clazz
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



    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into a new {@link Properties} object and
     * returned.
     *
     * @param clazz The class to load the properties file from.
     * @param name The name of the properties file (without extension).
     *
     * @return A new {@link Properties} object with the loaded properties.
     *
     * @throws IllegalArgumentException If the name is null or empty.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz,
            @NotNull final String name) throws IllegalArgumentException,
    FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name);

        return properties;
    }

    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into a new {@link Properties} object and
     * returned.
     *
     * @param clazz The class to load the properties file from.
     * @param name The name of the properties file (without extension).
     * @param path The path to the properties file (relative to the classpath).
     *
     * @return A new {@link Properties} object with the loaded properties.
     *
     * @throws IllegalArgumentException If the name is null or empty or if the path is null.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz,
            @NotNull final String name, @NotNull final String path)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name, path);

        return properties;
    }

    /**
     * Loads a properties file from the classpath. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be loaded into a new {@link Properties} object and
     * returned.
     *
     * @param clazz The class to load the properties file from.
     * @param name The name of the properties file (without extension).
     * @param path The path to the properties file (relative to the classpath).
     * @param leniency The leniency level for loading the properties file.
     *
     * @return A new {@link Properties} object with the loaded properties.
     *
     * @throws IllegalArgumentException If the name is null or empty or if the path is null.
     * @throws FileNotFoundException If the properties file is not found and leniency is set to
     *         MANDATORY.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz,
            @NotNull final String name, @NotNull final String path, FileLoadLeniency leniency)
                    throws IllegalArgumentException, FileNotFoundException, IOException {
        var properties = new Properties();

        loadProperties(clazz, properties, name, path, leniency);

        return properties;
    }

    /**
     * Loads a properties file from the file system. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is found, it will be loaded into a new {@link Properties} object and
     * returned.
     *
     * @param filePath The path to the properties file (relative to the classpath).
     *
     * @return A new {@link Properties} object with the loaded properties.
     *
     * @throws FileNotFoundException If the properties file is not found.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final Properties loadPropertiesFromFileSystem(@NotNull final String filePath)
            throws FileNotFoundException, IOException {
        var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties);
        return properties;
    }

    /**
     * Loads a properties file from the file system. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is found, it will be loaded into the provided {@link Properties}
     * object.
     *
     * @param filePath The path to the properties file (relative to the classpath).
     * @param properties The properties object to load the properties into.
     *
     * @throws FileNotFoundException If the properties file is not found.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final void loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final Properties properties) throws FileNotFoundException, IOException {
        try (var inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        }
    }

    /**
     * Saves the properties to a file. <br>
     * <br>
     * The properties file is expected to be in the same package as the class provided. <br>
     * <br>
     * If the properties file is not found, the behavior depends on the {@link FileLoadLeniency}
     * parameter. <br>
     * <br>
     * If the properties file is found, it will be saved to the provided {@link Properties} object.
     *
     * @param clazz The class to load the properties file from.
     * @param properties The properties object to load the properties into.
     * @param name The name of the properties file (without extension).
     *
     * @throws IllegalArgumentException If the name is null or empty.
     * @throws FileNotFoundException If the properties file is not found.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static final void saveProperties(@NotNull final Class<?> clazz,
            @NotNull final Properties properties, @NotNull final String name,
            @NotNull final String path)
            throws IllegalArgumentException,
    FileNotFoundException, IOException {
        URI uri = null;
        var filePath = path + name + PROPERTIES_FILE_EXTENSION;

        try {
            var url = clazz.getResource(filePath);

            if (url == null) {
                throw new FileNotFoundException("Properties file not found: " + filePath + " in class: " + clazz.getName());
            }

            uri = url.toURI();
        } catch (URISyntaxException e) {
            LOGGER.warning("Failed to convert URL to URI: " + e.getMessage());
            return;
        }

        var file = new File(uri);

        try (var outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, null);
        } catch (SecurityException e) {
            LOGGER.severe("Denied access to file: " + file.getAbsolutePath());
        } catch (ClassCastException e) {
            LOGGER.log(Level.SEVERE, "Configuration file in " + file.getAbsolutePath() + " contains invalid data. ", e);
        }
    }

    private PropertiesIO() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
