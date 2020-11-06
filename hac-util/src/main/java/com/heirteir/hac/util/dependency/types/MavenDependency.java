package com.heirteir.hac.util.dependency.types;

import com.heirteir.hac.util.dependency.DependencyUtils;
import com.heirteir.hac.util.dependency.types.annotation.Maven;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.Log;
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

    public MavenDependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull String repoUrl) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repoUrl = repoUrl.endsWith("/") ? repoUrl : repoUrl + "/";
    }

    public MavenDependency(@NotNull Maven maven) {
        this(maven.groupId(), maven.artifactId(), maven.version(), maven.repoUrl());
    }


    @Override
    public boolean needsUpdate() {
        return !Files.exists(this.getDownloadLocation());
    }

    @Override
    public Path getDownloadLocation() {
        return FilePaths.INSTANCE.getPluginFolder().resolve("dependencies").resolve(this.getName() + ".jar");
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
        Log.INSTANCE.info(String.format("Dependency '%s' is not in the library folder '%s'. Downloading now...", this.getName(), this.getDownloadLocation().getParent().toAbsolutePath()));

        try {
            URL url = this.getUrl();

            Files.createDirectories(this.getDownloadLocation().getParent());
            InputStream is = url.openStream();
            Files.copy(is, this.getDownloadLocation());
            Log.INSTANCE.info(String.format("Dependency '%s' successfully downloaded.", this.getName()));
        } catch (IOException e) {
            success = false;
        }
        return success;
    }


    @Override
    public boolean load() {
        boolean success = true;
        URLClassLoader classLoader = (URLClassLoader) DependencyUtils.class.getClassLoader();

        Log.INSTANCE.info(String.format("Attempting to Load dependency '%s'.", this.getName()));

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, this.getDownloadLocation().toUri().toURL());
        } catch (NoSuchMethodException | MalformedURLException | IllegalAccessException | InvocationTargetException e) {
            Log.INSTANCE.severe(e);
            success = false;
        }
        return success;
    }
}
