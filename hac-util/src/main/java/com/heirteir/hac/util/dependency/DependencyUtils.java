package com.heirteir.hac.util.dependency;

import com.google.common.collect.Sets;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.AbstractDependency;
import com.heirteir.hac.util.dependency.types.GithubDependency;
import com.heirteir.hac.util.dependency.types.MavenDependency;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import com.heirteir.hac.util.dependency.types.annotation.Maven;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Set;

public final class DependencyUtils {
    private DependencyUtils() {
        throw new IllegalStateException("Utility Class");
    }

    public static boolean loadDependenciesFromPlugin(DependencyPlugin plugin) {
        Set<AbstractDependency> dependencies = Sets.newLinkedHashSet();

        if (plugin.getClass().isAnnotationPresent(Maven.List.class)) {
            Arrays.stream(plugin.getClass().getAnnotation(Maven.List.class).value())
                    .map(maven -> new MavenDependency(plugin, maven))
                    .forEachOrdered(dependencies::add);
        }

        if (plugin.getClass().isAnnotationPresent(Maven.class)) {
            dependencies.add(new MavenDependency(plugin, plugin.getClass().getAnnotation(Maven.class)));
        }

        if (plugin.getClass().isAnnotationPresent(Github.List.class)) {
            Arrays.stream(plugin.getClass().getAnnotation(Github.List.class).value())
                    .map(github -> new GithubDependency(plugin, github))
                    .forEachOrdered(dependencies::add);
        }

        if (plugin.getClass().isAnnotationPresent(Github.class)) {
            dependencies.add(new GithubDependency(plugin, plugin.getClass().getAnnotation(Github.class)));
        }

        return dependencies.stream().allMatch(dependency -> DependencyUtils.loadDependency(plugin, dependency));
    }

    public static boolean downloadDependency(DependencyPlugin plugin, AbstractDependency dependency) {
        boolean success = true;

        if (dependency.needsUpdate()) {
            success = dependency.download();
        }

        if (!success) {
            try {
                plugin.getLog().reportFatalError(String.format("Failed to download dependency '%s'. Please download the dependency from: '%s' and place it into the folder '%s'.",
                        dependency.getName(),
                        dependency.getManualURL(),
                        dependency.getDownloadLocation()));
            } catch (MalformedURLException e) {
                plugin.getLog().severe(e);
            }
        }

        return success;
    }

    public static boolean loadDependency(DependencyPlugin plugin, AbstractDependency dependency) {
        if (DependencyUtils.downloadDependency(plugin, dependency) && dependency.load()) {
            plugin.getLog().info(String.format("Successfully loaded dependency '%s'.", dependency.getName()));
            return true;
        }

        return false;
    }

}
