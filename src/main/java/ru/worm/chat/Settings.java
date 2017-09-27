package ru.worm.chat;

import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Settings {
    private static Logger log = LoggerFactory.getLogger(Settings.class);
    public static Properties properties;
    public static Properties messages;
    public static final String jarDirectory;
    static {
        String path = Settings.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = (new File(path)).getAbsolutePath();
        path = path.substring(0, path.lastIndexOf(File.separator)) + File.separator;
        jarDirectory = path;
    }

    private final void Settings() {
    }

    private static void initSettings() {
        properties = new Properties();
        messages = new Properties();
        try {
            String path = jarDirectory + "vtbdatacorrection.properties";
            properties.load(new FileInputStream(path));
            messages.load(new InputStreamReader(Settings.class.getClassLoader().getResourceAsStream("vtbmessages.properties"), "utf-8"));
        } catch (Exception e) {
            log.error("", e);
            log.error("Can not read application properties 'vtbdatacorrection.properties' or 'vtbmessages.properties'");
            System.exit(1);
        }
    }

    public static Properties getProperties() {
        if (properties == null)
            synchronized (Settings.class) {
                if (properties == null)
                    initSettings();
            }
        return properties;
    }

    public static Properties getMessages() {
        if (messages == null)
            synchronized (Settings.class) {
                if (messages == null)
                    initSettings();
            }
        return messages;
    }

    public static String getProperty(@NotNull String key) {
        return getProperty(key, null);
    }

    public static String getProperty(@NotNull String key, String defaultProperty) {
        Properties prop = getProperties();
        String property;
        if((property = prop.getProperty(key))!=null)
            return property;
        else if(defaultProperty!=null)
            return defaultProperty;
        else {
            log.warn("Property '" + key + "' doesn't set; null is returned.");
            return null;
        }
    }

    public static int getInteger(@NotNull String key) {
        return getInteger(key, null);
    }

    public static int getInteger(@NotNull String key, String defalutValue) {
        try {
            return Integer.parseInt(getProperty(key, defalutValue));
        } catch (NumberFormatException e) {
            log.error("'" + key + "' setting is wrong, or is absent with default value '" + defalutValue + "'");
            throw e;
        }
    }

    public static String getMessage(@NotNull String key) { return getMessage(key, null); }

    public static String getMessage(@NotNull String key, String defaultMessage) {
        Properties prop = getMessages();
        String message;
        if((message = prop.getProperty(key))!=null)
            return message;
        else if(defaultMessage!=null)
            return defaultMessage;
        else throw new RuntimeException("Не существует сообщения " + key);
    }

    public static void setProperty(String propName, String value) {
        if(properties != null && value != null) {
            properties.setProperty(propName, value);
        }
    }
}
