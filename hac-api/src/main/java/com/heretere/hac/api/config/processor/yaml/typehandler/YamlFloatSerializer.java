package com.heretere.hac.api.config.processor.yaml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.MultiSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class YamlFloatSerializer implements MultiSerializer<YamlConfiguration, Float> {
    @Override
    public @NotNull Float deserialize(
        final @NotNull YamlConfiguration parser,
        final @NotNull Class<?> exactType,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!parser.isDouble(key)) {
            throw new InvalidTypeException();
        }

        double configDouble = parser.getDouble(key);

        if ((float) configDouble != configDouble) {
            throw new InvalidTypeException();
        }

        return (float) configDouble;
    }

    @Override
    public @NotNull List<String> serialize(@NotNull final Object value) {
        return Lists.newArrayList(this.getGenericType().cast(value).toString());
    }

    @Override
    public @NotNull Class<Float> getGenericType() {
        return Float.class;
    }
}