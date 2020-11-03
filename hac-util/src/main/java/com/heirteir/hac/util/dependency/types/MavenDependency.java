package com.heirteir.hac.util.dependency.types;

import com.heirteir.hac.util.dependency.types.annotation.Maven;
import com.heirteir.hac.util.logging.Log;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

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
    public URL getUrl() {
        URL url;
        try {
            url = new URL(String.format(this.repoUrl + "%s/%s/%s/%s-%s.jar", this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version));
        } catch (MalformedURLException e) {
            url = null;
            Log.INSTANCE.severe(e);
        }
        return url;
    }

    @Override
    public String getName() {
        return this.artifactId + "-" + this.version;
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s from %s", this.groupId, this.artifactId, this.version, this.repoUrl);
    }
}
