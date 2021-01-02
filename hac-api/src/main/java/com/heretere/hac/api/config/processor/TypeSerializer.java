package com.heretere.hac.api.config.processor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TypeSerializer<T> extends TypeDefinition<T> {
    @NotNull List<String> serialize(@NotNull Object value);
}
