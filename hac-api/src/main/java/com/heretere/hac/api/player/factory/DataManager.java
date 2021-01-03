/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
