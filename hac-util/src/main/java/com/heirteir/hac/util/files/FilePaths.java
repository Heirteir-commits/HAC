package com.heirteir.hac.util.files;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public enum FilePaths {
    INSTANCE;

    private static final String PLUGIN_FOLDER_NAME = "HAC";

    /* plugin data folder */
    private final Path pluginFolder;

    FilePaths() {
        Plugin plugin = JavaPlugin.getProvidingPlugin(FilePaths.class);

        this.pluginFolder = plugin.getDataFolder().toPath().getParent().resolve(FilePaths.PLUGIN_FOLDER_NAME);
    }
}
