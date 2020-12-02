package com.heirteir.hac.util.dependency.types;

import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDependency {
    @Getter(AccessLevel.PROTECTED)
    private final DependencyPlugin dependencyPlugin;

    public abstract boolean needsUpdate();

    public abstract Path getDownloadLocation();

    public abstract URL getManualURL() throws MalformedURLException;

    public abstract URL getUrl() throws MalformedURLException;

    public abstract String getName();

    public abstract boolean download();

    public abstract boolean load();

    @Override
    public abstract String toString();
}
