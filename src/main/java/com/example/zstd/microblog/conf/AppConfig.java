package com.example.zstd.microblog.conf;

import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class for holding application storage
 */
public final class AppConfig {

    private static final Logger LOG = Logger.getLogger(AppConfig.class.getName());

    private static final String ERROR = "Failed to load app config. ";

    public static AppConfig createAppConfig() {
        InputStream confIs = getConfigIS();
        Properties properties = new Properties();
        try {
            properties.load(confIs);
            LOG.info("loaded properties " + properties.toString());
            return new AppConfig(properties);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(ERROR +"IOException: " + e.getMessage());
        }
    }

    private static InputStream getConfigIS() {
        String confFileLocation = System.getProperty(Constants.SYSTEM_PROP_CONFIG_LOCATION_URL);

        if(Strings.isNullOrEmpty(confFileLocation)) {
            LOG.info("Config file location from system property is empty, using default");
            return AppConfig.class.getClassLoader().getResourceAsStream("default.properties");
        } else {
            try {
                URL resolvedConfUrl = new URL(confFileLocation);
                return resolvedConfUrl.openStream();
            } catch (MalformedURLException e) {
                throw new ExceptionInInitializerError(ERROR +"Invalid URL: " + confFileLocation);
            } catch (IOException e) {
                throw new ExceptionInInitializerError(ERROR +"IOException: " + e.getMessage());
            }
        }
    }

    public enum Param {
        JDBC_URL("jdbc.url",true),
        JDBC_DRIVER("jdbc.driver",true);

        private boolean required;
        private String propValue;

        Param(String propValue, boolean required) {
            this.propValue = propValue;
            this.required = required;
        }
    }

    private Properties properties;

    public AppConfig(Properties properties) {
        this.properties = properties;
    }

    public String getStringParam(Param param) {
        return getParam(String.class,param);
    }

    public <T> T getParam(Class<T> clazz,Param param) {
        return (T)getParam(param);
    }

    public Object getParam(Param param) {
        Object result = properties.getProperty(param.propValue);
        if(param.required && result == null) {
            throw new IllegalStateException(String.format("Required param '%s' is null",param.propValue));
        }
        return result;
    }
}
