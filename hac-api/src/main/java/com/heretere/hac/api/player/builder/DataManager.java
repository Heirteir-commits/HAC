package com.heretere.hac.api.player.builder;

import com.google.common.collect.Maps;

import java.util.Map;

public final class DataManager {
    private final Map<Class<?>, Object> data;

    public DataManager() {
        this.data = Maps.newHashMap();
    }

    public void addData(Class<?> clazz, Object data) {
        this.data.put(clazz, data);
    }

    public void removeData(Class<?> clazz) {
        this.data.remove(clazz);
    }

    public <T> T getData(Class<T> clazz) {
        return clazz.cast(this.data.get(clazz));
    }

}
