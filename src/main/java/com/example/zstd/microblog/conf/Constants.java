package com.example.zstd.microblog.conf;

/**
 * Holder for different system constants.
 */
public final class Constants {

    private Constants(){}

    private static final String CONF_PREFIX = "com.example.zstd.microblog.conf.";

    /**
     * Name of system property that can be provided to app to set custom config file location.
     * URL format used.
     */
    public static final String SYSTEM_PROP_CONFIG_LOCATION_URL = CONF_PREFIX + "location";

}
