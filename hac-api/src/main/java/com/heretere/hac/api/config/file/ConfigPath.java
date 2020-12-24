package com.heretere.hac.api.config.file;

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
