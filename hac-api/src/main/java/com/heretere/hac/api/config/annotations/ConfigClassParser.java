package com.heretere.hac.api.config.annotations;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.annotations.backend.ConfigField;
import com.heretere.hac.api.config.annotations.backend.ConfigPath;
import com.heretere.hac.api.config.annotations.backend.ConfigSection;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class ConfigClassParser {
    private final @NotNull HACAPI api;

    public ConfigClassParser(final @NotNull HACAPI api) {
        this.api = api;
    }

    public @NotNull Map<String, ConfigPath> getConfigPaths(final @NotNull Object instance) {
        Class<?> clazz = instance.getClass();
        Map<String, ConfigPath> output = Maps.newHashMap();

        if (this.baseProcess(clazz, output)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigKey.class)) {
                    ConfigKey configKey = field.getAnnotation(ConfigKey.class);

                    ConfigField<?> configField = (ConfigField<?>) output.computeIfAbsent(
                        configKey.path(),
                        path -> new ConfigField<>(
                            this.api,
                            field.getType(),
                            instance,
                            path,
                            configKey.comments()
                        )
                    );

                    Optional<Method> setter = Arrays.stream(clazz.getMethods())
                                                    .filter(method -> method.getName().equals(configKey.setter()))
                                                    .findFirst();

                    if (setter.isPresent()) {
                        configField.setSetter(setter.get());
                    } else {
                        this.api.getErrorHandler().getHandler().accept(new NoSuchMethodException(String.format(
                            "Setter with name '%s' does not exist in class '%s'.",
                            configKey.setter(),
                            clazz.getName()
                        )));
                    }

                    Optional<Method> getter = Arrays.stream(clazz.getMethods())
                                                    .filter(method -> method.getName().equals(configKey.getter()))
                                                    .findFirst();

                    if (getter.isPresent()) {
                        configField.setGetter(getter.get());
                    } else {
                        this.api.getErrorHandler().getHandler().accept(new NoSuchMethodException(String.format(
                            "Getter with name '%s' does not exist in class '%s'.",
                            configKey.getter(),
                            clazz.getName()
                        )));
                    }
                }
            }
        }

        return output;
    }

    private boolean baseProcess(
        final @NotNull Class<?> clazz,
        final @NotNull Map<String, ConfigPath> output
    ) {
        boolean valid = clazz.isAnnotationPresent(ConfigFile.class);

        if (valid && clazz.isAnnotationPresent(Section.class)) {
            Section section = clazz.getAnnotation(Section.class);
            output.computeIfAbsent(
                section.key(),
                path -> new ConfigSection(
                    this.api,
                    path,
                    section.comments()
                )
            );
        }

        return valid;
    }

}
