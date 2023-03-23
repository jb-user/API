package com.coherent.api.training.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "/Users/juliabokhan/IdeaProjects/API-Training-Project/src/main/resources/config.properties";
    private static ConfigReader instance;
    private static final Properties props = new Properties();

    private ConfigReader() {
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                instance = new ConfigReader();
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
