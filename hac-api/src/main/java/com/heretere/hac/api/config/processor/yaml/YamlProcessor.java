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

package com.heretere.hac.api.config.processor.yaml;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.processor.Processor;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlBooleanSerializer;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlCharacterSerializer;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlDoubleSerializer;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlEnumSerializer;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlFloatSerializer;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlIntegerSerializer;
import com.heretere.hac.api.config.processor.yaml.typehandler.YamlStringSerializer;
import com.heretere.hac.api.config.structure.backend.ConfigField;
import com.heretere.hac.api.config.structure.backend.ConfigPath;
import com.heretere.hac.api.config.structure.backend.ConfigSection;
import com.heretere.hac.api.config.structure.backend.ReflectiveConfigField;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public final class YamlProcessor extends Processor<YamlConfiguration> {
    /**
     * The processing backend for the YamlProcessor.
     */
    private @Nullable YamlConfiguration yaml;

    /**
     * Creates a new YamlProcessor
     *
     * @param api      The HACAPI reference.
     * @param location The location of the config file.
     */
    public YamlProcessor(
        final @NotNull HACAPI api,
        final @NotNull Path location
    ) {
        super(api, location);
        this.createDefaultHandlers();
    }

    /**
     * Attaches pre made serializers to this processor.
     */
    private void createDefaultHandlers() {
        super.attachTypeHandler(new YamlBooleanSerializer());
        super.attachTypeHandler(new YamlCharacterSerializer());
        super.attachTypeHandler(new YamlDoubleSerializer());
        super.attachTypeHandler(new YamlEnumSerializer());
        super.attachTypeHandler(new YamlFloatSerializer());
        super.attachTypeHandler(new YamlIntegerSerializer());
        super.attachTypeHandler(new YamlStringSerializer());
    }

    private void attachSectionParent(final @NotNull ConfigPath path) {
        String parentPath = StringUtils.substringBeforeLast(path.getKey(), ".");

        if (!path.getKey().equals(parentPath)) {
            ConfigSection section = new ConfigSection(parentPath);

            if (!super.getEntries().containsKey(parentPath)) {
                super.getEntries().put(parentPath, section);
            }

            this.attachSectionParent(section);
        }
    }

    @Override public boolean processConfigPath(final @NotNull ConfigPath configPath) {
        boolean success = true;

        if (configPath instanceof ConfigField) {
            this.attachSectionParent(configPath);
            super.getEntries().put(configPath.getKey(), configPath);

            if (this.yaml != null && this.yaml.contains(configPath.getKey())) {
                success = super.deserializeToField(this.yaml, (ConfigField<?>) configPath);
            }
        } else {
            this.getEntries().put(configPath.getKey(), configPath);
        }

        return success;
    }

    @Override
    public boolean load() {
        boolean success = true;

        try {
            this.createFileIfNotExists();

            this.yaml = YamlConfiguration.loadConfiguration(super.getFileLocation().toFile());

            this.yaml.getKeys(false).forEach(key -> {
                if (this.yaml.isConfigurationSection(key)) {
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

                    this.deserializeToField(this.yaml, configField);
                    super.getEntries().put(key, configField);
                }
            });
        } catch (Exception e) {
            success = false;
            super.getAPI().getErrorHandler().getHandler().accept(e);
        }

        return success;
    }

    private void createFileIfNotExists() throws IOException {
        if (!Files.exists(super.getFileLocation())) {
            Files.createDirectories(super.getFileLocation().getParent());
            Files.createFile(super.getFileLocation());
        }
    }

    @Override
    public boolean save() {
        boolean success = true;
        List<String> lines = Lists.newArrayList();

        super.getEntries().values()
             .forEach(configPath -> {
                 String indentation = StringUtils.repeat("  ", StringUtils.split(configPath.getKey(), ".").length - 1);

                 configPath.getComments().forEach(comment -> lines.add(indentation + "# " + comment));

                 if (configPath instanceof ConfigField) {
                     AtomicBoolean attached = new AtomicBoolean(false);
                     String firstLine = indentation + this.getPathString(configPath.getKey()) + ": ";
                     Optional<?> value = ((ConfigField<?>) configPath).getValue();

                     if (value.isPresent()) {
                         this.serializeToString(value.get()).forEach(line -> {
                             if (!attached.getAndSet(true)) {
                                 lines.add(firstLine + line);
                             } else {
                                 lines.add(indentation + "- " + line);
                             }
                         });
                     } else {
                         lines.add(firstLine + null);
                     }
                 } else {
                     lines.add(indentation + this.getPathString(configPath.getKey()) + ": ");
                 }
             });

        try {
            this.createFileIfNotExists();
            Files.write(super.getFileLocation(), lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
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
            output.add(object.toString());
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
