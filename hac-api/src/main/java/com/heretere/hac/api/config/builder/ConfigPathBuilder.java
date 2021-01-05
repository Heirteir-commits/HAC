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

package com.heretere.hac.api.config.builder;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.backendv2.ConfigPath;
import com.heretere.hac.api.config.backendv2.ConfigSection;
import com.heretere.hac.api.config.backendv2.SuppliedConfigField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigPathBuilder<T> {
    private final @NotNull Class<T> type;
    private final @NotNull Type pathType;
    private @Nullable String key;
    private @Nullable Supplier<@NotNull T> supplier;
    private @Nullable Consumer<@NotNull T> consumer;
    private @Nullable T defaultValue;
    private final @NotNull List<@NotNull String> comments;

    private ConfigPathBuilder(
        final @NotNull Class<T> type,
        final @NotNull Type pathType
    ) {
        this.type = type;
        this.pathType = pathType;
        this.comments = Lists.newArrayList();
    }

    public @NotNull ConfigPathBuilder<T> setKey(final @NotNull String key) {
        this.key = key;
        return this;
    }

    public @NotNull ConfigPathBuilder<T> setGetterSupplier(final @NotNull Supplier<@NotNull T> supplier) {
        this.supplier = supplier;
        return this;
    }

    public @NotNull ConfigPathBuilder<T> setSetterConsumer(final @NotNull Consumer<@NotNull T> consumer) {
        this.consumer = consumer;
        return this;
    }

    public @NotNull ConfigPathBuilder<T> setDefaultValue(final @NotNull T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public @NotNull ConfigPathBuilder<T> addComment(final @NotNull String comment) {
        this.comments.add(comment);
        return this;
    }

    public ConfigPath build() {
        Objects.requireNonNull(this.key, "Define key for config path.");

        if (this.pathType == Type.SECTION) {
            return new ConfigSection(this.key, this.comments);
        } else {
            Objects.requireNonNull(this.supplier, "Define supplier for config path.");
            Objects.requireNonNull(this.consumer, "Define consumer for config path.");

            SuppliedConfigField<T> path = new SuppliedConfigField<>(
                this.key,
                this.comments,
                this.type,
                this.supplier,
                this.consumer
            );

            path.setValue(this.defaultValue == null ? this.supplier.get() : this.defaultValue);
            return path;
        }
    }

    public enum Type {
        SECTION,
        FIELD
    }

    public static @NotNull <T> ConfigPathBuilder<T> builder(
        final @NotNull Class<T> type,
        final @NotNull Type pathType
    ) {
        return new ConfigPathBuilder<>(type, pathType);
    }
}
