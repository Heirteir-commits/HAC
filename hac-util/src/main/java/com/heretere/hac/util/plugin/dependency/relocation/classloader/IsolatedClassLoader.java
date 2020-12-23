package com.heretere.hac.util.plugin.dependency.relocation.classloader;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;

/**
 * This class handles loading some dependencies that are needed for loading other dependencies, but not
 * need afterwards.
 */
public final class IsolatedClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    /**
     * Instantiates a new Isolated class loader.
     *
     * @param urls the urls
     */
    public IsolatedClassLoader(@NotNull final URL... urls) {
        super(Objects.requireNonNull(urls), ClassLoader.getSystemClassLoader().getParent());
    }

    @Override
    public void addURL(@NotNull final URL url) {
        super.addURL(url);
    }

    /**
     * Add path boolean.
     *
     * @param path the path
     * @return the boolean
     */
    public boolean addPath(@NotNull final Path path) {
        try {
            this.addURL(path.toUri().toURL());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
