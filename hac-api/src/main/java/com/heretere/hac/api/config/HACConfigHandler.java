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

package com.heretere.hac.api.config;

import com.google.common.collect.Maps;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.processor.Processor;
import com.heretere.hac.api.config.processor.toml.TomlProcessor;
import com.heretere.hac.api.config.structure.annotation.ConfigFile;
import com.heretere.hac.api.config.structure.backend.ConfigClassParser;
import com.heretere.hac.api.config.structure.backend.ConfigPath;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * The type Hac config handler.
 */
public class HACConfigHandler {
    /**
     * The HAC API reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The config class parser converts annotations into config paths.
     */
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

    private Processor<?> getProcessor(final @NotNull String path) {
        return this.files.computeIfAbsent(path, key -> new TomlProcessor(this.api, this.basePath.resolve(key)));
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
            Processor<?> processor = this.getProcessor(file.value());

            configPaths.values().forEach(processor::processConfigPath);
        }

        this.load();
    }

    /**
     * Loads a collection of configs paths.
     *
     * @param relativeFilePath The relative path to the config file.
     * @param configPaths      The collection of config paths.
     */
    public void loadConfigPaths(
        final @NotNull String relativeFilePath,
        final @NotNull Collection<ConfigPath> configPaths
    ) {
        Processor<?> processor = this.getProcessor(relativeFilePath);

        configPaths.forEach(processor::processConfigPath);

        this.load();
    }

    /**
     * Loads all the configs.
     *
     * @return true if all loaded successfully.
     */
    public boolean load() {
        return this.files.values().stream().allMatch(Processor::load);
    }

    /**
     * Saves all the loaded config files.
     *
     * @return true if all configs were saved successfully.
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
