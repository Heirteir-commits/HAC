package com.heretere.hac.util.plugin.dependency;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractDependency {
    private final AbstractHACPlugin parent;
    private final Set<Relocation> relocations;

    protected AbstractDependency(@NotNull AbstractHACPlugin parent, Relocation... relocations) {
        this.parent = parent;

        this.relocations = ImmutableSet.copyOf(relocations);
    }

    public abstract boolean needsUpdate();

    public abstract boolean needsRelocation();

    public abstract Path getDownloadLocation();

    public abstract Path getRelocatedLocation();

    public abstract Optional<URL> getManualURL();

    public abstract Optional<URL> getDownloadURL();

    public abstract String getName();

    protected AbstractHACPlugin getParent() {
        return this.parent;
    }

    public Set<Relocation> getRelocations() {
        return relocations;
    }
}
