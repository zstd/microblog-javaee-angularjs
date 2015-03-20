package com.example.zstd.microblog.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

/**
 * Class for locating service/repo instances.
 */
public class ServiceLocator {

    private static ServiceLocator instance;
    private Map<Class,Object> services;

    public static final ServiceLocator getInstance() {
        if(instance == null) {
            throw new IllegalStateException("ServiceLocator not initialized");
        }
        return instance;
    }

    public static void initialize(Map<Class,Object> services) {
        if(instance != null) {
            throw new IllegalStateException("ServiceLocator already initialized");
        }
        instance = new ServiceLocator();
        instance.services = ImmutableMap.copyOf(services);
    }

    public <T> T getService(Class<T> type) {
        Preconditions.checkArgument(type != null, "Service type is null");
        T result = (T)services.get(type);
        if(result == null) {
            throw new IllegalArgumentException("Not found service of class " + type.getName());
        }
        return result;
    }


}
