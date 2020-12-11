package com.heretere.hac.util.plugin.dependency.relocation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.AbstractDependency;
import com.heretere.hac.util.plugin.dependency.DependencyLoader;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import com.heretere.hac.util.plugin.dependency.relocation.classloader.IsolatedClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Maven(groupId = "org.ow2.asm", artifactId = "asm-commons", version = "6.0")
@Maven(groupId = "org.ow2.asm", artifactId = "asm", version = "6.0")
@Maven(groupId = "me.lucko", artifactId = "jar-relocator", version = "1.3")
public final class Relocator {
    private final AbstractHACPlugin parent;
    private final DependencyLoader dependencyLoader;
    private final IsolatedClassLoader isolatedClassLoader;

    private Constructor<?> jarRelocatorConstructor;
    private Method jarRelocatorRunMethod;
    private Constructor<?> relocationConstructor;


    public Relocator(@NotNull AbstractHACPlugin parent, @NotNull DependencyLoader dependencyLoader) {
        this.parent = parent;
        this.dependencyLoader = dependencyLoader;

        this.isolatedClassLoader = new IsolatedClassLoader();

        Set<AbstractDependency> dependencies = this.dependencyLoader.getDependencies(Relocator.class);

        boolean success = dependencies.stream().allMatch(this.dependencyLoader::downloadDependency);

        if (success) {
            dependencies.forEach(dependency -> this.isolatedClassLoader.addPath(dependency.getDownloadLocation()));
        }

        try {
            Class<?> jarRelocatorClass = isolatedClassLoader.loadClass("me.lucko.jarrelocator.JarRelocator");
            Class<?> relocationClass = isolatedClassLoader.loadClass("me.lucko.jarrelocator.Relocation");

            this.jarRelocatorConstructor = jarRelocatorClass.getConstructor(File.class, File.class, Collection.class);
            this.jarRelocatorRunMethod = jarRelocatorClass.getMethod("run");

            relocationConstructor = relocationClass.getConstructor(String.class, String.class, Collection.class, Collection.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            this.parent.getLog().reportFatalError(e, true);
        }
    }

    public Optional<Throwable> relocate(@NotNull AbstractDependency dependency) {
        Optional<Throwable> output;
        try {
            Set<Object> rules = Sets.newLinkedHashSet();

            for (Relocation relocation : dependency.getRelocations()) {
                rules.add(
                        relocationConstructor.newInstance(
                                relocation.from(),
                                relocation.to(),
                                Lists.newArrayList(),
                                Lists.newArrayList()
                        )
                );
            }

            jarRelocatorRunMethod.invoke(jarRelocatorConstructor.newInstance(dependency.getDownloadLocation().toFile(), dependency.getRelocatedLocation().toFile(), rules));
            output = Optional.empty();
        } catch (ReflectiveOperationException e) {
            output = Optional.of(e);
        }
        return output;
    }
}
