package com.heretere.hac.util.plugin;

import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.dependency.AbstractDependency;
import com.heretere.hac.util.plugin.logging.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Set;

public abstract class AbstractHACPlugin extends JavaPlugin {
    private final Path baseDirectory;
    private final String prefix;

    private final Log log;

    protected AbstractHACPlugin(Path baseDirectory, String prefix) {
        this.baseDirectory = baseDirectory;
        this.prefix = prefix;

        this.log = new Log(this);
    }

    @Override
    public final void onLoad() {
        super.onLoad();
        this.load();
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        this.disable();
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        this.enable();
    }

    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    private boolean loadDependencies() {
        Set<AbstractDependency> dependencies = Sets.newLinkedHashSet();


    }

    public Path getBaseDirectory() {
        return baseDirectory;
    }

    public String getPrefix() {
        return prefix;
    }

    public Log getLog() {
        return log;
    }
}
