package com.heretere.hac.util.plugin.dependency;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractDependency {
    /**
     * The HACAPI reference.
     */
    private final @NotNull AbstractHACPlugin parent;

    /**
     * These relocations are processed {@link com.heretere.hac.util.plugin.dependency.relocation.Relocator}
     * to move packages from downloaded dependencies to a class path safe package.
     */
    private final @NotNull Set<Relocation> relocations;

    protected AbstractDependency(
        final @NotNull AbstractHACPlugin parent,
        final @NotNull Relocation... relocations
    ) {
        this.parent = parent;

        this.relocations = ImmutableSet.copyOf(relocations);
    }

    protected AbstractDependency(
        final @NotNull AbstractHACPlugin parent,
        final @NotNull Collection<Relocation> relocations
    ) {
        this.parent = parent;

        this.relocations = ImmutableSet.copyOf(relocations);
    }

    /**
     * Whether or not this dependency should be downloaded.
     *
     * @return true if it should be downloaded.
     */
    public abstract boolean needsUpdate();

    /**
     * Whether or not this dependency needs to be ran through the Relocator.
     *
     * @return true if it needs to be relocated.
     */
    public abstract boolean needsRelocation();

    /**
     * Where this file should be downloaded to. This should not be equal to the output provided by
     * {@link AbstractDependency#getRelocatedLocation()}.
     *
     * @return The download location of the dependency.
     */
    public abstract @NotNull Path getDownloadLocation();

    /**
     * Where the file containing the relocated packages should be placed. This should not be equal to the output
     * provided by {@link AbstractDependency#getDownloadLocation()}.
     *
     * @return The packaged relocated file location.
     */
    public abstract @NotNull Path getRelocatedLocation();

    /**
     * If the download fails somehow this url is provided for the user to manually download the dependency and place it
     * it into the dependencies folder.
     *
     * @return An optional of the manual download location. The optional should be empty when there was an error parsing
     * the url.
     */
    public abstract @NotNull Optional<URL> getManualURL();

    /**
     * The download location of the dependency.
     *
     * @return The download location of the dependency. The optional should be empty if there is an error parsing the
     * url.
     */
    public abstract @NotNull Optional<URL> getDownloadURL();

    /**
     * The name of the dependency ideally it contains name & version. This value is sent to the logger.
     *
     * @return The name of the dependency.
     */
    public abstract @NotNull String getName();

    /**
     * The HACAPI reference to be used by child classes.
     *
     * @return The HACAPI reference.
     */
    protected final @NotNull AbstractHACPlugin getParent() {
        return this.parent;
    }

    /**
     * The current relocations that should be processed by this dependency.
     *
     * @return A immutable set of the relocations.
     */
    public @NotNull Set<Relocation> getRelocations() {
        return this.relocations;
    }
}
