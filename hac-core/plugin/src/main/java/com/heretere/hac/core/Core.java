package com.heretere.hac.core;

import com.heretere.hac.core.implementation.versions.VersionImplementation;
import com.heretere.hac.core.player.HACPlayerListUpdater;
import com.heretere.hac.util.implementation.VersionProcessor;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class Core extends JavaPlugin {
    private VersionImplementation versionImplementation;
    private HACPlayerListUpdater hacPlayerListUpdater;

    @Override
    public void onLoad() {
        Class<?> versionImplementationClass =
                VersionProcessor.getLatestVersionImplementation(
                        "com.heretere.hac.core.implementation.versions"
                );

        if (versionImplementationClass == null) {
            throw new NotImplementedException(); //TODO: incorporate with logger.
        }

        try {
            Object versionImplementationUnchecked = versionImplementationClass.getConstructor().newInstance();

            if (versionImplementationUnchecked instanceof VersionImplementation) {
                this.versionImplementation = (VersionImplementation) versionImplementationUnchecked;
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace(); //TODO: move to logger
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.versionImplementation.registerPackets();

        this.hacPlayerListUpdater = new HACPlayerListUpdater(this);
    }

    @Override
    public void onDisable() {
        this.hacPlayerListUpdater.unload();
    }

    public VersionImplementation getVersionImplementation() {
        return versionImplementation;
    }
}
