package com.heretere.hac.util.plugin.dependency;

import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.Relocator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class DependencyLoader {
    private final AbstractHACPlugin parent;
    private final Relocator relocator;

    public DependencyLoader(AbstractHACPlugin parent) {
        this.parent = parent;
        this.relocator = new Relocator(parent, this);
    }

    public Set<AbstractDependency> getDependencies(Class<?> clazz) {
        Set<AbstractDependency> dependencies = Sets.newLinkedHashSet();

        if (clazz.isAnnotationPresent(Maven.List.class)) {
            for (Maven maven : clazz.getAnnotation(Maven.List.class).value()) {
                dependencies.add(new MavenDependency(this.parent, maven));
            }
        }

        if (clazz.isAnnotationPresent(Maven.class)) {
            dependencies.add(new MavenDependency(this.parent, clazz.getAnnotation(Maven.class)));
        }

        return dependencies;
    }

    public boolean loadDependencies() {
        return this.getDependencies(this.parent.getClass()).parallelStream().allMatch(this::loadDependency);
    }

    public boolean downloadDependency(AbstractDependency dependency) {
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
            this.parent.getLog().reportFatalError(
                    () ->
                            String.format("Failed to download dependency '%s'. " +
                                            "Please download the dependency from: '%s' and place it into the folder '%s'.",
                                    dependency.getName(),
                                    dependency.getManualURL().orElse(null),
                                    dependency.getDownloadLocation()),
                    true);
        }

        return success;
    }

    public boolean relocateDependency(AbstractDependency dependency) {
        boolean success = true;

        if (dependency.needsRelocation()) {
            this.parent.getLog().info(() -> String.format("Relocating dependency '%s'.", dependency.getName()));
            Optional<Throwable> relocate = this.relocator.relocate(dependency);

            if (relocate.isPresent()) {
                success = false;

                this.parent.getLog().reportFatalError(relocate.get(), true);
            }
        }

        return success;
    }

    public boolean loadDependency(AbstractDependency dependency) {
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
            } catch (NoSuchMethodException | MalformedURLException | IllegalAccessException | InvocationTargetException e) {
                this.parent.getLog().reportFatalError(e, true);
                success = false;
            }
        }

        return success;
    }
}
