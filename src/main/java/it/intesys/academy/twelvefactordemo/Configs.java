package it.intesys.academy.twelvefactordemo;

import java.io.InputStream;
import java.util.Properties;

public class Configs {

    static Properties properties;
    static {
        try {
            // read properties from the application.properties
            properties = new Properties();
            InputStream resource = Configs.class.getClassLoader()
                    .getResourceAsStream("application.properties");
            properties.load(resource);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read properties", e);
        }
    }

    public static String getStringProperty(String property) {

        String prop = System.getenv(property);
        if (prop != null) {
            return prop;
        }

        if (properties.getProperty(property) != null) {
            return properties.getProperty(property);
        }
        throw new RuntimeException("Property " + property + " not found");
    }

    public static Integer getIntegerProperty(String property) {

        String prop = System.getenv(property);

        if (prop != null) {
            return Integer.parseInt(prop);
        }

        if (properties.getProperty(property) != null) {
            return Integer.parseInt(properties.getProperty(property));
        }

        throw new RuntimeException("Property " + property + " not found");
    }

}
