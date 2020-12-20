package com.heretere.hac.api.config.file;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.HACConfigHandler;
import com.heretere.hac.api.config.annotations.ConfigFile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class HACConfigFile {
    private final HACAPI api;
    private final YamlConfiguration configuration;
    private final Map<String, ConfigPath> entries;
    private final Path path;

    public HACConfigFile(@NotNull HACAPI api, @NotNull Path path) {
        this.api = api;
        this.path = path;

        this.configuration = new YamlConfiguration();
        this.entries = Maps.newTreeMap();

        this.load();
    }

    public HACConfigFile(@NotNull HACAPI api, @NotNull HACConfigHandler configHandler, @NotNull ConfigFile file) {
        this(api, configHandler.getBasePath().resolve(file.value()));
    }

    private static String getPathString(String path) {
        String output = StringUtils.substringAfterLast(path, ".");

        if (output.isEmpty()) {
            output = path;
        }

        return output;
    }

    private void load() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            this.configuration.load(this.path.toFile());

            for (String string : this.configuration.getKeys(true)) {
                if (this.configuration.isConfigurationSection(string)) {
                    this.entries.put(string, new ConfigSection(this.api, string));
                } else {
                    Object value = configuration.get(string);

                    if (value != null) {
                        ConfigField<?> field = new ConfigField<>(this.api, value.getClass(), null, string);
                        field.setValueRaw(configuration.get(string));
                        this.entries.put(string, field);
                    }
                }
            }
        } catch (IOException | InvalidConfigurationException e) {
            this.api.getErrorHandler().getHandler().accept(e);
        }
    }

    public void loadConfigPath(@NotNull ConfigPath configPath) {
        this.recursiveAddPath(configPath);

        if (configPath.getType().equals(ConfigPath.Type.VALUE)) {
            ConfigField<?> field = (ConfigField<?>) configPath;

            if (this.configuration.contains(field.getPath())) {
                field.setValueRaw(this.configuration.getObject(field.getPath(), field.getClassType()));
            }
        }
    }

    private void recursiveAddPath(ConfigPath path) {
        if (!path.getComments().isEmpty() || path.getType().equals(ConfigPath.Type.VALUE)) {
            this.entries.put(path.getPath(), path);
        } else {
            this.entries.putIfAbsent(path.getPath(), path);
        }

        if (StringUtils.split(path.getPath(), ".").length > 1) {
            this.recursiveAddPath(new ConfigSection(this.api, StringUtils.substringBeforeLast(path.getPath(), ".")));
        }
    }

    public void save() {
        Yaml yaml = new Yaml();
        Set<String> lines = Sets.newLinkedHashSet();
        this.entries.values().forEach(configPath -> {
            String indent = StringUtils.repeat(" ", configPath.getIndentLevel());

            for (String comment : configPath.getComments()) {
                lines.add(indent + "# " + comment);
            }

            if (configPath.getType().equals(ConfigPath.Type.SECTION)) {
                lines.add(indent + HACConfigFile.getPathString(configPath.getPath()) + ":");
            }

            if (configPath.getType().equals(ConfigPath.Type.VALUE)) {
                ConfigField<?> field = (ConfigField<?>) configPath;

                String[] split = StringUtils.split(yaml.dump(field.getValue()), System.lineSeparator());

                for (int x = 0; x != split.length; x++) {
                    String output = split[x].trim();
                    output = output.toLowerCase(Locale.ROOT).equals("null") ? "" : output;
                    if (x == 0) {
                        lines.add(indent + HACConfigFile.getPathString(configPath.getPath()) + ": " + output);
                    } else {
                        lines.add(indent + output);
                    }
                }
            }
        });

        try {
            Files.write(this.path, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (IOException e) {
            this.api.getErrorHandler().getHandler().accept(e);
        }
    }
}
