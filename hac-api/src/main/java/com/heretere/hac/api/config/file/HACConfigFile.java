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

/**
 * The type Hac config file.
 */
public class HACConfigFile {
    /**
     * The HAC API reference.
     */
    private final HACAPI api;
    /**
     * The YAMLConfiguration that this class is backed by.
     */
    private final YamlConfiguration configuration;
    /**
     * The map that handles organizing all the path locations.
     */
    private final Map<String, ConfigPath> entries;
    /**
     * The path to the config file.
     */
    private final Path path;

    /**
     * Instantiates a new Hac config file.
     *
     * @param api  the api
     * @param path the path
     */
    public HACConfigFile(@NotNull final HACAPI api, @NotNull final Path path) {
        this.api = api;
        this.path = path;

        this.configuration = new YamlConfiguration();
        this.entries = Maps.newTreeMap();

        this.load();
    }

    /**
     * Instantiates a new Hac config file.
     *
     * @param api           the api
     * @param configHandler the config handler
     * @param file          the file
     */
    public HACConfigFile(@NotNull final HACAPI api,
                         @NotNull final HACConfigHandler configHandler,
                         @NotNull final ConfigFile file) {
        this(api, configHandler.getBasePath().resolve(file.value()));
    }

    private static String getPathString(@NotNull final String path) {
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
                        field.setValueRaw(value);
                        this.entries.put(string, field);
                    }
                }
            }
        } catch (IOException | InvalidConfigurationException e) {
            this.api.getErrorHandler().getHandler().accept(e);
        }
    }

    /**
     * Load config path.
     *
     * @param configPath the config path
     */
    public void loadConfigPath(@NotNull final ConfigPath configPath) {
        this.recursiveAddPath(configPath);

        if (configPath.getType().equals(ConfigPath.Type.VALUE)) {
            ConfigField<?> field = (ConfigField<?>) configPath;

            if (this.configuration.contains(field.getPath())) {
                Object value = this.configuration.getObject(field.getPath(), field.getClassType());

                if (value != null) {
                    field.setValueRaw(value);
                }
            }
        }
    }

    private void recursiveAddPath(@NotNull final ConfigPath path) {
        if (!path.getComments().isEmpty() || path.getType().equals(ConfigPath.Type.VALUE)) {
            this.entries.put(path.getPath(), path);
        } else {
            this.entries.putIfAbsent(path.getPath(), path);
        }

        if (StringUtils.split(path.getPath(), ".").length > 1) {
            this.recursiveAddPath(new ConfigSection(this.api, StringUtils.substringBeforeLast(path.getPath(), ".")));
        }
    }

    /**
     * Saves the yaml to the current file.
     */
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
