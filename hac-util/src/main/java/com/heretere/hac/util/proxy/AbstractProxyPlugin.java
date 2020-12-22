package com.heretere.hac.util.proxy;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

public abstract class AbstractProxyPlugin<T extends AbstractVersionProxy> extends AbstractHACPlugin {
    /**
     * This .txt is created by the build.gradle it helps in figuring out what NMS versions this
     * plugin supports at runtime.
     */
    private static final String PACKAGED_VERSIONS_NAME = "packaged_versions.txt";

    /**
     * The base package location of the version proxies located in this plugin.
     */
    private final String basePackage;

    /**
     * The T AbstractVersionProxy class type that should be found.
     */
    private final Class<T> versionProxyClass;

    /**
     * The T version proxy instance.
     */
    private T proxy;

    /**
     * Whether or not a version proxy was successfully found.
     */
    private boolean success;

    protected AbstractProxyPlugin(
        @NotNull final String baseDirectory,
        @NotNull final String prefix,
        @NotNull final String basePackage,
        @NotNull final Class<T> versionProxyClass
    ) {
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
                    this.basePackage + "." + currentVersion + ".Proxy",
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

        Preconditions.checkNotNull(
            in,
            "'%s' not located in jar. Please rebuild.",
            AbstractProxyPlugin.PACKAGED_VERSIONS_NAME
        );

        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            reader.lines().filter(line -> !line.trim().isEmpty()).forEach(line -> builder.add(line.trim()));
        } catch (IOException e) {
            super.getLog().severe(e);
        }

        return builder.build();
    }

    /**
     * Similar to {@link AbstractHACPlugin#load()} except it is passed after the proxy is loaded.
     */
    public abstract void proxyLoad();

    /**
     * Similar to {@link AbstractHACPlugin#enable()} except it is passed after the proxy is loaded.
     */
    public abstract void proxyEnable();

    /**
     * Similar to {@link AbstractHACPlugin#disable()} except it is passed after the proxy is loaded.
     */
    public abstract void proxyDisable();

    /**
     * The T proxy instance.
     *
     * @return T version proxy instance.
     */
    public T getProxy() {
        return this.proxy;
    }
}

