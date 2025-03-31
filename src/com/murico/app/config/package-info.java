/**
 * Provides configuration settings for the application.
 * 
 * <p>
 * The {@link com.murico.app.config.AppSettings} class is responsible for loading application
 * properties from a configuration file and providing access to various settings such as fonts,
 * colors, screen dimensions, and border properties.
 * 
 * <p>
 * It follows a singleton pattern, ensuring that configuration values are consistently accessible
 * throughout the application.
 * 
 * <h2>Features:</h2>
 * <ul>
 * <li>Loads properties from a {@code config.properties} file.</li>
 * <li>Provides application-wide font settings.</li>
 * <li>Manages primary and secondary color schemes.</li>
 * <li>Defines UI layout properties such as screen dimensions and border styles.</li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * 
 * <pre>
 * AppSettings settings = AppSettings.getInstance();
 * String title = settings.getAppTitle();
 * Font mainFont = settings.getMainFont();
 * Color primaryColor = settings.getPrimaryColor();
 * </pre>
 * 
 * @see com.murico.app.config.AppSettings
 */
package com.murico.app.config;