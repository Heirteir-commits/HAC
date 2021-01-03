package com.heretere.hac.api.config.annotations.backend;

import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Optional;

/**
 * The type Config field.
 *
 * @param <T> the type parameter
 */
public final class ConfigField<T> extends ConfigPath {
    /**
     * The class type of the config field.
     */
    private final @NotNull Class<T> type;
    /**
     * A WeakReference to the instance that holds this value.
     */
    private final @NotNull Reference<?> instance;
    /**
     * The field reference.
     */
    private @NotNull Reference<Field> field;
    /**
     * If the WeakReference is null, this is the last known value that was retrieved.
     */
    private @Nullable T lastKnownValue;

    /**
     * Instantiates a new Config field.
     *
     * @param api      the api
     * @param type     the type
     * @param instance the instance
     * @param path     the path
     * @param comments the comments
     */
    public ConfigField(
        final @NotNull HACAPI api,
        final @NotNull Class<T> type,
        final @Nullable Object instance,
        final @NotNull String path,
        final @NotNull String... comments
    ) {
        super(api, Type.VALUE, path, comments);
        this.type = type;
        this.instance = new WeakReference<>(instance);
        this.field = new WeakReference<>(null);
    }

    /**
     * @param field The field value attached to this instance.
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
                boolean accessible = ConfigField.canAccess(tmpInstance, tmpField);
                if (!accessible) {
                    ConfigField.changeAccessibility(tmpField, true);
                }
                this.lastKnownValue = this.type.cast(tmpField.get(tmpField));
                output = this.lastKnownValue;
                if (!accessible) {
                    ConfigField.changeAccessibility(tmpField, false);
                }
            } catch (IllegalAccessException e) {
                output = null;
                super.getAPI().getErrorHandler().getHandler().accept(e);
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
                boolean accessible = ConfigField.canAccess(tmpInstance, tmpField);
                if (!accessible) {
                    ConfigField.changeAccessibility(tmpField, true);
                }
                tmpField.set(tmpField, value);
                if (!accessible) {
                    ConfigField.changeAccessibility(tmpField, false);
                }
            } catch (IllegalAccessException e) {
                super.getAPI().getErrorHandler().getHandler().accept(e);
            }
        }
    }

    /**
     * Sets value raw.
     *
     * @param value the value
     */
    public void setValueRaw(final @NotNull Object value) {
        this.setValue(this.type.cast(value));
    }

    /**
     * Gets class type.
     *
     * @return the class type
     */
    public @NotNull Class<T> getClassType() {
        return this.type;
    }

    private static void changeAccessibility(
        final @NotNull Field field,
        final boolean flag
    ) {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            field.setAccessible(flag);
            return null;
        });
    }

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
}
