package com.heretere.hac.util.implementation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for helping an implementation find a valid version implementation to send its processing to.
 */
public final class VersionProcessor {
    /**
     * Possible versions that could be available.
     */
    private static final Set<String> possibleVersions = Sets.newLinkedHashSet(
            Lists.newArrayList(
                    "1_8_R3",
                    "1_9_R2",
                    "1_10_R1",
                    "1_11_R1",
                    "1_12_R1",
                    "1_13_R2",
                    "1_14_R1",
                    "1_15_R1",
                    "1_16_R3"
            )
    );

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
     * Check which possible versions are actually available and creates a Set from them.
     *
     * @param basePackage The base package to check
     * @return a {@link LinkedHashSet} of the available versions.
     */
    public static Set<String> getAvailableVersions(String basePackage) {
        return VersionProcessor.possibleVersions.stream()
                .filter(version -> VersionProcessor.findVersionImplementationClass(VersionProcessor.basePackageVersionAppend(basePackage, version), false) != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Finds a version implementation for a specified path.
     *
     * @param path The path to a possible version implementation (can be wrong).
     * @param init Whether or not to add the class to the classloader if found.
     * @return the version implementation class.
     */
    private static Class<?> findVersionImplementationClass(String path, boolean init) {
        Class<?> clazz;

        try {
            clazz = Class.forName(path, init, VersionProcessor.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            clazz = null;
        }

        return clazz;
    }

    @Nullable
    public static Class<?> getLatestVersionImplementation(String basePackage) {
        Class<?> versionImplementationClass;

        String serverVersion = VersionProcessor.getServerVersionString();
        if (VersionProcessor.getAvailableVersions(basePackage).contains(serverVersion)) {
            versionImplementationClass = VersionProcessor.findVersionImplementationClass(VersionProcessor.basePackageVersionAppend(basePackage, serverVersion), true);
        } else {
            versionImplementationClass = null;
        }

        return versionImplementationClass;
    }

    /**
     * concat the base package and version into a path to use with
     * {@link VersionProcessor#findVersionImplementationClass(String, boolean)}.
     *
     * @param basePackage the base package
     * @param version     the version
     * @return a concatenated path to the version implementation.
     */
    private static String basePackageVersionAppend(String basePackage, String version) {
        return basePackage + "." + version + ".Implementation";
    }
}
