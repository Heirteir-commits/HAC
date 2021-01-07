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

package com.heretere.hac.api.config.structure.backend;

import com.google.common.base.Preconditions;
import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Optional;

/**
 * This class is used for annotation side of the config package.
 * This is used to assign values directly to the field based on the config value.
 *
 * @param <T> The type of this config field.
 */
public final class ReflectiveConfigField<T> extends ConfigSection implements ConfigField<T> {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;
    /**
     * The generic type reference of this config field.
     * Used for casting in a type safe way.
     */
    private final @NotNull Class<T> type;
    /**
     * A reference to any instance that this config field may be attached to.
     */
    private final @NotNull Reference<@Nullable ?> instance;
    /**
     * A reference to any field that this config field may be attached to.
     */
    private @NotNull Reference<@Nullable Field> field;
    /**
     * Used in case the reference is null, this allows us to pass a value if for some
     * reason the instance has been garbage collected and we can no longer retrieve the value from the field.
     */
    private @Nullable T lastKnownValue;

    /**
     * @param api      The HACAPI reference.
     * @param key      The key to this config field.
     * @param comments The comments attached to this config field.
     * @param type     The generic type of this config field.
     * @param instance The instance attached to this config field.
     */
    public ReflectiveConfigField(
        final @NotNull HACAPI api,
        final @NotNull String key,
        final @NotNull List<@NotNull String> comments,
        final @NotNull Class<T> type,
        final @Nullable Object instance
    ) {
        super(key, comments);

        /* Primitives are not supported. */
        Preconditions.checkState(!type.isPrimitive(), "Primitive types are not allowed. Invalid Key (%s).", key);

        this.api = api;
        this.type = type;
        this.instance = new WeakReference<>(instance);
        this.field = new WeakReference<>(null);
    }

    /**
     * Changes the accessibility of the field so it can be edited.
     *
     * @param field The field to change the accessibility for.
     * @param flag  What to set it's accessibility to.
     */
    private static void changeAccessibility(
        final @NotNull Field field,
        final boolean flag
    ) {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            field.setAccessible(flag);
            return null;
        });
    }

    /**
     * @param instance The instance attached to this field.
     * @param field    The field to check accessibility for.
     * @return true if the field can be accessed.
     */
    private static boolean canAccess(
        final @NotNull Object instance,
        final @NotNull Field field
    ) {
        boolean accessible;
        try {
            accessible = field.get(instance) != null;
        } catch (IllegalAccessException e) {
            accessible = false;
        }

        return accessible;
    }

    /**
     * Assign a field to this config field.
     *
     * @param field The field that should be attached to this config field.
     */
    public void setField(final @NotNull Field field) {
        this.field = new WeakReference<>(field);
    }

    /**
     * Gets value of config field. If getter is present it will delegate to the getter.
     *
     * @return the value
     */
    public @NotNull Optional<T> getValue() {
        T output;
        Object tmpInstance = this.instance.get();
        Field tmpField = this.field.get();
        if (tmpInstance == null || tmpField == null) {
            output = this.lastKnownValue;
        } else {
            try {
                boolean accessible = ReflectiveConfigField.canAccess(tmpInstance, tmpField);
                if (!accessible) {
                    ReflectiveConfigField.changeAccessibility(tmpField, true);
                }
                this.lastKnownValue = this.type.cast(tmpField.get(tmpInstance));
                output = this.lastKnownValue;
                if (!accessible) {
                    ReflectiveConfigField.changeAccessibility(tmpField, false);
                }
            } catch (IllegalAccessException e) {
                output = null;
                this.api.getErrorHandler().getHandler().accept(e);
            }
        }

        return Optional.ofNullable(output);
    }

    /**
     * Set value of config field. If setter is present it will delegate to the setter.
     *
     * @param value the value
     */
    public void setValue(final @NotNull T value) {
        this.lastKnownValue = value;
        Object tmpInstance = this.instance.get();
        Field tmpField = this.field.get();
        if (tmpInstance != null && tmpField != null) {
            try {
                boolean accessible = ReflectiveConfigField.canAccess(tmpInstance, tmpField);
                if (!accessible) {
                    ReflectiveConfigField.changeAccessibility(tmpField, true);
                }
                tmpField.set(tmpInstance, value);
                if (!accessible) {
                    ReflectiveConfigField.changeAccessibility(tmpField, false);
                }
            } catch (IllegalAccessException e) {
                this.api.getErrorHandler().getHandler().accept(e);
            }
        }
    }

    /**
     * Sets value raw.
     *
     * @param value the value
     */
    public void setValueRaw(final @NotNull Object value) {
        this.setValue(this.getGenericType().cast(value));
    }

    @Override public @NotNull Class<T> getGenericType() {
        return this.type;
    }
}
