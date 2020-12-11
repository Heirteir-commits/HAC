package com.heretere.hac.util.plugin.dependency.relocation.classloader;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class IsolatedClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public IsolatedClassLoader(@NotNull URL... urls) {
        super(urls, ClassLoader.getSystemClassLoader().getParent());
    }

    @Override
    public void addURL(@NotNull URL url) {
        super.addURL(url);
    }

    public boolean addPath(@NotNull Path path) {
        try {
            this.addURL(path.toUri().toURL());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
