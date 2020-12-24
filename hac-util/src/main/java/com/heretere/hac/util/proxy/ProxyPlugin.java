package com.heretere.hac.util.proxy;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.heretere.hac.util.plugin.HACPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

public abstract class ProxyPlugin<T extends VersionProxy> extends HACPlugin {
    /**
     * This .txt is created by the build.gradle it helps in figuring out what NMS versions this
     * plugin supports at runtime.
     */
    private static final @NotNull String PACKAGED_VERSIONS_NAME = "packaged_versions.txt";

    /**
     * The base package location of the version proxies located in this plugin.
     */
    private final @NotNull String basePackage;

    /**
     * The T VersionProxy class type that should be found.
     */
    private final @NotNull Class<T> versionProxyClass;

    /**
     * The T version proxy instance.
     */
    private @Nullable T proxy;

    /**
     * Whether or not a version proxy was successfully found.
     */
    private boolean success;

    protected ProxyPlugin(
        final @NotNull String baseDirectory,
        final @NotNull String prefix,
        final @NotNull String basePackage,
        final @NotNull Class<T> versionProxyClass
    ) {
        super(baseDirectory, prefix);
        this.basePackage = basePackage;
        this.versionProxyClass = versionProxyClass;
    }

    /**
     * Get server version as string.
     *
     * @return server version
     */
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
                ProxyPlugin.getServerVersionString(),
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
        String currentVersion = ProxyPlugin.getServerVersionString();
        if (this.getPackagedVersions().contains(currentVersion)) {
            try {
                Class<?> clazz = Class.forName(
                    this.basePackage + "." + currentVersion + ".Proxy",
                    true,
                    this.getClass().getClassLoader()
                );

                Object instance = clazz.getConstructor(HACPlugin.class).newInstance(this);

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

    private @NotNull Set<String> getPackagedVersions() {
        InputStream in = this.getResource(ProxyPlugin.PACKAGED_VERSIONS_NAME);

        Preconditions.checkNotNull(
            in,
            "'%s' not located in jar. Please rebuild.",
            ProxyPlugin.PACKAGED_VERSIONS_NAME
        );

        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            reader.lines().filter(line -> !line.trim().isEmpty()).forEach(line -> builder.add(line.trim()));
            in.close();
        } catch (IOException e) {
            super.getLog().severe(e);
        }

        return builder.build();
    }

    /**
     * Similar to {@link HACPlugin#load()} except it is passed after the proxy is loaded.
     */
    public abstract void proxyLoad();

    /**
     * Similar to {@link HACPlugin#enable()} except it is passed after the proxy is loaded.
     */
    public abstract void proxyEnable();

    /**
     * Similar to {@link HACPlugin#disable()} except it is passed after the proxy is loaded.
     */
    public abstract void proxyDisable();

    /**
     * The T proxy instance.
     *
     * @return T version proxy instance.
     */
    public @NotNull T getProxy() {
        assert this.proxy != null;
        return this.proxy;
    }
}

