package com.heretere.hac.api.config.processor.yaml;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.processor.Processor;
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

public class YAMLProcessor extends Processor<YamlConfiguration> {
    private @Nullable YamlConfiguration current;

    public YAMLProcessor(
        @NotNull final HACAPI api,
        @NotNull final Path location
    ) {
        super(api, location);
    }

    private void attachSectionParent(@NotNull final ConfigPath path) {
        String parentPath = StringUtils.substringBeforeLast(path.getKey(), ".");

        if (!path.getKey().equals(parentPath)) {
            ConfigSection section = new ConfigSection(parentPath);

            if (!super.getEntries().containsKey(parentPath)) {
                super.getEntries().put(parentPath, section);
            }

            this.attachSectionParent(section);
        }
    }

    @Override
    public boolean processConfigPath(@NotNull final ConfigPath configPath) {
        boolean success = true;

        if (configPath instanceof ConfigField) {
            this.attachSectionParent(configPath);
            super.getEntries().put(configPath.getKey(), configPath);

            if (this.current != null && this.current.contains(configPath.getKey())) {
                success = this.deserializeToField((ConfigField<?>) configPath);
            }
        } else {
            this.getEntries().put(configPath.getKey(), configPath);
        }

        return success;
    }

    private boolean deserializeToField(@NotNull final ConfigField<?> configField) {
        AtomicBoolean success = new AtomicBoolean(true);

        if (this.current != null) {
            super.getDeserializer(configField.getGenericType()).ifPresent(deserializer -> {
                try {
                    configField.setValueRaw(deserializer.deserialize(
                        this.current,
                        configField.getGenericType(),
                        configField.getKey()
                    ));
                } catch (Exception e) {
                    success.set(false);
                    super.getAPI().getErrorHandler().getHandler().accept(e);
                }
            });
        }

        return success.get();
    }

    @Override
    public boolean load() {
        boolean success = true;

        try {
            createFileIfNotExists();

            current = YamlConfiguration.loadConfiguration(super.getFileLocation().toFile());

            current.getKeys(false).forEach(key -> {
                if (this.current.isConfigurationSection(key)) {
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

                    this.deserializeToField(configField);
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
        if (!this.load()) {
            return false;
        }

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
