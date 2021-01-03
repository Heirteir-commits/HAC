package com.heretere.hac.api.config.annotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.annotations.backend.ConfigField;
import com.heretere.hac.api.config.annotations.backend.ConfigPath;
import com.heretere.hac.api.config.annotations.backend.ConfigSection;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                if (field.isAnnotationPresent(Key.class)) {
                    Key key = field.getAnnotation(Key.class);

                    ConfigField<?> configField = (ConfigField<?>) output.computeIfAbsent(
                        key.value(),
                        path -> new ConfigField<>(this.api, field.getType(), instance, path)
                    );

                    configField.setField(field);
                    ConfigClassParser.getComments(field).forEach(configField::addComment);
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
            ConfigSection sectionBackend = (ConfigSection) output.computeIfAbsent(
                section.value(),
                key -> new ConfigSection(this.api, key)
            );


            ConfigClassParser.getComments(clazz).forEach(sectionBackend::addComment);
        }

        return valid;
    }

    private static List<String> getComments(final @NotNull AnnotatedElement element) {
        List<String> comments = Lists.newArrayList();

        if (element.isAnnotationPresent(Comment.class)) {
            comments.add(element.getAnnotation(Comment.class).value());
        }

        if (element.isAnnotationPresent(Comment.List.class)) {
            comments.addAll(Lists.newArrayList(Arrays.stream(element.getAnnotation(Comment.List.class).value())
                                                     .map(Comment::value)
                                                     .collect(Collectors.toList())));
        }

        return comments;
    }

}
