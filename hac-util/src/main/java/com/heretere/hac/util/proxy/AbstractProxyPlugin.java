package com.heretere.hac.util.proxy;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractProxyPlugin<T extends AbstractVersionProxy> extends JavaPlugin {
    private final String basePackage;

    private Class<T> proxyType;
    private T proxy;

    public AbstractProxyPlugin(String basePackage, Class<T> proxyType) {
        this.basePackage = basePackage;
        this.proxyType = proxyType;
    }

    protected abstract void enable();

    protected abstract void disable();

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

    private void loadVersionProxy() {
        Optional<Class<?>> versionProxyClass = VersionProcessor.getVersionProxy(this, this.basePackage);

        versionProxyClass.ifPresent(clazz ->
        {
            try {
                this.proxy = this.proxyType.cast(clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace(); //TODO: Move to logger
            }
        });

        if (!versionProxyClass.isPresent()) {
            String currentVersion = VersionProcessor.getServerVersionString();
            Set<String> packagedVersions = VersionProcessor.getPackagedVersions(this);


        }
    }

    public T getProxy() {
        return proxy;
    }
}
