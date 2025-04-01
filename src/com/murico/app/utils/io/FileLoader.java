package com.murico.app.utils.io;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading properties files from the classpath.
 *
 * <p>
 * This class provides a method to load a properties file into a {@link Properties} object. The
 * properties file must be located in the classpath.
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * {@code
 * Properties properties = new Properties();
 * FileLoader.loadIntoProperties("/path/to/properties/file.properties", properties);
 * }
 * </pre>
 * 
 * @author Aaron Ragudos
 * @version 1.0
 */
public class FileLoader {
  /**
   * Loads a properties file from the classpath into the provided Properties object.
   *
   * @param filePath The path to the properties file, relative to the classpath.
   * @param properties The Properties object to load the file into.
   * 
   * @throws IOException if an error occurred when reading from the input stream, or an I/O error
   *         occured.
   * @throws IllegalArgumentException if the input stream contains a malformed Unicode escape
   *         sequence.
   */
  public static void loadIntoProperties(String filePath, Properties properties)
      throws IOException, IllegalArgumentException, NullPointerException {
    var fileStream = FileLoader.class.getClassLoader().getResourceAsStream(filePath);

    assert fileStream != null : "File not found: " + filePath;

    properties.load(fileStream);
    fileStream.close();
  }
}
