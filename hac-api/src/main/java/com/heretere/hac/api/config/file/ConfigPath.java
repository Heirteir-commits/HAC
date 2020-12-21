package com.heretere.hac.api.config.file;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The type Config path.
 */
public abstract class ConfigPath {
    /**
     * The HACAPI reference.
     */
    private final HACAPI api;

    /**
     * The {@link Type} of this Config Path.
     */
    private final Type type;
    /**
     * The path (or key) to this path in a yaml file.
     */
    private final String path;
    /**
     * All the comments attached to this path.
     */
    private final List<String> comments;
    /**
     * The indent levels of this path in a yaml file.
     */
    private final int indentLevel;

    /**
     * Instantiates a new Config path.
     *
     * @param api      the api
     * @param type     the type
     * @param path     the path
     * @param comments the comments
     */
    protected ConfigPath(@NotNull final HACAPI api,
                         @NotNull final Type type,
                         @NotNull final String path,
                         @NotNull final String... comments) {
        this.api = api;
        this.type = type;
        this.path = path;
        this.comments = Lists.newArrayList(comments);
        indentLevel = StringUtils.countMatches(path, ".") * 2;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Add comment.
     *
     * @param comment the comment
     */
    public void addComment(@NotNull final String comment) {
        this.comments.add(comment);
    }

    /**
     * Gets comments.
     *
     * @return the comments
     */
    public List<String> getComments() {
        return this.comments;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets indent level.
     *
     * @return the indent level
     */
    public int getIndentLevel() {
        return indentLevel;
    }

    /**
     * Gets api.
     *
     * @return the api
     */
    protected HACAPI getAPI() {
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
