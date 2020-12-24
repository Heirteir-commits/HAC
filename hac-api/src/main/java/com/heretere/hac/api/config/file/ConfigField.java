package com.heretere.hac.api.config.file;

import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
     * The getter to retrieve the value.
     */
    private @Nullable Method getter;
    /**
     * The setter to set the value.
     */
    private @Nullable Method setter;
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
    }

    /**
     * Sets the getter.
     *
     * @param getter the getter
     */
    public void setGetter(final @NotNull Method getter) {
        this.getter = getter;
        this.lastKnownValue = this.getValue().orElse(null);
    }

    /**
     * Sets setter.
     *
     * @param setter the setter
     */
    public void setSetter(final @NotNull Method setter) {
        this.setter = setter;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public @NotNull Optional<T> getValue() {
        T output;
        Object tmpInstance = this.instance.get();
        if (tmpInstance == null || this.getter == null) {
            output = this.lastKnownValue;
        } else {
            try {
                this.lastKnownValue = this.type.cast(this.getter.invoke(tmpInstance));
                output = this.lastKnownValue;
            } catch (IllegalAccessException | InvocationTargetException e) {
                output = null;
                super.getAPI().getErrorHandler().getHandler().accept(e);
            }
        }

        return Optional.ofNullable(output);
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(final @NotNull T value) {
        this.lastKnownValue = value;
        Object tmpInstance = this.instance.get();
        if (tmpInstance != null && this.setter != null) {
            try {
                this.setter.invoke(tmpInstance, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
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
}
