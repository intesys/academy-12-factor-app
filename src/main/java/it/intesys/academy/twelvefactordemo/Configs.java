package it.intesys.academy.twelvefactordemo;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Configs {

    static Properties properties = new Properties();
    static {
        try {

            Properties classPathProperties = new Properties();
            Properties localProperties = new Properties();
            Properties fileSystemProperties = new Properties();

            // read properties from the application.properties
            InputStream resource = Configs.class.getClassLoader()
                    .getResourceAsStream("application.properties");
            classPathProperties.load(resource);

            // read properties from the non versioned application-local.properties
            InputStream localResource = Configs.class.getClassLoader()
                    .getResourceAsStream("application-local.properties");
            if (localResource != null) {
                localProperties.load(localResource);
            }

            //read properties from file system
            Path fileSystemPropertiesPath = Path.of("config", "application.properties");
            if (Files.exists(fileSystemPropertiesPath)) {
                InputStream config = Files.newInputStream(fileSystemPropertiesPath);
                fileSystemProperties.load(config);
            }

            // file system properties override classpath properties
            properties.putAll(classPathProperties);
            properties.putAll(localProperties);
            properties.putAll(fileSystemProperties);

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

        return null;
    }

    public static Integer getIntegerProperty(String property) {

        String prop = System.getenv(property);

        if (prop != null) {
            return Integer.parseInt(prop);
        }

        if (properties.getProperty(property) != null) {
            return Integer.parseInt(properties.getProperty(property));
        }

        return null;
    }

}
