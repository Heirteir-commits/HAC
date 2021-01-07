package com.heretere.hac.api.config.processor.yaml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.MultiSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class YAMLStringSerializer implements MultiSerializer<YamlConfiguration, String> {
    @Override
    public @NotNull String deserialize(
        final @NotNull YamlConfiguration parser,
        final @NotNull Class<?> exactType,
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

    @Override
    public @NotNull List<String> serialize(@NotNull final Object value) {
        return Lists.newArrayList('"' + this.getGenericType().cast(value) + '"');
    }

    @Override
    public @NotNull Class<String> getGenericType() {
        return String.class;
    }
}