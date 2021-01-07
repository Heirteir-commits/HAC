package com.heretere.hac.api.config.processor.yaml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.MultiSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class YAMLBooleanSerializer implements MultiSerializer<YamlConfiguration, Boolean> {
    @Override
    public @NotNull Boolean deserialize(
        final @NotNull YamlConfiguration parser,
        final @NotNull Class<?> exactType,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!parser.isBoolean(key)) {
            throw new InvalidTypeException();
        }

        return parser.getBoolean(key);
    }

    @Override
    public @NotNull List<String> serialize(@NotNull final Object value) {
        return Lists.newArrayList(this.getGenericType().cast(value).toString());
    }

    @Override
    @NotNull
    public Class<Boolean> getGenericType() {
        return Boolean.class;
    }

}