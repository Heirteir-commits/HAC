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

package com.heretere.hac.api.config.processor.toml;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.collection.ConfigList;
import com.heretere.hac.api.config.processor.Processor;
import com.heretere.hac.api.config.processor.toml.typehandler.TomlBooleanSerializer;
import com.heretere.hac.api.config.processor.toml.typehandler.TomlRawCollectionSerializer;
import com.heretere.hac.api.config.processor.toml.typehandler.TomlRawEnumSerializer;
import com.heretere.hac.api.config.processor.toml.typehandler.TomlStringSerializer;
import com.heretere.hac.api.config.structure.backend.ConfigField;
import com.heretere.hac.api.config.structure.backend.ConfigPath;
import com.heretere.hac.api.config.structure.backend.ConfigSection;
import com.heretere.hac.api.config.structure.backend.ReflectiveConfigField;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TomlProcessor extends Processor<TomlParseResult> {
    /**
     * The processing backend for the TomlProcessor.
     */
    private @Nullable TomlParseResult toml;

    /**
     * This is used to tell the save method if the config was loaded correctly.
     * If the load failed we don't want to overwrite the save file with a potentially corrupted config.
     */
    private boolean loadSuccess;

    /**
     * Creates a new TomlProcessor.
     *
     * @param api      The HACAPI reference.
     * @param location the location of the config file.
     */
    public TomlProcessor(
        final @NotNull HACAPI api,
        final @NotNull Path location
    ) {
        super(api, location);

        this.loadSuccess = false;

        this.createDefaultHandlers();
    }

    /**
     * Attaches pre made serializers to this processor.
     */
    private void createDefaultHandlers() {
        super.attachTypeHandler(new TomlStringSerializer());
        super.attachTypeHandler(new TomlBooleanSerializer());
        super.attachTypeHandler(new TomlRawEnumSerializer());
        super.attachTypeHandler(new TomlRawCollectionSerializer());
    }

    /**
     * Creates a header for a config key if one isn't already in the entries.
     *
     * @param path The config path to generate a parent path for.
     */
    private void attachSectionParent(final @NotNull ConfigPath path) {
        /* We only want the parent section for toml files */
        String parentPath = StringUtils.substringBeforeLast(path.getKey(), ".");

        /* No reason to put in a new section if one is already there */
        if (!parentPath.isEmpty() && !path.getKey().equals(parentPath) && !super.getEntries().containsKey(parentPath)) {
            super.getEntries().put(parentPath, new ConfigSection(parentPath));
        }
    }

    @Override public boolean processConfigPath(final @NotNull ConfigPath configPath) {
        boolean success = true;

        /* If the config path is a section we need to do some extra processing. */
        if (configPath instanceof ConfigField) {
            /* Attaches the header to a config value if one doesn't exist */
            this.attachSectionParent(configPath);

            /* Puts the config path in the entries map. */
            super.getEntries().put(configPath.getKey(), configPath);

            /* If the toml file contains this key, we deserialize the value to the config field. */
            if (this.toml != null && this.toml.contains(configPath.getKey())) {
                success = super.deserializeToField(this.toml, (ConfigField<?>) configPath);
            }
        } else {
            this.getEntries().put(configPath.getKey(), configPath);
        }

        this.loadSuccess = success;
        return success;
    }

    /**
     * Self explanatory.
     *
     * @throws IOException If there is an issue creating the file.
     */
    private void createFileIfNotExists() throws IOException {
        if (!Files.exists(super.getFileLocation())) {
            Files.createDirectories(super.getFileLocation().getParent());
            Files.createFile(super.getFileLocation());
        }
    }

    @Override public boolean load() {
        boolean success = true;

        try {
            /* Create file */
            this.createFileIfNotExists();

            /* Parse the file so we can process it. */
            this.toml = Toml.parse(super.getFileLocation());

            /* We need to loop through all the keys in the toml file so we can add them as entries to the config map.*/
            this.toml.dottedKeySet(false).forEach(key -> {
                if (this.toml.isTable(key)) {
                    /* Add section to entries. */
                    super.getEntries().put(key, new ConfigSection(key));
                } else {
                    /* If the entry is already registered we need to update it, instead of creating a new one. */
                    ConfigField<?> configField = super.getEntries().containsKey(key)
                        ? (ConfigField<?>) super.getEntries().get(key)
                        : new ReflectiveConfigField<>(
                            super.getAPI(),
                            key,
                            Lists.newArrayList(),
                            Object.class,
                            null
                        );

                    /* deserialize the config value to the field. */
                    super.deserializeToField(this.toml, configField);

                    /* Add the entry to the map. */
                    super.getEntries().put(key, configField);
                }
            });

            /* Throw any errors that occurred during the loading process. */
            if (this.toml.hasErrors()) {
                throw this.toml.errors().get(0);
            }
        } catch (Exception e) {
            success = false;
            super.getAPI().getErrorHandler().getHandler().accept(e);
        }

        this.loadSuccess = success;
        return success;
    }

    @Override public boolean save() {
        /* If the config file failed to load we don't want to overwrite it with bad values. */
        if (!this.loadSuccess) {
            return false;
        }

        boolean success = true;
        List<String> lines = Lists.newArrayList();

        /* Loop through all the config map entries. */
        super.getEntries().values()
             .forEach(configPath -> {
                 /* Add all the comments first */
                 configPath.getComments().forEach(comment -> lines.add("# " + comment));

                 /* If the config path is a field we need to do some extra processing. */
                 if (configPath instanceof ConfigField) {
                     /* Whether or not we have attached the first line yet */
                     AtomicBoolean attached = new AtomicBoolean(false);

                     /* The first list of the config field. */
                     String firstLine = this.getPathString(configPath.getKey()) + " = ";
                     Optional<?> value = ((ConfigField<?>) configPath).getValue();

                     if (value.isPresent()) {
                         /* Serialize the string, and if there are multiple lines do some extra processing. */
                         this.serializeToString(value.get()).forEach(line -> {
                             if (!attached.getAndSet(true)) {
                                 lines.add(firstLine + line);
                             } else {
                                 lines.add(StringUtils.repeat(' ', firstLine.length() + 1) + line);
                             }
                         });
                     } else {
                         /* If the value isn't present set it to null. */
                         lines.add(firstLine + null);
                     }
                 } else {
                     lines.add("[" + configPath.getKey() + "]");
                 }
             });

        try {
            this.createFileIfNotExists();
            Files.write(super.getFileLocation(), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            success = false;
            super.getAPI().getErrorHandler().getHandler().accept(e);
        }

        return success;
    }

    /**
     * Converts an object to a valid toml string.
     *
     * @param object the object to serialize.
     * @return A list of valid toml strings.
     */
    private List<String> serializeToString(final @NotNull Object object) {
        List<String> output = Lists.newArrayList();

        if (ConfigList.class.isAssignableFrom(object.getClass())) {
            super.getSerializer(((ConfigList<?>) object).getGenericType())
                 .ifPresent(serializer -> ((Collection<?>) object)
                     .forEach(item -> output.addAll(serializer.serialize(item))));
        } else {
            /* If there is a serializer use it */
            super.getSerializer(object.getClass())
                 .ifPresent(serializer -> output.addAll(serializer.serialize(object)));
        }

        /* if no serializer was found we use a generic approach to creating a valid toml string. */
        if (output.isEmpty()) {
            output.add(Toml.tomlEscape(object.toString()).toString());
        }

        /* If there is more than one string in the list that means it's an array so we need to do some
         * TOML specific changes to make it work with the toml deserializer. */

        /* This basically converts a list of any type to example
        *  "Hey", "There", "Bro"
        * ["Hey",
        * "There",
        " "Bro"]/
        *
        * Any extra formatting is done in the save method.
         */
        if (output.size() > 1) {
            for (int x = 0; x != output.size(); x++) {
                if (x == 0) {
                    output.set(x, "[" + output.get(x) + ", ");
                } else if (x + 1 == output.size()) {
                    output.set(x, output.get(x) + "]");
                } else {
                    output.set(x, output.get(x) + ", ");
                }
            }
        }

        return output;
    }

    @Override protected String getPathString(final @NotNull String path) {
        /* Converts a key to the final value in the chain. */
        /* eg. 'hello.there.how.are.you' turns into 'you'. */
        String output = StringUtils.substringAfterLast(path, ".");

        /* If the output is empty that means there were no extra dots. */
        if (output.isEmpty()) {
            output = path;
        }

        return output;
    }
}
