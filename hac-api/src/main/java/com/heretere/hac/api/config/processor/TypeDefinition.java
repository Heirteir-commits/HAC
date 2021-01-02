package com.heretere.hac.api.config.processor;

import org.jetbrains.annotations.NotNull;

public interface TypeDefinition<T> {
    @NotNull Class<T> getClassType();
}
