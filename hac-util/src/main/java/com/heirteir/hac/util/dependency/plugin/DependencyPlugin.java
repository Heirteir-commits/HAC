package com.heirteir.hac.util.dependency.plugin;

import com.heirteir.hac.util.dependency.Dependencies;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.Log;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DependencyPlugin extends JavaPlugin {
    @Getter
    private final String loggerPrefix;

    private boolean dependencySuccess;

    protected DependencyPlugin(String loggerPrefix) {
        this.loggerPrefix = "[HAC] [" + loggerPrefix + "] ";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Log.INSTANCE.open();
        this.dependencySuccess = Dependencies.loadDependenciesFromPlugin(this);
        this.load();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Log.INSTANCE.close();
        this.disable();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (!this.dependencySuccess) {
            Log.INSTANCE.reportFatalError(
                    String.format("'%s' Wasn't able to download required dependencies. " +
                                    "Please look at the latest.log in the '%s' folder to determine the issue.",
                            super.getName(),
                            FilePaths.INSTANCE.getPluginFolder().getParent().relativize(FilePaths.INSTANCE.getPluginFolder().resolve("log")))
            );
            return;
        }

        this.enable();

    }

    protected abstract void enable();

    protected abstract void disable();

    protected abstract void load();
}
