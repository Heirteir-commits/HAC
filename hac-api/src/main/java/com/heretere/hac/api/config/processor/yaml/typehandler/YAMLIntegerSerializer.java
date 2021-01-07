package com.heretere.hac.api.config.processor.yaml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.MultiSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class YAMLIntegerSerializer implements MultiSerializer<YamlConfiguration, Integer> {
    @Override
    public @NotNull Integer deserialize(
        final @NotNull YamlConfiguration parser,
        final @NotNull Class<?> exactType,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!parser.isInt(key)) {
            throw new InvalidTypeException();
        }

        return parser.getInt(key);
    }

    @Override
    public @NotNull List<String> serialize(@NotNull final Object value) {
        return Lists.newArrayList(this.getGenericType().cast(value).toString());
    }

    @Override
    public @NotNull Class<Integer> getGenericType() {
        return Integer.class;
    }
}
