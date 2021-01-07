package com.heretere.hac.api.config.processor.yaml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.MultiSerializer;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class YAMLEnumSerializer implements MultiSerializer<YamlConfiguration, Enum> {
    @Override
    public @NotNull Enum deserialize(
        final @NotNull YamlConfiguration parser,
        final @NotNull Class<?> exactType,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!exactType.isEnum()) {
            throw new InvalidTypeException();
        }

        if (!parser.isString(key)) {
            throw new InvalidTypeException();
        }

        String output = parser.getString(key);

        if (output == null) {
            throw new InvalidTypeException();
        }

        return Enum.valueOf((Class<Enum>) exactType, output.toUpperCase(Locale.ROOT));
    }

    @Override public @NotNull List<String> serialize(final @NotNull Object value) {
        return Lists.newArrayList("\"" + this.getGenericType().cast(value).name() + "\"");
    }

    @Override public @NotNull Class<Enum> getGenericType() {
        return Enum.class;
    }

}