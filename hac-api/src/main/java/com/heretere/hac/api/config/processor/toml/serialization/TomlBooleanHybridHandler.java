package com.heretere.hac.api.config.processor.toml.serialization;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.TypeDeserializer;
import com.heretere.hac.api.config.processor.TypeSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.jetbrains.annotations.NotNull;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.util.List;

public final class TomlBooleanHybridHandler
    implements TypeSerializer<Boolean>, TypeDeserializer<TomlParseResult, Boolean> {

    @Override public @NotNull Boolean deserialize(
        final @NotNull TomlParseResult parser,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!parser.isBoolean(key)) {
            throw new InvalidTypeException();
        }

        Boolean output = parser.getBoolean(key);

        if (output == null) {
            throw new InvalidTypeException();
        }

        return output;
    }

    @Override public @NotNull List<String> serialize(final @NotNull Object value) {
        return Lists.newArrayList(Toml.tomlEscape(this.getClassType().cast(value).toString()).toString());
    }

    @NotNull @Override public Class<Boolean> getClassType() {
        return Boolean.class;
    }
}
