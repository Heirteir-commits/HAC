package com.heretere.hac.api.config.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

public abstract class ConfigPath {
    private final Type type;
    private final String path;
    private final Set<String> comments;
    private final int indentLevel;

    protected ConfigPath(Type type, String path, String... comments) {
        this.type = type;
        this.path = path;
        this.comments = Sets.newLinkedHashSet(Lists.newArrayList(comments));
        indentLevel = StringUtils.countMatches(path, ".") * 2;
    }

    public String getPath() {
        return path;
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }

    public Set<String> getComments() {
        return this.comments;
    }

    public Type getType() {
        return type;
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public enum Type {
        SECTION,
        VALUE
    }
}
