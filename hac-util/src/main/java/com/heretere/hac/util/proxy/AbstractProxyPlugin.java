package com.heretere.hac.util.proxy;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;

public abstract class AbstractProxyPlugin<T extends AbstractVersionProxy> extends AbstractHACPlugin {
    private static final String PACKAGED_VERSIONS_NAME = "packaged_versions.txt";

    private final String basePackage;
    private final Class<T> versionProxyClass;

    private T proxy;
    private boolean success;

    protected AbstractProxyPlugin(String baseDirectory, String prefix, String basePackage, Class<T> versionProxyClass) {
        super(baseDirectory, prefix);
        this.basePackage = basePackage;
        this.versionProxyClass = versionProxyClass;
    }

    private static String getServerVersionString() {
        return Iterables.get(Splitter.on('v').split(Bukkit.getServer().getClass().getPackage().getName()), 1);
    }

    @Override
    public final void load() {

        this.success = this.loadProxy();

        if (this.success) {
            this.proxyLoad();
        }
    }

    @Override
    public final void enable() {
        if (this.success) {
            this.proxyEnable();
        } else {
            super.getLog().reportFatalError(() -> String.format(
                    "No version proxy found for server version '%s'. Jar only contains versions '%s'.",
                    AbstractProxyPlugin.getServerVersionString(),
                    Arrays.toString(this.getPackagedVersions().toArray(new String[0]))
            ), false);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public final void disable() {
        if (this.success) {
            this.proxyDisable();
        }
    }

    private boolean loadProxy() {
        boolean output;
        String currentVersion = AbstractProxyPlugin.getServerVersionString();
        if (this.getPackagedVersions().contains(currentVersion)) {
            try {
                Class<?> clazz = Class.forName(
                        basePackage + "." + currentVersion + ".Proxy",
                        true,
                        this.getClass().getClassLoader()
                );

                Object instance = clazz.getConstructor(AbstractHACPlugin.class).newInstance(this);

                this.proxy = this.versionProxyClass.cast(instance);

                output = true;
            } catch (Exception e) {
                output = false;
                super.getLog().reportFatalError(e, false);
            }
        } else {
            output = false;
        }
        return output;
    }

    private Set<String> getPackagedVersions() {
        InputStream in = this.getResource(AbstractProxyPlugin.PACKAGED_VERSIONS_NAME);

        Preconditions.checkNotNull(in, "'%s' not located in jar. Please rebuild.", AbstractProxyPlugin.PACKAGED_VERSIONS_NAME);

        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .forEach(line -> builder.add(line.trim()));
        } catch (IOException e) {
            super.getLog().severe(e);
        }

        return builder.build();
    }

    public abstract void proxyLoad();

    public abstract void proxyEnable();

    public abstract void proxyDisable();

    protected T getProxy() {
        return proxy;
    }
}

