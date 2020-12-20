package com.heretere.hac.api.player.builder;

import com.google.common.collect.Maps;
import com.heretere.hac.api.player.HACPlayer;

import java.util.Map;

/**
 * This class is used by {@link HACPlayer} to handle all dynamic data that may be created for it.
 */
public final class DataManager {
    private final Map<Class<?>, Object> data;

    /**
     * Instantiates a new Data manager.
     */
    public DataManager() {
        this.data = Maps.newIdentityHashMap(); //Since we are only comparing classes identity hashmap is beneficial here for speed
    }

    /**
     * Add a new Data type to the Data Manager.
     *
     * @param clazz the class
     * @param data  the data
     * @param <T>   the type of the data class
     */
    public <T> void addDataRaw(Class<T> clazz, Object data) {
        this.addData(clazz, clazz.cast(data));
    }

    private <T> void addData(Class<T> clazz, T data) {
        this.data.put(clazz, data);
    }

    /**
     * Removes a Data type from the Data Manager.
     *
     * @param clazz the class
     */
    public void removeData(Class<?> clazz) {
        this.data.remove(clazz);
    }

    /**
     * Gets a Data instance from the Data Manager.
     *
     * @param <T>   the type parameter
     * @param clazz the class
     * @return the data
     */
    public <T> T getData(Class<T> clazz) {
        return clazz.cast(this.data.get(clazz));
    }
}
