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

import com.google.common.collect.Sets;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.backend.ConfigField;
import com.heretere.hac.api.config.backend.ConfigPath;
import com.heretere.hac.api.config.backend.ConfigSection;
import com.heretere.hac.api.config.processor.Processor;
import com.heretere.hac.api.config.processor.TypeDeserializer;
import com.heretere.hac.api.config.processor.TypeSerializer;
import com.heretere.hac.api.config.processor.toml.serialization.TomlBooleanHybridHandler;
import com.heretere.hac.api.config.processor.toml.serialization.TomlStringHybridHandler;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class TOMLProcessor extends Processor<TomlParseResult> {
    private @Nullable TomlParseResult current;

    private boolean loadSuccess = false;

    public TOMLProcessor(
        final @NotNull HACAPI api,
        final @NotNull Path fileLocation
    ) {
        super(api, fileLocation);

        TomlStringHybridHandler tomlStringHybridHandler = new TomlStringHybridHandler();
        TomlBooleanHybridHandler booleanHandler = new TomlBooleanHybridHandler();

        super.attachTypeSerializer(tomlStringHybridHandler);
        super.attachTypeDeserializer(tomlStringHybridHandler);
        super.attachTypeSerializer(booleanHandler);
        super.attachTypeDeserializer(booleanHandler);
    }

    private void addParent(final @NotNull ConfigPath path) {
        String parentPath = StringUtils.substringBeforeLast(path.getPath(), ".");

        if (!parentPath.isEmpty()
            && (!super.getEntries().containsKey(parentPath)
            || !super.getEntries().get(parentPath).getComments().isEmpty())) {
            super.getEntries().put(parentPath, new ConfigSection(super.getAPI(), parentPath));
        }
    }

    private boolean deserializeToField(final @NotNull ConfigField<?> field) {
        boolean success = true;
        if (this.current != null) {
            TypeDeserializer<TomlParseResult, ?> deserializer = super.getDeserializers().get(field.getClassType());

            if (deserializer != null) {
                try {
                    field.setValueRaw(deserializer.deserialize(this.current, field.getPath()));
                } catch (Exception e) {
                    success = false;
                    super.getAPI().getErrorHandler().getHandler().accept(e);
                }
            }
        }

        return success;
    }

    @Override public boolean processConfigPath(final @NotNull ConfigPath configPath) {
        boolean success = true;

        if (configPath.getType().equals(ConfigPath.Type.SECTION)) {
            this.getEntries().put(configPath.getPath(), configPath);
        } else {
            this.addParent(configPath);
            super.getEntries().put(configPath.getPath(), configPath);

            if (this.current != null && this.current.contains(configPath.getPath())) {
                ConfigField<?> field = (ConfigField<?>) configPath;

                success = this.deserializeToField(field);
            }
        }

        return success;
    }

    @Override public boolean load() {
        boolean success = true;
        try {
            if (!Files.exists(super.getFileLocation())) {
                Files.createDirectories(super.getFileLocation().getParent());
                Files.createFile(super.getFileLocation());
            }

            this.current = Toml.parse(super.getFileLocation());

            if (this.current.hasErrors()) {
                success = false;
                this.current.errors().forEach(error -> super.getAPI().getErrorHandler().getHandler().accept(error));
            } else {
                for (String key : this.current.dottedKeySet(false)) {
                    if (this.current.isTable(key)
                        && !Objects.requireNonNull(this.current.getTable(key)).isEmpty()) {
                        super.getEntries().put(key, new ConfigSection(super.getAPI(), key));
                    } else {
                        Object value = this.current.get(key);

                        if (this.current.hasErrors()) {
                            success = false;
                            this.current.errors()
                                        .forEach(error -> super.getAPI().getErrorHandler().getHandler().accept(error));
                            continue;
                        }

                        if (value != null) {
                            ConfigField<?> field;
                            if (super.getEntries().containsKey(key)) {
                                field = (ConfigField<?>) super.getEntries().get(key);

                                this.deserializeToField(field);

                                if (!field.getValue().isPresent()) {
                                    field.setValueRaw(value);
                                }
                            } else {
                                field = new ConfigField<>(super.getAPI(), Object.class, null, key);
                                field.setValueRaw(value);
                                super.getEntries().put(key, field);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            success = false;
            super.getAPI().getErrorHandler().getHandler().accept(e);
        }

        this.loadSuccess = success;
        return success;
    }

    @Override public boolean save() {
        if (!this.loadSuccess || !this.load()) {
            return false;
        }

        boolean success = true;
        Set<String> lines = Sets.newLinkedHashSet();

        super.getEntries()
             .values()
             .forEach(configPath -> {
                 configPath.getComments().forEach(comment -> lines.add("# " + comment));

                 if (configPath.getType().equals(ConfigPath.Type.SECTION)) {
                     lines.add("[" + configPath.getPath() + "]");
                 } else {
                     ConfigField<?> field = (ConfigField<?>) configPath;

                     String firstLine = this.getPathString(configPath.getPath()) + " = ";
                     Optional<?> value = field.getValue();

                     if (value.isPresent()) {
                         TypeSerializer<?> serializer = super.getSerializers().get(field.getClassType());

                         if (serializer != null) {
                             List<String> serialized = serializer.serialize(value.get());

                             for (int x = 0; x != serialized.size(); x++) {
                                 if (x == 0) {
                                     lines.add(firstLine + serialized.get(x));
                                 } else {
                                     lines.add(StringUtils.repeat(' ', 4) + serialized.get(x));
                                 }
                             }
                         }
                     } else {
                         lines.add(firstLine + "null");
                     }
                 }
             });

        try {
            if (!Files.exists(super.getFileLocation())) {
                Files.createDirectories(super.getFileLocation().getParent());
                Files.createFile(super.getFileLocation());
            }

            Files.write(super.getFileLocation(), lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (IOException e) {
            success = false;
            super.getAPI().getErrorHandler().getHandler().accept(e);
        }

        return success;
    }

    @Override protected @NotNull String getPathString(final @NotNull String path) {
        String output = StringUtils.substringAfterLast(path, ".");

        if (output.isEmpty()) {
            output = path;
        }

        return output;
    }
}
