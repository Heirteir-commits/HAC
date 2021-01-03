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

package com.heretere.hac.api.config.processor;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.annotations.backend.ConfigPath;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

public abstract class Processor<T> {
    private final @NotNull HACAPI api;
    private final @NotNull Path fileLocation;

    private final @NotNull Map<Class<?>, TypeSerializer<?>> serializers;
    private final @NotNull Map<Class<?>, TypeDeserializer<T, ?>> deserializers;

    private final @NotNull Map<String, ConfigPath> entries;

    protected Processor(
        final @NotNull HACAPI api,
        final @NotNull Path fileLocation
    ) {
        this.api = api;
        this.fileLocation = fileLocation;

        this.serializers = Maps.newIdentityHashMap();
        this.deserializers = Maps.newIdentityHashMap();

        this.entries = Maps.newTreeMap();
    }

    public final <K> Processor<T> attachTypeSerializer(
        final @NotNull TypeSerializer<K> serializer
    ) {
        this.serializers.put(serializer.getClassType(), serializer);
        return this;
    }

    public final <K> Processor<T> attachTypeDeserializer(
        final @NotNull TypeDeserializer<T, K> deserializer
    ) {
        this.deserializers.put(deserializer.getClassType(), deserializer);
        return this;
    }

    public abstract boolean processConfigPath(@NotNull ConfigPath configPath);

    public abstract boolean load();

    public abstract boolean save();

    protected abstract String getPathString(@NotNull String path);

    protected final @NotNull Map<Class<?>, TypeSerializer<?>> getSerializers() {
        return this.serializers;
    }

    protected final @NotNull Map<Class<?>, TypeDeserializer<T, ?>> getDeserializers() {
        return this.deserializers;
    }

    protected final @NotNull HACAPI getAPI() {
        return this.api;
    }

    protected final @NotNull Map<String, ConfigPath> getEntries() {
        return this.entries;
    }

    public final @NotNull Path getFileLocation() {
        return this.fileLocation;
    }
}
