package com.heretere.hac.api.util.reflections.helper;

import com.google.common.collect.Maps;

import java.util.Map;

public final class HelperManager {
    private final Map<Class<?>, Object> helpers;

    public HelperManager() {
        this.helpers = Maps.newHashMap();
    }

    public <T> void registerHelper(Class<T> clazz, T helper) {
        this.helpers.put(clazz, helper);
    }

    public <T> void unregisterHelper(Class<T> clazz) {
        this.helpers.remove(clazz);
    }

    public <T> T getHelper(Class<T> clazz) {
        return clazz.cast(this.helpers.get(clazz));
    }
}
