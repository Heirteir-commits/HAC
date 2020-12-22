package com.heretere.hac.util.plugin.dependency.relocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.AbstractDependency;
import com.heretere.hac.util.plugin.dependency.DependencyLoader;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import com.heretere.hac.util.plugin.dependency.relocation.classloader.IsolatedClassLoader;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * The type Relocator.
 */
@Maven(groupId = "org.ow2.asm", artifactId = "asm-commons", version = "6.0")
@Maven(groupId = "org.ow2.asm", artifactId = "asm", version = "6.0")
@Maven(groupId = "me.lucko", artifactId = "jar-relocator", version = "1.3")
public final class Relocator {
    /**
     * Isolated class loader to stop from polluting the class path with unneeded packages.
     */
    private final IsolatedClassLoader isolatedClassLoader;

    /**
     * The constructor of the jar relocator.
     */
    private Constructor<?> jarRelocatorConstructor;
    /**
     * The method to run the jar relocator.
     */
    private Method jarRelocatorRunMethod;
    /**
     * The relocation constructor.
     */
    private Constructor<?> relocationConstructor;


    /**
     * Instantiates a new Relocator.
     *
     * @param parent           the parent
     * @param dependencyLoader the dependency loader
     */
    public Relocator(
            @NotNull final AbstractHACPlugin parent,
            @NotNull final DependencyLoader dependencyLoader
    ) {
        this.isolatedClassLoader = new IsolatedClassLoader();

        Set<AbstractDependency> dependencies = dependencyLoader.getDependencies(Relocator.class);

        boolean success = dependencies.stream().allMatch(dependencyLoader::downloadDependency);

        if (success) {
            for (AbstractDependency dependency : dependencies) {
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
                Class<?> jarRelocatorClass = isolatedClassLoader.loadClass("me.lucko.jarrelocator.JarRelocator");
                Class<?> relocationClass = isolatedClassLoader.loadClass("me.lucko.jarrelocator.Relocation");

                this.jarRelocatorConstructor = jarRelocatorClass.getConstructor(File.class,
                                                                                File.class,
                                                                                Collection.class
                );
                this.jarRelocatorRunMethod = jarRelocatorClass.getMethod("run");

                relocationConstructor = relocationClass.getConstructor(String.class,
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
    public Optional<Throwable> relocate(@NotNull final AbstractDependency dependency) {
        Optional<Throwable> output;
        try {
            Set<Object> rules = Sets.newLinkedHashSet();

            for (Relocation relocation : dependency.getRelocations()) {
                rules.add(relocationConstructor.newInstance(StringUtils.replace(relocation.from(), "|", "."),
                                                            StringUtils.replace(relocation.to(), "|", "."),
                                                            Lists.newArrayList(),
                                                            Lists.newArrayList()
                ));
            }

            jarRelocatorRunMethod.invoke(jarRelocatorConstructor.newInstance(dependency.getDownloadLocation().toFile(),
                                                                             dependency.getRelocatedLocation().toFile(),
                                                                             rules
            ));
            output = Optional.empty();
        } catch (ReflectiveOperationException e) {
            output = Optional.of(e);
        }
        return output;
    }
}
