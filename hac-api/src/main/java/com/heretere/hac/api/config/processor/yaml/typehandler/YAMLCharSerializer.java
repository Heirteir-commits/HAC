package com.heretere.hac.api.config.processor.yaml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.MultiSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class YAMLCharSerializer implements MultiSerializer<YamlConfiguration, Character> {
    @Override
    public @NotNull Character deserialize(
        final @NotNull YamlConfiguration parser,
        final @NotNull Class<?> exactType,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (key.length() > 1) {
            throw new InvalidTypeException();
        }

        return key.charAt(0);
    }

    @Override
    public @NotNull List<String> serialize(@NotNull final Object value) {
        return Lists.newArrayList("'" + this.getGenericType().cast(value) + "'");
    }

    @Override
    public @NotNull Class<Character> getGenericType() {
        return Character.class;
    }
}