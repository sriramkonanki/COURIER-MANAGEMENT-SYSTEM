package com.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads framework configuration from src/main/resources/config.properties.
 * Values can be overridden at runtime via -D system properties, e.g.
 *   mvn test -Dbrowser=firefox -DgridUrl=http://node2:4444/wd/hub
 */
public class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    /**
     * Returns a config value, giving precedence to a JVM system property
     * with the same key if one is set.
     */
    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    public static String getGridUrl() {
        // Allow -DgridUrl=... as a convenient alias for grid.url
        String override = System.getProperty("gridUrl");
        return (override != null && !override.isEmpty()) ? override : get("grid.url");
    }

    public static String getBrowser() {
        String override = System.getProperty("browser");
        return (override != null && !override.isEmpty()) ? override : get("default.browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(get("implicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
    }

    public static String getBaseUrl() {
        return get("base.url");
    }
}
