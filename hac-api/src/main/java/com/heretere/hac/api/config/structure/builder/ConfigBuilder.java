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

package com.heretere.hac.api.config.structure.builder;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.structure.backend.ConfigPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigBuilder {
    private final @NotNull HACAPI api;
    private @Nullable String relativePath;

    private final List<ConfigPath> paths;

    private ConfigBuilder(final @NotNull HACAPI api) {
        this.api = api;
        this.paths = Lists.newArrayList();
    }

    public @NotNull ConfigBuilder setRelativePath(final @NotNull String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    public @NotNull ConfigBuilder addConfigPath(final @NotNull ConfigPath path) {
        this.paths.add(path);
        return this;
    }

    public void build() {
        if (this.relativePath == null) {
            throw new IllegalStateException("Please define the relative path.");
        }

        this.api.getConfigHandler().loadConfigPaths(this.relativePath, this.paths);
    }

    public static @NotNull ConfigBuilder builder(final @NotNull HACAPI api) {
        return new ConfigBuilder(api);
    }
}
