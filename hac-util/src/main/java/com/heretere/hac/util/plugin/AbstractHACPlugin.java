package com.heretere.hac.util.plugin;

import com.heretere.hac.util.plugin.dependency.DependencyLoader;
import com.heretere.hac.util.plugin.logging.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public abstract class AbstractHACPlugin extends JavaPlugin {
    private final Path baseDirectory;
    private final String prefix;

    private final Log log;

    private final DependencyLoader dependencyLoader;
    private boolean dependencySuccess;

    protected AbstractHACPlugin(String baseDirectory, String prefix) {
        this.baseDirectory = this.getDataFolder().toPath().getParent().resolve(baseDirectory);
        this.prefix = prefix;

        this.log = new Log(this);
        this.log.open();
        this.dependencyLoader = new DependencyLoader(this);
    }

    @Override
    public final void onLoad() {
        super.onLoad();

        this.dependencySuccess = this.dependencyLoader.loadDependencies();

        if (this.dependencySuccess) {
            this.load();
        }
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        if (this.dependencySuccess) {
            this.disable();
        }

        this.log.close();
    }

    @Override
    public final void onEnable() {
        super.onEnable();

        if (this.dependencySuccess) {
            this.enable();
        }
    }

    public abstract void load();

    public abstract void enable();

    public abstract void disable();


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
