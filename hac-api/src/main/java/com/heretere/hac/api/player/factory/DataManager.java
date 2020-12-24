package com.heretere.hac.api.player.factory;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * This class is used by {@link com.heretere.hac.api.player.HACPlayer} to handle all dynamic data that may be created
 * for it.
 */
public final class DataManager {
    /**
     * A map of the data objects attached ot his data manager.
     */
    private final @NotNull Map<Class<?>, Object> data;

    /**
     * Instantiates a new Data manager.
     */
    public DataManager() {
        //Since we are only comparing classes identity hashmap is beneficial here for speed
        this.data = Maps.newIdentityHashMap();
    }

    /**
     * Add a new Data type to the Data Manager.
     *
     * @param clazz the class
     * @param data  the data
     * @param <T>   the type of the data class
     */
    public <T> void addDataRaw(
        final @NotNull Class<T> clazz,
        final @NotNull Object data
    ) {
        this.addData(clazz, clazz.cast(data));
    }

    private <T> void addData(
        final @NotNull Class<T> clazz,
        final @NotNull T data
    ) {
        this.data.put(clazz, data);
    }

    /**
     * Removes a Data type from the Data Manager.
     *
     * @param clazz the class
     */
    public void removeData(final @NotNull Class<?> clazz) {
        this.data.remove(clazz);
    }

    /**
     * Gets a Data instance from the Data Manager.
     *
     * @param <T>   the type parameter
     * @param clazz the class
     * @return the data
     */
    public @NotNull <T> Optional<T> getData(final @NotNull Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(this.data.get(clazz)));
    }
}
