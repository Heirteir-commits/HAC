package com.heretere.hac.api.config.processor.toml.serialization;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.TypeDeserializer;
import com.heretere.hac.api.config.processor.TypeSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.jetbrains.annotations.NotNull;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.util.List;

public final class TomlStringHybridHandler
    implements TypeSerializer<String>, TypeDeserializer<TomlParseResult, String> {
    @Override public @NotNull String deserialize(
        final @NotNull TomlParseResult parser,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!parser.isString(key)) {
            throw new InvalidTypeException();
        }

        String output = parser.getString(key);

        if (output == null) {
            throw new InvalidTypeException();
        }

        return output;
    }

    @Override public @NotNull List<String> serialize(final @NotNull Object value) {
        return Lists.newArrayList(Toml.tomlEscape(this.getClassType().cast(value)).toString());
    }

    @Override public @NotNull Class<String> getClassType() {
        return String.class;
    }
}
