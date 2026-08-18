package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {

    private static final Properties PROPERTIES = loadProperties();

    private TestConfig() {
    }

    public static String apiBaseUrl() {
        return getProperty("api.baseUrl");
    }

    public static String uiBaseUrl() {
        return getProperty("ui.baseUrl");
    }

    private static String getProperty(String key) {
        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String configuredValue = PROPERTIES.getProperty(key);

        if (configuredValue == null || configuredValue.isBlank()) {
            throw new IllegalStateException(
                    "Missing required configuration property: " + key
            );
        }

        return configuredValue;
    }

    private static Properties loadProperties() {
        String environment = System.getProperty("env", "default");
        String resourcePath = "/" + environment + ".properties";

        Properties properties = new Properties();

        try (InputStream input =
                     TestConfig.class.getResourceAsStream(resourcePath)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Configuration file not found: " + resourcePath
                );
            }

            properties.load(input);
            return properties;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not load configuration: " + resourcePath,
                    exception
            );
        }
    }
}