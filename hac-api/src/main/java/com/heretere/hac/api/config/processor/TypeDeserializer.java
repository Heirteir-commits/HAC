package com.heretere.hac.api.config.processor;

import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.jetbrains.annotations.NotNull;

public interface TypeDeserializer<T, K> extends TypeDefinition<K> {
    @NotNull K deserialize(
        @NotNull T parser,
        @NotNull String key
    ) throws InvalidTypeException;
}
