package com.heirteir.hac.util.dependency;

import com.google.common.collect.Sets;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.AbstractDependency;
import com.heirteir.hac.util.dependency.types.GithubDependency;
import com.heirteir.hac.util.dependency.types.MavenDependency;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import com.heirteir.hac.util.logging.Log;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Set;

public final class DependencyUtils {

    public static boolean loadDependenciesFromPlugin(DependencyPlugin plugin) {
        Set<AbstractDependency> dependencies = Sets.newLinkedHashSet();

        if (plugin.getClass().isAnnotationPresent(Maven.List.class)) {
            Arrays.stream(plugin.getClass().getAnnotation(Maven.List.class).value())
                    .map(MavenDependency::new)
                    .forEachOrdered(dependencies::add);
        }

        if (plugin.getClass().isAnnotationPresent(Maven.class)) {
            dependencies.add(new MavenDependency(plugin.getClass().getAnnotation(Maven.class)));
        }

        if (plugin.getClass().isAnnotationPresent(Github.List.class)) {
            Arrays.stream(plugin.getClass().getAnnotation(Github.List.class).value())
                    .map(GithubDependency::new)
                    .forEachOrdered(dependencies::add);
        }

        if (plugin.getClass().isAnnotationPresent(Github.class)) {
            dependencies.add(new GithubDependency(plugin.getClass().getAnnotation(Github.class)));
        }

        return dependencies.stream().allMatch(DependencyUtils::loadDependency);
    }

    private static boolean downloadDependency(AbstractDependency dependency) {
        boolean success = true;

        if (dependency.needsUpdate()) {
            success = dependency.download();
        }

        if (!success) {
            try {
                Log.INSTANCE.reportFatalError(String.format("Failed to download dependency '%s'. Please download the dependency from: '%s' and place it into the folder '%s'.",
                        dependency.getName(),
                        dependency.getManualURL(),
                        dependency.getDownloadLocation()));
            } catch (MalformedURLException e) {
                Log.INSTANCE.severe(e);
            }
        }

        return success;
    }

    public static boolean loadDependency(AbstractDependency dependency) {
        if (DependencyUtils.downloadDependency(dependency) && dependency.load()) {
            Log.INSTANCE.info(String.format("Successfully loaded dependency '%s'.", dependency.getName()));
            return true;
        }

        return false;
    }

}
