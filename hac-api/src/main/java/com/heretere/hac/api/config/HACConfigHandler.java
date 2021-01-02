package com.heretere.hac.api.config;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.annotations.ConfigClassParser;
import com.heretere.hac.api.config.annotations.ConfigFile;
import com.heretere.hac.api.config.annotations.backend.ConfigPath;
import com.heretere.hac.api.config.processor.Processor;
import com.heretere.hac.api.config.processor.toml.TOMLProcessor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

/**
 * The type Hac config handler.
 */
public class HACConfigHandler {
    /**
     * The HAC API reference.
     */
    private final @NotNull HACAPI api;
    private final @NotNull ConfigClassParser parser;
    /**
     * The base path of the config files.
     */
    private final @NotNull Path basePath;

    /**
     * Only currently loaded files.
     */
    private final @NotNull Map<String, Processor<?>> files;

    /**
     * Instantiates a new Hac config handler.
     *
     * @param api    the api
     * @param parent the providing plugin
     */
    public HACConfigHandler(
        final @NotNull HACAPI api,
        final @NotNull Plugin parent
    ) {
        this.api = api;
        this.parser = new ConfigClassParser(this.api);
        this.basePath = parent
            .getDataFolder()
            .toPath()
            .getParent()
            .resolve("HAC");

        this.files = Maps.newHashMap();
    }

    /**
     * Load config class.
     *
     * @param instance the instance
     */
    public void loadConfigClass(final @NotNull Object instance) {
        Map<String, ConfigPath> configPaths = this.parser.getConfigPaths(instance);

        if (!configPaths.isEmpty()) {
            ConfigFile file = instance.getClass().getAnnotation(ConfigFile.class);
            Processor<?> processor =
                this.files.computeIfAbsent(
                    file.value(),
                    key -> new TOMLProcessor(this.api, this.basePath.resolve(file.value()))
                );

            configPaths.values().forEach(processor::processConfigPath);
        }
    }

    public boolean load() {
        return this.files.values().stream().allMatch(Processor::load);
    }

    /**
     * Saves all the loaded config files.
     */
    public boolean unload() {
        return this.files.values().stream().allMatch(Processor::save);
    }

    /**
     * Gets base path.
     *
     * @return the base path
     */
    public @NotNull Path getBasePath() {
        return this.basePath;
    }
}
