package com.heirteir.hac.util.dependency;

import com.google.common.collect.Sets;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.AbstractDependency;
import com.heirteir.hac.util.dependency.types.MavenDependency;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public final class Dependencies {

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

        return dependencies.stream().allMatch(Dependencies::loadDependency);
    }

    private static boolean downloadDependency(AbstractDependency dependency, Path location) {
        boolean success = true;
        if (!Files.exists(location)) {
            Log.INSTANCE.info(String.format("Dependency '%s' is not in the library folder '%s'. Downloading now...", dependency.getName(), location.getParent().toAbsolutePath()));

            URL url = dependency.getUrl();

            try {
                Files.createDirectories(location.getParent());
                InputStream is = url.openStream();
                Files.copy(is, location);
                Log.INSTANCE.info(String.format("Dependency '%s' successfully downloaded.", dependency.getName()));
            } catch (IOException e) {
                success = false;
                Log.INSTANCE.severe(
                        String.format("Failed to download dependency '%s'. Please download the dependency from: '%s' and place it into the folder '%s'.",
                                dependency.getName(),
                                dependency.getUrl().toString(),
                                location.getParent().toAbsolutePath())
                );
            }
        }

        return success;
    }

    private static boolean loadDependency(AbstractDependency dependency) {
        Path location = FilePaths.INSTANCE.getPluginFolder().resolve("dependencies").resolve(dependency.getName() + ".jar");
        boolean success = Dependencies.downloadDependency(dependency, location);

        if (success) {
            URLClassLoader classLoader = (URLClassLoader) Dependencies.class.getClassLoader();

            Log.INSTANCE.info(String.format("Attempting to Load dependency '%s'.", dependency.getName()));

            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(classLoader, location.toUri().toURL());
            } catch (NoSuchMethodException | MalformedURLException | IllegalAccessException | InvocationTargetException e) {
                Log.INSTANCE.severe(e);
                success = false;
            }
        }

        if (success) {
            Log.INSTANCE.info(String.format("Successfully loaded dependency '%s'.", dependency.getName()));
        }

        return success;
    }

}
