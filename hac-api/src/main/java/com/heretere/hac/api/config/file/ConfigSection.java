package com.heretere.hac.api.config.file;

public class ConfigSection extends ConfigPath {
    public ConfigSection(String path, String... comments) {
        super(Type.SECTION, path, comments);
    }
}
