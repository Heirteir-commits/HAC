package com.heretere.hac.core;

import com.heretere.hac.core.proxy.VersionProxy;
import com.heretere.hac.util.implementation.VersionProcessor;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class Core extends JavaPlugin {
    private VersionProxy versionProxy;

    @Override
    public void onLoad() {
        Class<?> versionProxyClass =
                VersionProcessor.getLatestVersionProxy(
                        "com.heretere.hac.core.implementation.versions"
                );

        if (versionProxyClass == null) {
            throw new NotImplementedException(); //TODO: incorporate with logger.
        }

        try {
            Object versionProxyUnchecked = versionProxyClass.getConstructor().newInstance();

            if (versionProxyUnchecked instanceof VersionProxy) {
                this.versionProxy = (VersionProxy) versionProxyUnchecked;
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace(); //TODO: move to logger
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.versionProxy.baseLoad();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.versionProxy.baseUnload();
    }

}
