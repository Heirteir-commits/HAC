package com.heretere.hac.util.dependency.plugin;

import com.heretere.hac.util.dependency.DependencyUtils;
import com.heretere.hac.util.dependency.plugin.logging.Log;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.nio.file.Path;

public abstract class DependencyPlugin extends JavaPlugin {
    @Getter
    private final String loggerPrefix;

    private boolean dependenciesLoaded = false;

    @Getter
    private final Path pluginFolder;

    @Getter
    private final Log log;

    protected DependencyPlugin(String loggerPrefix) {
        this.pluginFolder = this.getDataFolder().toPath().getParent().resolve("HAC");
        this.loggerPrefix = "[HAC] [" + loggerPrefix + "] ";
        this.log = new Log(this);
    }

    protected DependencyPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.pluginFolder = this.getDataFolder().toPath().getParent().resolve("HAC");
        this.loggerPrefix = "[HAC] [TEST]";
        this.log = new Log(this);
    }

    @Override
    public final void onLoad() {
        super.onLoad();

        this.log.open();
        this.dependenciesLoaded = DependencyUtils.loadDependenciesFromPlugin(this);

        this.load();
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        if (this.dependenciesLoaded) {
            this.disable();
        }

        this.log.close();
    }

    @Override
    public final void onEnable() {
        super.onEnable();

        if (this.dependenciesLoaded) {
            this.enable();
        } else {
            this.log.reportFatalError(
                    String.format("Could not download required dependencies. " +
                                    "Please look at the latest.log in the '%s' folder to determine the issue.",
                            this.pluginFolder.getParent().relativize(this.pluginFolder.resolve("log").resolve(super.getName())))
            );
        }
    }

    protected abstract void enable();

    protected abstract void disable();

    protected abstract void load();
}
