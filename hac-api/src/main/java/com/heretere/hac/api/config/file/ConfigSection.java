package com.heretere.hac.api.config.file;

import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;

public class ConfigSection extends ConfigPath {
    public ConfigSection(@NotNull HACAPI api, @NotNull String path, @NotNull String... comments) {
        super(api, Type.SECTION, path, comments);
    }
}
