/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
    public IsolatedClassLoader(final @NotNull URL... urls) {
        super(Objects.requireNonNull(urls), ClassLoader.getSystemClassLoader().getParent());
    }

    @Override
    public void addURL(final @NotNull URL url) {
        super.addURL(url);
    }

    /**
     * Add path boolean.
     *
     * @param path the path
     * @return the boolean
     */
    public boolean addPath(final @NotNull Path path) {
        try {
            this.addURL(path.toUri().toURL());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
