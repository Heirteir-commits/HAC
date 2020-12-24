package com.heretere.hac.util.plugin;

import com.heretere.hac.util.plugin.dependency.DependencyLoader;
import com.heretere.hac.util.plugin.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public abstract class AbstractHACPlugin extends JavaPlugin {
    /**
     * The base directory all files should be passed to. This is similar to {@link JavaPlugin#getDataFolder()}
     * except it points to a directory that could be used by multiple plugins.
     */
    private final @NotNull Path baseDirectory;

    /**
     * The prefix that the logger uses for console.
     */
    private final @NotNull String prefix;

    /**
     * This is a logger delegator that just adds some custom functionality to the Bukkit logger.
     */
    private final @NotNull Log log;

    /**
     * Whether or not the dependencies were successfully loaded.
     */
    private boolean dependencySuccess;

    protected AbstractHACPlugin(
        final @NotNull String baseDirectory,
        final @NotNull String prefix
    ) {
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
            this.log.reportFatalError(() -> String.format(
                "Could not download required dependencies. " + "Please look at the latest.log in '%s' to " +
                    "determine the issue.",
                this.baseDirectory.getParent()
                                  .relativize(this.baseDirectory.resolve("logs").resolve(this.getName()))
            ), false);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Similar to {@link JavaPlugin#onLoad()} except it's ran after all dependencies are loaded.
     */
    public abstract void load();

    /**
     * Similar to {@link JavaPlugin#onEnable()} except it's ran after all dependencies are loaded.
     */
    public abstract void enable();

    /**
     * Similar to {@link JavaPlugin#onDisable()} except it's ran after all dependencies are loaded.
     */
    public abstract void disable();

    /**
     * The base directory for files in this plugin.
     *
     * @return The base directory
     */
    public @NotNull Path getBaseDirectory() {
        return this.baseDirectory;
    }

    /**
     * The logger prefix of this plugin.
     *
     * @return The logger prefix of this plugin
     */
    public @NotNull String getPrefix() {
        return this.prefix;
    }

    /**
     * The logger delegator for this dependency plugin.
     *
     * @return The logger delegator.
     */
    public @NotNull Log getLog() {
        return this.log;
    }
}
