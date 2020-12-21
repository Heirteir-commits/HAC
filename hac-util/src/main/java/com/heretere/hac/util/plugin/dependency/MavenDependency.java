package com.heretere.hac.util.plugin.dependency;

import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

/**
 * The type Maven dependency.
 */
public final class MavenDependency extends AbstractDependency {
    /**
     * The group id.
     */
    private final String groupId;
    /**
     * The artifact id.
     */
    private final String artifactId;
    /**
     * The version.
     */
    private final String version;
    /**
     * The repo url.
     */
    private final String repoURL;

    /**
     * Instantiates a new Maven dependency.
     *
     * @param parent      the parent
     * @param groupId     the group id
     * @param artifactId  the artifact id
     * @param version     the version
     * @param repoURL     the repo url
     * @param relocations the relocations
     */
    public MavenDependency(@NotNull final AbstractHACPlugin parent,
                           @NotNull final String groupId,
                           @NotNull final String artifactId,
                           @NotNull final String version,
                           @NotNull final String repoURL,
                           @NotNull final Set<Relocation> relocations) {
        super(parent, relocations);
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repoURL = repoURL + (StringUtils.endsWith(repoURL, "/") ? "" : "/");
    }

    /**
     * Instantiates a new Maven dependency.
     *
     * @param parent      the parent
     * @param maven       the maven
     * @param relocations the relocations
     */
    public MavenDependency(@NotNull final AbstractHACPlugin parent,
                           @NotNull final Maven maven,
                           @NotNull final Set<Relocation> relocations) {
        this(parent, maven.groupId(), maven.artifactId(), maven.version(), maven.repoUrl(), relocations);
    }

    @Override
    public boolean needsUpdate() {
        return !Files.exists(this.getDownloadLocation());
    }

    @Override
    public boolean needsRelocation() {
        return !Files.exists(this.getRelocatedLocation());
    }

    @Override
    public Path getDownloadLocation() {
        return super.getParent().getBaseDirectory().resolve("dependencies").resolve(this.getName() + ".jar");
    }

    @Override
    public Path getRelocatedLocation() {
        return super.getParent().getBaseDirectory().resolve("dependencies").resolve(this.getName() + "-relocated.jar");
    }

    @Override
    public Optional<URL> getManualURL() {
        return this.getDownloadURL();
    }

    @Override
    public Optional<URL> getDownloadURL() {
        URL url;

        try {
            url = new URL(
                    String.format("%s%s/%s/%s/%s-%s.jar",
                            this.repoURL,
                            this.groupId.replace(".", "/"),
                            this.artifactId,
                            this.version,
                            this.artifactId,
                            this.version));
        } catch (MalformedURLException e) {
            url = null;
        }

        return Optional.ofNullable(url);
    }

    @Override
    public String getName() {
        return this.artifactId + "-" + this.version;
    }
}
