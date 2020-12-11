package com.heretere.hac.util.plugin.dependency;

import com.heretere.hac.util.plugin.AbstractHACPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

public abstract class AbstractDependency {
    private final AbstractHACPlugin parent;

    protected AbstractDependency(@NotNull AbstractHACPlugin parent) {
        this.parent = parent;
    }

    public abstract boolean needsUpdate();

    public abstract Path getDownloadLocation();

    public abstract Optional<URL> getManualURL();

    public abstract Optional<URL> getDownloadURL();

    public abstract String getName();

    protected AbstractHACPlugin getParent() {
        return this.parent;
    }
}
