package pl.nlpday.workshop.elastic.common;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final Logger LOG = Logger.getGlobal();

    private static final URL URL_CONFIG_FILE = Config.class.getClassLoader().getResource("elastic.properties");
    private static final URL URL_SETTINGS_FILE = Config.class.getClassLoader().getResource("elastic-settings.json");
    private static final URL URL_MAPPING_FILE = Config.class.getClassLoader().getResource("elastic-mapping.json");
    private static Properties properties;

    static {
        properties = new Properties();

        try(InputStream stream = Objects.requireNonNull(URL_CONFIG_FILE).openStream()) {
            properties.load(stream);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e.getCause());
        }

        File confFile = new File("config/jsa-elastic.properties");
        if(confFile.exists()) {
            try (InputStream fStream = new FileInputStream(confFile)) {
                properties.load(fStream);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getJSON(URL fileLocator) {
        String json = null;

        try(InputStream stream = Objects.requireNonNull(fileLocator).openStream()) {
            json = IOUtils.toString(stream, "UTF-8");

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e.getCause());
        }

        return json;
    }

    public static int getInt(String propertyName) {
        return Integer.parseInt(properties.getProperty(propertyName));
    }

    public static String getString(String propertyName) {
        return properties.getProperty(propertyName);
    }


    public static String getSettings() {
        return getJSON(URL_SETTINGS_FILE);
    }

    public static String getMappings() {
        return getJSON(URL_MAPPING_FILE);
    }
}
