/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.util.plugin.dependency;

import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.HACPlugin;
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
import java.security.AccessController;
import java.security.PrivilegedAction;
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
    private final @NotNull HACPlugin parent;
    /**
     * The relocator instance.
     */
    private final @NotNull Relocator relocator;

    /**
     * Instantiates a new Dependency loader.
     *
     * @param parent the parent
     */
    public DependencyLoader(final @NotNull HACPlugin parent) {
        this.parent = parent;
        this.relocator = new Relocator(parent, this);

        this.openClassLoaderJava9();
    }

    /**
     * Gives this module access to the URLClassLoader.
     * Thanks lucko :)
     * Modified from: lucko/LuckPerms repo on GitHub (ReflectiveClassLoader)
     */
    @SuppressWarnings("JavaReflectionInvocation") private void openClassLoaderJava9() {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            try {
                Class<?> moduleClass = Class.forName("java.lang.Module");
                Method getModuleMethod = Class.class.getMethod("getModule");
                Method addOpensMethod = moduleClass.getMethod("addOpens", String.class, moduleClass);

                Object urlClassLoaderModule = getModuleMethod.invoke(URLClassLoader.class);
                Object thisModule = getModuleMethod.invoke(DependencyLoader.class);

                addOpensMethod.invoke(urlClassLoaderModule, URLClassLoader.class.getPackage().getName(), thisModule);
            } catch (Exception ignored) {
                //Will throw error on <Java9
            }
            return null;
        });
    }

    /**
     * Gets all the dependency annotations or a class and processes them into Dependency objects.
     *
     * @param clazz the clazz
     * @return the dependencies
     */
    public @NotNull Set<Dependency> getDependencies(final @NotNull Class<?> clazz) {
        Set<Dependency> dependencies = Sets.newLinkedHashSet();
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
        Set<Dependency> set = this.getDependencies(this.parent.getClass());
        this.parent.getLog().info(() -> "Loading Dependencies...");
        boolean passed = set.parallelStream().allMatch(this::loadDependency);
        if (passed) {
            this.parent.getLog().info(() -> "Loaded " + set.size() + " Dependencies.");
        }
        return passed;
    }

    /**
     * Download dependency boolean.
     *
     * @param dependency the dependency
     * @return the boolean
     */
    public boolean downloadDependency(final @NotNull Dependency dependency) {
        boolean success = true;

        if (dependency.needsDownload() && dependency.needsRelocation()) {
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
    public boolean relocateDependency(final @NotNull Dependency dependency) {
        boolean success = true;

        if (dependency.needsRelocation()) {
            Optional<Throwable> relocate = this.relocator.relocate(dependency);

            if (relocate.isPresent()) {
                success = false;

                this.parent.getLog().reportFatalError(relocate.get(), false);
            } else {
                try {
                    Files.delete(dependency.getDownloadLocation());
                } catch (IOException e) {
                    success = false;
                    this.parent.getLog().reportFatalError(e, false);
                }
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
    public boolean loadDependency(final @NotNull Dependency dependency) {
        boolean success = this.downloadDependency(dependency);

        if (success) {
            success = this.relocateDependency(dependency);
        }

        if (success) {
            URLClassLoader classLoader = (URLClassLoader) this.parent.getClass().getClassLoader();

            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                    method.setAccessible(true);
                    return null;
                });
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
