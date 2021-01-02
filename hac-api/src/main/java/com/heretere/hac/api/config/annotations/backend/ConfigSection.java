package com.heretere.hac.api.config.annotations.backend;

import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;

/**
 * The type Config section.
 */
public class ConfigSection extends ConfigPath {
    /**
     * Instantiates a new Config section.
     *
     * @param api      the api
     * @param path     the path
     * @param comments the comments
     */
    public ConfigSection(
        final @NotNull HACAPI api,
        final @NotNull String path,
        final @NotNull String... comments
    ) {
        super(api, Type.SECTION, path, comments);
    }
}
