package com.heretere.hac.util.plugin.dependency;

import com.heretere.hac.util.plugin.AbstractHACPlugin;
import com.heretere.hac.util.plugin.dependency.annotations.Maven;
import com.heretere.hac.util.plugin.dependency.relocation.annotations.Relocation;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public final class MavenDependency extends AbstractDependency {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String repoURL;

    public MavenDependency(@NotNull AbstractHACPlugin parent, @NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull String repoURL, @NotNull Set<Relocation> relocations) {
        super(parent, relocations);
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repoURL = repoURL;
    }

    public MavenDependency(@NotNull AbstractHACPlugin parent, @NotNull Maven maven, @NotNull Set<Relocation> relocations) {
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
            url = new URL(String.format("%s%s/%s/%s/%s-%s.jar", this.repoURL, this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version));
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
