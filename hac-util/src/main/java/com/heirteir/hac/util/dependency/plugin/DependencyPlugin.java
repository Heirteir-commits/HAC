package com.heirteir.hac.util.dependency.plugin;

import com.heirteir.hac.util.dependency.DependencyUtils;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.Log;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DependencyPlugin extends JavaPlugin {
    @Getter
    private final String loggerPrefix;

    private boolean dependenciesLoaded = false;

    protected DependencyPlugin(String loggerPrefix) {
        this.loggerPrefix = "[HAC] [" + loggerPrefix + "] ";
    }

    @Override
    public final void onLoad() {
        super.onLoad();

        Log.INSTANCE.open();
        this.dependenciesLoaded = DependencyUtils.loadDependenciesFromPlugin(this);

        this.load();
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        if (this.dependenciesLoaded) {
            this.disable();
        }

        Log.INSTANCE.close();
    }

    @Override
    public final void onEnable() {
        super.onEnable();

        if (this.dependenciesLoaded) {
            this.enable();
        } else {
            Log.INSTANCE.reportFatalError(
                    String.format("Could not download required dependencies. " +
                                    "Please look at the latest.log in the '%s' folder to determine the issue.",
                            FilePaths.INSTANCE.getPluginFolder().getParent().relativize(FilePaths.INSTANCE.getPluginFolder().resolve("log").resolve(super.getName())))
            );
        }
    }

    protected abstract void enable();

    protected abstract void disable();

    protected abstract void load();
}
