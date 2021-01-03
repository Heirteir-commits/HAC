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

package com.heretere.hac.util.plugin.dependency.relocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.HACPlugin;
import com.heretere.hac.util.plugin.dependency.Dependency;
import com.heretere.hac.util.plugin.dependency.DependencyLoader;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import com.heretere.hac.util.plugin.dependency.relocation.classloader.IsolatedClassLoader;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * The type Relocator.
 */
@Maven(groupId = "org.ow2.asm", artifactId = "asm-commons", version = "7.0")
@Maven(groupId = "org.ow2.asm", artifactId = "asm", version = "7.0")
@Maven(groupId = "me.lucko", artifactId = "jar-relocator", version = "1.4")
public final class Relocator {
    /**
     * Isolated class loader to stop from polluting the class path with unneeded packages.
     */
    private @Nullable IsolatedClassLoader isolatedClassLoader;

    /**
     * The constructor of the jar relocator.
     */
    private @Nullable Constructor<?> jarRelocatorConstructor;
    /**
     * The method to run the jar relocator.
     */
    private @Nullable Method jarRelocatorRunMethod;
    /**
     * The relocation constructor.
     */
    private @Nullable Constructor<?> relocationConstructor;


    /**
     * Instantiates a new Relocator.
     *
     * @param parent           the parent
     * @param dependencyLoader the dependency loader
     */
    public Relocator(
        final @NotNull HACPlugin parent,
        final @NotNull DependencyLoader dependencyLoader
    ) {
        AccessController.doPrivileged((PrivilegedAction<?>) () -> this.isolatedClassLoader = new IsolatedClassLoader());

        Set<Dependency> dependencies = dependencyLoader.getDependencies(Relocator.class);

        boolean success = dependencies.stream().allMatch(dependencyLoader::downloadDependency);

        if (success) {
            for (Dependency dependency : dependencies) {
                if (!this.isolatedClassLoader.addPath(dependency.getDownloadLocation())) {
                    success = false;
                    parent.getLog().reportFatalError(
                        () -> "Failed to load dependency '" + dependency.getDownloadLocation() + "'." + "Please " +
                            "delete it and re-download it.",
                        false
                    );
                    break;
                }
            }
            dependencies.forEach(dependency -> this.isolatedClassLoader.addPath(dependency.getDownloadLocation()));
        }

        if (success) {
            try {
                Class<?> jarRelocatorClass = this.isolatedClassLoader.loadClass("me.lucko.jarrelocator.JarRelocator");
                Class<?> relocationClass = this.isolatedClassLoader.loadClass("me.lucko.jarrelocator.Relocation");

                this.jarRelocatorConstructor = jarRelocatorClass.getConstructor(
                    File.class,
                    File.class,
                    Collection.class
                );
                this.jarRelocatorRunMethod = jarRelocatorClass.getMethod("run");

                this.relocationConstructor = relocationClass.getConstructor(
                    String.class,
                    String.class,
                    Collection.class,
                    Collection.class
                );
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                parent.getLog().reportFatalError(e, true);
            }
        }
    }

    /**
     * Relocates the dependency. throws an optional throwable if an error occurred.
     *
     * @param dependency the dependency
     * @return the optional
     */
    public @NotNull Optional<Throwable> relocate(final @NotNull Dependency dependency) {
        Optional<Throwable> output;

        if (!(this.isolatedClassLoader == null
            || this.jarRelocatorConstructor == null
            || this.jarRelocatorRunMethod == null
            || this.relocationConstructor == null)) {
            try {
                Set<Object> rules = Sets.newLinkedHashSet();

                for (Relocation relocation : dependency.getRelocations()) {
                    rules.add(this.relocationConstructor.newInstance(
                        StringUtils.replace(relocation.from(), "|", "."),
                        StringUtils.replace(relocation.to(), "|", "."),
                        Lists.newArrayList(),
                        Lists.newArrayList()
                    ));
                }

                this.jarRelocatorRunMethod.invoke(this.jarRelocatorConstructor.newInstance(
                    dependency.getDownloadLocation().toFile(),
                    dependency.getRelocatedLocation().toFile(),
                    rules
                ));
                output = Optional.empty();
            } catch (ReflectiveOperationException e) {
                output = Optional.of(e);
            }
        } else {
            output = Optional.empty();
        }

        return output;
    }
}
