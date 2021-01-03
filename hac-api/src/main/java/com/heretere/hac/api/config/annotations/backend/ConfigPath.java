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

package com.heretere.hac.api.config.annotations.backend;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The type Config path.
 */
public abstract class ConfigPath {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The {@link Type} of this Config Path.
     */
    private final @NotNull Type type;
    /**
     * The path (or key) to this path in a yaml file.
     */
    private final @NotNull String path;
    /**
     * All the comments attached to this path.
     */
    private final @NotNull List<String> comments;

    /**
     * Instantiates a new Config path.
     *
     * @param api      the api
     * @param type     the type
     * @param path     the path
     * @param comments the comments
     */
    protected ConfigPath(
        final @NotNull HACAPI api,
        final @NotNull Type type,
        final @NotNull String path,
        final @NotNull String... comments
    ) {
        this.api = api;
        this.type = type;
        this.path = path;
        this.comments = Lists.newArrayList(comments);
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public @NotNull String getPath() {
        return this.path;
    }

    /**
     * Add comment.
     *
     * @param comment the comment
     */
    public void addComment(final @NotNull String comment) {
        this.comments.add(comment);
    }

    /**
     * Gets comments.
     *
     * @return the comments
     */
    public @NotNull List<String> getComments() {
        return this.comments;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public @NotNull Type getType() {
        return this.type;
    }

    /**
     * Gets api.
     *
     * @return the api
     */
    protected @NotNull HACAPI getAPI() {
        return this.api;
    }

    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * Section type.
         */
        SECTION,
        /**
         * Value type.
         */
        VALUE
    }
}
