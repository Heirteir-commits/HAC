package com.heretere.hac.util.dependency.types;

import com.heretere.hac.util.dependency.plugin.DependencyPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
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

    public boolean download() {
        boolean success = true;
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
            } catch (IOException e) {
                success = false;
            }
        }

        return success;
    }

    public abstract boolean load();

    @Override
    public abstract String toString();
}
