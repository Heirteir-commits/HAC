package com.heretere.hac.util.plugin.dependency;

import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.Relocator;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import org.jetbrains.annotations.NotNull;

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
import java.util.Optional;
import java.util.Set;

/**
 * The type Dependency loader.
 */
public class DependencyLoader {
    /**
     * The HACPlugin reference.
     */
    private final AbstractHACPlugin parent;
    /**
     * The relocator instance.
     */
    private final Relocator relocator;

    /**
     * Instantiates a new Dependency loader.
     *
     * @param parent the parent
     */
    public DependencyLoader(@NotNull final AbstractHACPlugin parent) {
        this.parent = parent;
        this.relocator = new Relocator(parent, this);
    }

    /**
     * Gets all the dependency annotations or a class and processes them into AbstractDependency objects.
     *
     * @param clazz the clazz
     * @return the dependencies
     */
    public Set<AbstractDependency> getDependencies(@NotNull final Class<?> clazz) {
        Set<AbstractDependency> dependencies = Sets.newLinkedHashSet();
        Set<Relocation> relocations = Sets.newLinkedHashSet();

        if (clazz.isAnnotationPresent(Relocation.List.class)) {
            relocations.addAll(Arrays.asList(clazz.getAnnotation(Relocation.List.class).value()));
        }

        if (clazz.isAnnotationPresent(Relocation.class)) {
            relocations.add(clazz.getAnnotation(Relocation.class));
        }

        if (clazz.isAnnotationPresent(Maven.List.class)) {
            for (Maven maven : clazz.getAnnotation(Maven.List.class).value()) {
                dependencies.add(new MavenDependency(this.parent, maven, relocations));
            }
        }

        if (clazz.isAnnotationPresent(Maven.class)) {
            dependencies.add(new MavenDependency(this.parent, clazz.getAnnotation(Maven.class), relocations));
        }

        return dependencies;
    }

    /**
     * Attempts to load all the dependencies.
     *
     * @return true if all dependencies were successfully loaded
     */
    public boolean loadDependencies() {
        return this.getDependencies(this.parent.getClass()).parallelStream().allMatch(this::loadDependency);
    }

    /**
     * Download dependency boolean.
     *
     * @param dependency the dependency
     * @return the boolean
     */
    public boolean downloadDependency(@NotNull final AbstractDependency dependency) {
        boolean success = true;

        if (dependency.needsUpdate()) {
            this.parent.getLog().info(() -> String.format("Downloading dependency '%s'.", dependency.getName()));

            Optional<URL> optionalURL = dependency.getDownloadURL();

            if (optionalURL.isPresent()) {
                try (InputStream is = optionalURL.get().openStream()) {
                    Path downloadLocation = dependency.getDownloadLocation();
                    Files.createDirectories(downloadLocation.getParent());
                    Files.deleteIfExists(downloadLocation);
                    Files.copy(is, downloadLocation);
                } catch (IOException e) {
                    success = false;
                }
            } else {
                success = false;
            }
        }

        if (!success) {
            this.parent.getLog().reportFatalError(() -> String.format(
                    "Failed to download dependency '%s'. " + "Please download the dependency from: '%s' " + "and " +
                            "place it into the folder '%s'.",
                    dependency.getName(),
                    dependency.getManualURL().orElse(null),
                    dependency.getDownloadLocation()
            ), false);
        }

        return success;
    }

    /**
     * Relocates the packages in a dependency.
     *
     * @param dependency the dependency
     * @return true if dependency was successfully relocated.
     */
    public boolean relocateDependency(@NotNull final AbstractDependency dependency) {
        boolean success = true;

        if (dependency.needsRelocation()) {
            this.parent.getLog().info(() -> String.format("Relocating dependency '%s'.", dependency.getName()));
            Optional<Throwable> relocate = this.relocator.relocate(dependency);

            if (relocate.isPresent()) {
                success = false;

                this.parent.getLog().reportFatalError(relocate.get(), false);
            }
        }

        return success;
    }

    /**
     * Load dependency boolean.
     *
     * @param dependency the dependency
     * @return the boolean
     */
    public boolean loadDependency(@NotNull final AbstractDependency dependency) {
        boolean success = this.downloadDependency(dependency);

        if (success) {
            success = this.relocateDependency(dependency);
        }

        if (success) {
            URLClassLoader classLoader = (URLClassLoader) this.parent.getClass().getClassLoader();

            this.parent.getLog().info(() -> String.format("Loading dependency '%s'.", dependency.getName()));

            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(classLoader, dependency.getRelocatedLocation().toUri().toURL());
            } catch (NoSuchMethodException
                    | MalformedURLException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                this.parent.getLog().reportFatalError(e, false);
                success = false;
            }
        }

        return success;
    }
}
