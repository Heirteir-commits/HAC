package com.heirteir.hac.util.dependency.types;

import com.heirteir.hac.util.dependency.DependencyUtils;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import lombok.Getter;
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

@Getter
public final class MavenDependency extends AbstractDependency {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String repoUrl;

    public MavenDependency(DependencyPlugin parent, @NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull String repoUrl) {
        super(parent);
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repoUrl = repoUrl.endsWith("/") ? repoUrl : repoUrl + "/";
    }

    public MavenDependency(DependencyPlugin parent, @NotNull Maven maven) {
        this(parent, maven.groupId(), maven.artifactId(), maven.version(), maven.repoUrl());
    }

    @Override
    public boolean needsUpdate() {
        return !Files.exists(this.getDownloadLocation());
    }

    @Override
    public Path getDownloadLocation() {
        return super.getDependencyPlugin().getPluginFolder().resolve("dependencies").resolve(this.getName() + ".jar");
    }

    @Override
    public URL getManualURL() throws MalformedURLException {
        return this.getUrl();
    }

    @Override
    public URL getUrl() throws MalformedURLException {
        return new URL(String.format("%s%s/%s/%s/%s-%s.jar", this.repoUrl, this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version));
    }

    @Override
    public String getName() {
        return this.artifactId + "-" + this.version;
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s from %s", this.groupId, this.artifactId, this.version, this.repoUrl);
    }

    @Override
    public boolean download() {
        boolean success = true;
        super.getDependencyPlugin().getLog().info(String.format("Dependency '%s' is not in the library folder '%s'. Downloading now...", this.getName(), this.getDownloadLocation().getParent().toAbsolutePath()));
        URL url = null;

        try {
            url = this.getUrl();
        } catch (MalformedURLException e) {
            success = false;
        }

        if (url != null) {
            try (InputStream is = url.openStream()) {
                Files.createDirectories(this.getDownloadLocation().getParent());
                Files.deleteIfExists(this.getDownloadLocation());
                Files.copy(is, this.getDownloadLocation());
                super.getDependencyPlugin().getLog().info(String.format("Dependency '%s' successfully downloaded.", this.getName()));
            } catch (IOException e) {
                success = false;
            }
        }

        return success;
    }


    @Override
    public boolean load() {
        boolean success = true;
        URLClassLoader classLoader = (URLClassLoader) DependencyUtils.class.getClassLoader();

        super.getDependencyPlugin().getLog().info(String.format("Attempting to Load dependency '%s'.", this.getName()));

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, this.getDownloadLocation().toUri().toURL());
        } catch (NoSuchMethodException | MalformedURLException | IllegalAccessException | InvocationTargetException e) {
            super.getDependencyPlugin().getLog().severe(e);
            success = false;
        }
        return success;
    }
}
