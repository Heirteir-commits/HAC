package com.heirteir.hac.util.dependency.plugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class DependencyPluginTest extends DependencyPlugin {
    public DependencyPluginTest() {
        super("HAC");
    }

    protected DependencyPluginTest(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void enable() {
        //not used
    }

    @Override
    protected void disable() {
        //not used
    }

    @Override
    protected void load() {
        //not used
    }
}
