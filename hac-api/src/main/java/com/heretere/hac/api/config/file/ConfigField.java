package com.heretere.hac.api.config.file;

import com.heretere.hac.api.HACAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The type Config field.
 *
 * @param <T> the type parameter
 */
public class ConfigField<T> extends ConfigPath {
    /**
     * The class type of the config field.
     */
    private final Class<T> type;
    /**
     * A WeakReference to the instance that holds this value.
     */
    private final WeakReference<?> instance;
    /**
     * The getter to retrieve the value.
     */
    private Method getter;
    /**
     * The setter to set the value.
     */
    private Method setter;
    /**
     * If the WeakReference is null, this is the last known value that was retrieved.
     */
    private T lastKnownValue;

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
        @NotNull final HACAPI api,
        @NotNull final Class<T> type,
        @Nullable final Object instance,
        @NotNull final String path,
        @NotNull final String... comments
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
    public void setGetter(@NotNull final Method getter) {
        this.getter = getter;
        this.lastKnownValue = this.getValue();
    }

    /**
     * Sets setter.
     *
     * @param setter the setter
     */
    public void setSetter(@NotNull final Method setter) {
        this.setter = setter;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public T getValue() {
        T output;
        Object tmpInstance = this.instance.get();
        if (tmpInstance == null) {
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

        return output;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(@NotNull final T value) {
        this.lastKnownValue = value;
        Object tmpInstance = this.instance.get();
        if (tmpInstance != null) {
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
    public void setValueRaw(@NotNull final Object value) {
        this.setValue(this.type.cast(value));
    }

    /**
     * Gets class type.
     *
     * @return the class type
     */
    public Class<T> getClassType() {
        return type;
    }
}
