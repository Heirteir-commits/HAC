package com.heretere.hac.api.config.file;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.HACConfigHandler;
import com.heretere.hac.api.config.annotations.ConfigFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * The type Hac config file.
 */
public final class HACConfigFile {
    /**
     * The HAC API reference.
     */
    private final HACAPI api;
    /**
     * The map that handles organizing all the path locations.
     */
    private final Map<String, ConfigPath> entries;
    /**
     * The path to the config file.
     */
    private final Path path;

    /**
     * The parse result of the TOML config.
     */
    private TomlParseResult current;

    /**
     * Instantiates a new Hac config file.
     *
     * @param api  the api
     * @param path the path
     */
    public HACConfigFile(
        @NotNull final HACAPI api,
        @NotNull final Path path
    ) {
        this.api = api;
        this.path = path;

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
    public HACConfigFile(
        @NotNull final HACAPI api,
        @NotNull final HACConfigHandler configHandler,
        @NotNull final ConfigFile file
    ) {
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
            if (!Files.exists(this.path)) {
                Files.createDirectories(this.path.getParent());
                Files.createFile(this.path);
            }

            this.current = Toml.parse(this.path);

            for (String string : this.current.dottedKeySet(true)) {
                if (this.current.isTable(string)) {
                    this.entries.put(string, new ConfigSection(this.api, string));
                } else {
                    Object value = this.current.get(string);

                    if (value != null) {
                        ConfigField<?> field = new ConfigField<>(this.api, value.getClass(), null, string);
                        field.setValueRaw(value);
                        this.entries.put(string, field);
                    }
                }
            }
        } catch (IOException e) {
            this.api.getErrorHandler().getHandler().accept(e);
        }
    }

    /**
     * Load config path.
     *
     * @param configPath the config path
     */
    public void loadConfigPath(@NotNull final ConfigPath configPath) {
        if (configPath.getType().equals(ConfigPath.Type.VALUE)) {
            this.addParent(configPath);
            this.entries.put(configPath.getPath(), configPath);

            ConfigField<?> field = (ConfigField<?>) configPath;

            if (this.current.contains(field.getPath())) {
                Object value = this.current.get(field.getPath());

                if (value != null) {
                    field.setValueRaw(value);
                }
            }
        } else if (configPath.getType().equals(ConfigPath.Type.SECTION)) {
            this.entries.put(configPath.getPath(), configPath);
        }
    }

    private void addParent(@NotNull final ConfigPath path) {
        String parentPath = StringUtils.substringBeforeLast(path.getPath(), ".");

        if (!parentPath.isEmpty() && (!this.entries.containsKey(parentPath) || !this.entries.get(parentPath)
                                                                                            .getComments()
                                                                                            .isEmpty())) {
            this.entries.put(parentPath, new ConfigSection(this.api, parentPath));
        }
    }

    /**
     * Saves the file.
     */
    public void save() {
        Set<String> lines = Sets.newLinkedHashSet();

        this.entries.values().forEach(configPath -> {
            for (String comment : configPath.getComments()) {
                lines.add("# " + comment);
            }

            if (configPath.getType().equals(ConfigPath.Type.SECTION)) {
                lines.add("[" + configPath.getPath() + "]");
            }

            if (configPath.getType().equals(ConfigPath.Type.VALUE)) {
                ConfigField<?> field = (ConfigField<?>) configPath;

                lines.add(HACConfigFile.getPathString(configPath.getPath()) + " = " + field.getValue());
            }
        });

        try {
            Files.write(this.path, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
        } catch (IOException e) {
            this.api.getErrorHandler().getHandler().accept(e);
        }
    }
}
