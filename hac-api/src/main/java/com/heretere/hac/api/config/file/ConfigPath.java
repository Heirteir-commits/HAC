package com.heretere.hac.api.config.file;

import com.google.common.collect.Lists;
import com.heretere.hac.api.HACAPI;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ConfigPath {
    private final HACAPI api;

    private final Type type;
    private final String path;
    private final List<String> comments;
    private final int indentLevel;

    protected ConfigPath(@NotNull HACAPI api, @NotNull Type type, @NotNull String path, @NotNull String... comments) {
        this.api = api;
        this.type = type;
        this.path = path;
        this.comments = Lists.newArrayList(comments);
        indentLevel = StringUtils.countMatches(path, ".") * 2;
    }

    public String getPath() {
        return path;
    }

    public void addComment(@NotNull String comment) {
        this.comments.add(comment);
    }

    public List<String> getComments() {
        return this.comments;
    }

    public Type getType() {
        return type;
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    protected HACAPI getAPI() {
        return this.api;
    }

    public enum Type {
        SECTION,
        VALUE
    }
}
