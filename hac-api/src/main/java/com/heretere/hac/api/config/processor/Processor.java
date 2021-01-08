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
import com.heretere.hac.api.config.collection.ConfigList;
import com.heretere.hac.api.config.processor.toml.TomlProcessor;
import com.heretere.hac.api.config.processor.yaml.YamlProcessor;
import com.heretere.hac.api.config.structure.backend.ConfigField;
import com.heretere.hac.api.config.structure.backend.ConfigPath;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is used for processing different config file types.
 *
 * @param <T> The processing backend type.
 */
public abstract class Processor<T> {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The location of the config file.
     */
    private final @NotNull Path fileLocation;

    /**
     * A map of all the serializers for this processor.
     */
    private final @NotNull Map<Class<?>, TypeSerializer<?>> serializers;
    /**
     * A map of all the deserializers for this processor.
     */
    private final @NotNull Map<Class<?>, TypeDeserializer<T, ?>> deserializers;

    /**
     * A map of all the entries in this processor.
     */
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

    /**
     * Used to attach a serializer or deserializer to this processor.
     *
     * @param <K>     The type the serializer handles
     * @param handler The type deserializer or serializer
     */
    public final <K> void attachTypeHandler(
        final @NotNull TypeHandler<K> handler
    ) {
        if (handler instanceof TypeDeserializer) {
            this.deserializers.put(handler.getGenericType(), (TypeDeserializer<T, K>) handler);
        }

        if (handler instanceof TypeSerializer) {
            this.serializers.put(handler.getGenericType(), (TypeSerializer<K>) handler);
        }

    }

    /**
     * Retrieves a previously registered deserializer from the map.
     * If one isn't found for the exact type it tries to find a super class type.
     *
     * @param type The type to find a deserializer for
     * @return An Optional of found deserializer.
     */
    protected @NotNull Optional<TypeDeserializer<T, ?>> getDeserializer(final @NotNull Class<?> type) {
        /* Retrieve the deserializer from the map. */
        Optional<TypeDeserializer<T, ?>> optionalDeserializer = Optional.ofNullable(this.deserializers.get(type));

        /* If no deserializer was found for the type we need to do some extra searching. */
        if (!optionalDeserializer.isPresent()) {
            /* This will search through all the deserializers to find one that
             * is assignable to this type and use that instead. */
            for (TypeDeserializer<T, ?> deserializer : this.deserializers.values()) {
                if (deserializer.getGenericType().isAssignableFrom(type)) {
                    optionalDeserializer = Optional.of(deserializer);
                    break;
                }
            }
        }

        return optionalDeserializer;
    }

    /**
     * Retrieves a previously registered serializer from the map.
     * If one isn't found for the exact type it tries to find a super class type.
     *
     * @param type The type to find a serializer for
     * @return An Optional of found serializer.
     */
    protected @NotNull Optional<TypeSerializer<?>> getSerializer(final @NotNull Class<?> type) {
        /* Retrieve the serializer from the map. */
        Optional<TypeSerializer<?>> optionalSerializer = Optional.ofNullable(this.serializers.get(type));

        /* If no serializer was found for the type we need to do some extra searching. */
        if (!optionalSerializer.isPresent()) {
            /* This will search through all the serializers to find one that
             * is assignable to this type and use that instead. */
            for (TypeSerializer<?> serializer : this.serializers.values()) {
                if (serializer.getGenericType().isAssignableFrom(type)) {
                    optionalSerializer = Optional.of(serializer);
                    break;
                }
            }
        }

        return optionalSerializer;
    }

    /**
     * Deserializes a value in the backend to the config field.
     *
     * @param backend     The backend processor.
     * @param configField The config field to update.
     * @return True is the deserialization was a success.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected final boolean deserializeToField(
        final @NotNull T backend,
        final @NotNull ConfigField<?> configField
    ) {
        AtomicBoolean success = new AtomicBoolean(true);

        /* Check if config field generic type is a collection if so we need to get the generic type instead of searching
         * for a collection serializer. */
        if (ConfigList.class.isAssignableFrom(configField.getGenericType())) {
            Class<?> genericType = ((ConfigList<?>)
                configField.getValue().orElseThrow(NullPointerException::new)).getGenericType();
            this.getDeserializer(genericType).ifPresent(deserializer -> {
                /* Now we need to deserialize a collection */

                /* There should always be a collection serializer for a processor. */
                try {
                    /* Serialize the collection from the backend */
                    Collection collection = (Collection)
                        this.getDeserializer(Collection.class)
                            .orElseThrow(IllegalStateException::new)
                            .deserialize(
                                backend,
                                configField.getGenericType(),
                                configField.getKey()
                            );

                    /* Next we need to convert all the values from the collection into
                     *  Their correct type to be passed to the field */
                    ConfigList output = ConfigList.newInstance(genericType);

                    for (Object value : collection) {
                        output.add(deserializer.deserializeRaw(genericType, value));
                    }

                    configField.setValueRaw(output);
                } catch (Exception e) {
                    success.set(false);
                    this.api.getErrorHandler().getHandler().accept(e);
                }
            });
        } else {
            /* If the config field isn't a collection we can just process it regularly. */
            this.getDeserializer(configField.getGenericType()).ifPresent(deserializer -> {
                try {
                    configField.setValueRaw(deserializer.deserialize(
                        backend,
                        configField.getGenericType(),
                        configField.getKey()
                    ));
                } catch (Exception e) {
                    success.set(false);
                    this.api.getErrorHandler().getHandler().accept(e);
                }
            });
        }

        return success.get();
    }

    /**
     * Used to process and add config paths to the processor.
     *
     * @param configPath The config path to process.
     * @return true if the processing was a success.
     */
    public abstract boolean processConfigPath(@NotNull ConfigPath configPath);

    /**
     * Used to load the config file and populate the entry map with found values.
     *
     * @return true if the loading was a success.
     */
    public abstract boolean load();

    /**
     * Used to save the config file based on the entry map.
     *
     * @return true if the saving was a success.
     */
    public abstract boolean save();

    /**
     * See implementation for more information.
     *
     * @param path the path to the key
     * @return the path
     * @see TomlProcessor
     * @see YamlProcessor
     */
    protected abstract String getPathString(@NotNull String path);

    protected final @NotNull HACAPI getAPI() {
        return this.api;
    }

    protected final @NotNull Map<String, ConfigPath> getEntries() {
        return this.entries;
    }

    /**
     * @return The location of this config file.
     */
    public final @NotNull Path getFileLocation() {
        return this.fileLocation;
    }
}
