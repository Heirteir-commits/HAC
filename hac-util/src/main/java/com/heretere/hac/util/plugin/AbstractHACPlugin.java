package com.heretere.hac.util.plugin;

import com.heretere.hac.util.plugin.dependency.DependencyLoader;
import com.heretere.hac.util.plugin.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public abstract class AbstractHACPlugin extends JavaPlugin {
    private final Path baseDirectory;
    private final String prefix;

    private final Log log;

    private boolean dependencySuccess;

    protected AbstractHACPlugin(String baseDirectory, String prefix) {
        this.baseDirectory = this.getDataFolder().toPath().getParent().resolve(baseDirectory);
        this.prefix = "[" + prefix + "] ";

        this.log = new Log(this);
        this.log.open();
    }

    @Override
    public final void onLoad() {
        super.onLoad();

        this.dependencySuccess = new DependencyLoader(this).loadDependencies();

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
        } else {
            this.log.reportFatalError(() ->
                            String.format("Could not download required dependencies. " +
                                            "Please look at the latest.log in the '%s' folder to determine the issue.",
                                    this.baseDirectory.getParent().relativize(this.baseDirectory.resolve("logs").resolve(this.getName()))),
                    false
            );
            Bukkit.getPluginManager().disablePlugin(this);
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
