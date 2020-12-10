package com.heretere.hac.util.proxy;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * This class is responsible for helping an implementation find a valid version implementation to send its processing to.
 */
public final class VersionProcessor {
    private static final String PACKAGED_VERSIONS_NAME = "packaged_versions.txt";

    private VersionProcessor() {
        throw new IllegalStateException("Utility class.");
    }

    /**
     * Retrieves a NMS version to use for version processing.
     *
     * @return The NMS version (example format: 1_16_R3).
     */
    public static String getServerVersionString() {
        return Iterables.get(Splitter.on('v').split(Bukkit.getServer().getClass().getPackage().getName()), 1);
    }

    /**
     * Get versions packaged in jar from resource file
     *
     * @param plugin the plugin
     * @return an ImmutableSet of packaged versions
     */
    public static ImmutableSet<String> getPackagedVersions(@NotNull Plugin plugin) {
        InputStream in = plugin.getResource(VersionProcessor.PACKAGED_VERSIONS_NAME);

        Preconditions.checkNotNull(in, "'%s' not located in jar. Please rebuild.", VersionProcessor.PACKAGED_VERSIONS_NAME);

        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .forEach(line -> builder.add(line.trim()));
        } catch (IOException e) {
            e.printStackTrace(); //TODO: move to logger
        }

        return builder.build();
    }

    /**
     * Get version proxy for current version
     *
     * @param basePackage The base package for all version proxies
     * @return Optional of version proxy class
     */
    public static Optional<Class<?>> getVersionProxy(Plugin plugin, String basePackage) {
        Optional<Class<?>> output;

        String currentVersion = VersionProcessor.getServerVersionString();
        if (VersionProcessor.getPackagedVersions(plugin).contains(currentVersion)) {
            try {
                output = Optional.of(
                        Class.forName(
                                basePackage + "." + currentVersion + ".Proxy",
                                true,
                                VersionProcessor.class.getClassLoader()
                        )
                );
            } catch (ClassNotFoundException e) {
                output = Optional.empty();
                e.printStackTrace();
            }
        } else {
            output = Optional.empty();
        }


        return output;
    }
}
