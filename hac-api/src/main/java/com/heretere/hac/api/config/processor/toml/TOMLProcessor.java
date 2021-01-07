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
import com.heretere.hac.api.config.processor.Processor;
import com.heretere.hac.api.config.processor.toml.typehandler.TomlBooleanSerializer;
import com.heretere.hac.api.config.processor.toml.typehandler.TomlEnumSerializer;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TOMLProcessor extends Processor<TomlParseResult> {
    private @Nullable TomlParseResult toml;

    public TOMLProcessor(
        final @NotNull HACAPI api,
        final @NotNull Path fileLocation
    ) {
        super(api, fileLocation);
        this.createDefaultHandlers();
    }

    private void createDefaultHandlers() {
        super.attachTypeHandler(new TomlStringSerializer());
        super.attachTypeHandler(new TomlBooleanSerializer());
        super.attachTypeHandler(new TomlEnumSerializer());
    }

    private void attachSectionParent(final @NotNull ConfigPath path) {
        String parentPath = StringUtils.substringBeforeLast(path.getKey(), ".");

        if (!parentPath.isEmpty() && !super.getEntries().containsKey(parentPath)) {
            super.getEntries().put(parentPath, new ConfigSection(parentPath));
        }
    }

    @Override public boolean processConfigPath(final @NotNull ConfigPath configPath) {
        boolean success = true;

        if (configPath instanceof ConfigField) {
            this.attachSectionParent(configPath);
            super.getEntries().put(configPath.getKey(), configPath);

            if (this.toml != null && this.toml.contains(configPath.getKey())) {
                success = super.deserializeToField(this.toml, (ConfigField<?>) configPath);
            }
        } else {
            this.getEntries().put(configPath.getKey(), configPath);
        }

        return success;
    }

    private void createFileIfNotExists() throws IOException {
        if (!Files.exists(super.getFileLocation())) {
            Files.createDirectories(super.getFileLocation().getParent());
            Files.createFile(super.getFileLocation());
        }
    }

    @Override public boolean load() {
        boolean success = true;

        try {
            this.createFileIfNotExists();
            this.toml = Toml.parse(super.getFileLocation());

            this.toml.dottedKeySet(false).forEach(key -> {
                if (this.toml.isTable(key)) {
                    super.getEntries().put(key, new ConfigSection(key));
                } else {
                    ConfigField<?> configField = super.getEntries().containsKey(key)
                        ? (ConfigField<?>) super.getEntries().get(key)
                        : new ReflectiveConfigField<>(
                            super.getAPI(),
                            key,
                            Lists.newArrayList(),
                            Object.class,
                            null
                        );

                    super.deserializeToField(this.toml, configField);
                    super.getEntries().put(key, configField);
                }
            });

            if (this.toml.hasErrors()) {
                throw this.toml.errors().get(0);
            }
        } catch (Exception e) {
            success = false;
            super.getAPI().getErrorHandler().getHandler().accept(e);
        }

        return success;
    }

    @Override public boolean save() {
        if (!this.load()) {
            return false;
        }

        boolean success = true;
        List<String> lines = Lists.newArrayList();

        super.getEntries().values()
             .forEach(configPath -> {
                 configPath.getComments().forEach(comment -> lines.add("# " + comment));

                 if (configPath instanceof ConfigField) {
                     AtomicBoolean attached = new AtomicBoolean(false);
                     String firstLine = this.getPathString(configPath.getKey()) + " = ";
                     Optional<?> value = ((ConfigField<?>) configPath).getValue();

                     if (value.isPresent()) {
                         this.serializeToString(value.get()).forEach(line -> {
                             if (!attached.getAndSet(true)) {
                                 lines.add(firstLine + line);
                             } else {
                                 lines.add(StringUtils.repeat(' ', firstLine.length()) + line);
                             }
                         });
                     } else {
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

    private List<String> serializeToString(final @NotNull Object object) {
        List<String> output = Lists.newArrayList();

        super.getSerializer(object.getClass())
             .ifPresent(serializer -> output.addAll(serializer.serialize(object)));

        if (output.isEmpty()) {
            output.add(Toml.tomlEscape(object.toString()).toString());
        }

        return output;
    }

    @Override protected String getPathString(final @NotNull String path) {
        String output = StringUtils.substringAfterLast(path, ".");

        if (output.isEmpty()) {
            output = path;
        }

        return output;
    }
}
