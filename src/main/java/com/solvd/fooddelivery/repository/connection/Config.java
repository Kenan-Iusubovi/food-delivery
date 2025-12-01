package com.solvd.fooddelivery.repository.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Config {

    URL("url"),
    DRIVER("driver"),
    USERNAME("username"),
    PASSWORD("password"),
    POOL_SIZE("poolSize", String.valueOf(1));

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final Properties PROPERTIES;

    static {
        PROPERTIES = loadProperties();
    }

    private final String key;
    private String defaultValue;

    Config(String key) {

        this.key = key;
    }

    Config(String key, String defaultValue) {

        this(key);
        this.defaultValue = defaultValue;
    }

    public String getValue() {

        return PROPERTIES.getProperty(key, defaultValue);
    }

    private static Properties loadProperties() {

        Properties config = new Properties();
        try (InputStream inputStream = Config.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE_NAME)) {

            config.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
