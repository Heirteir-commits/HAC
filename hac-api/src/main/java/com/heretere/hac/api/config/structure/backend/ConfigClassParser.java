/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.api.config.structure.backend;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.structure.annotation.Comment;
import com.heretere.hac.api.config.structure.annotation.ConfigFile;
import com.heretere.hac.api.config.structure.annotation.Key;
import com.heretere.hac.api.config.structure.annotation.Section;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is responsible for processing config annotations in classes.
 */
public final class ConfigClassParser {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;

    /**
     * @param api The HACAPI reference.
     */
    public ConfigClassParser(final @NotNull HACAPI api) {
        this.api = api;
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

    /**
     * Converts the annotations in an object to config paths that can be added to a processor.
     *
     * @param instance The instance that contains the annotations.
     * @return A Map of the found config paths.
     */
    public @NotNull Map<String, ConfigPath> getConfigPaths(final @NotNull Object instance) {
        Class<?> clazz = instance.getClass();
        Map<String, ConfigPath> output = Maps.newHashMap();

        if (this.baseProcess(clazz, output)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Key.class)) {
                    Key key = field.getAnnotation(Key.class);

                    List<String> comments = Lists.newArrayList();
                    comments.addAll(ConfigClassParser.getComments(field));

                    ReflectiveConfigField<?> configField = (ReflectiveConfigField<?>) output.computeIfAbsent(
                        key.value(),
                        path -> new ReflectiveConfigField<>(
                            this.api,
                            path,
                            comments,
                            field.getType(),
                            instance
                        )
                    );

                    configField.setField(field);
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

            List<String> comments = Lists.newArrayList();
            comments.addAll(ConfigClassParser.getComments(clazz));

            output.put(section.value(), output.computeIfAbsent(
                section.value(),
                key -> new ConfigSection(key, comments)
            ));
        }

        return valid;
    }

}
