package com.heirteir.hac.api.util.reflections.types;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Getter
public final class WrappedConstructor {
    private final WrappedClass parent;
    private final Constructor<?> raw;

    WrappedConstructor(WrappedClass parent, Constructor<?> raw) {
        this.parent = parent;
        this.raw = raw;
        this.raw.setAccessible(true);
    }

    /**
     * Create new instance of an object.
     *
     * @param arguments The arguments to pass to the new instance
     * @param <T>       Expected type of constructed value
     * @return New Instance of object
     */
    public <T> T newInstance(@NotNull Class<T> clazz, Object... arguments) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.cast(this.raw.newInstance(arguments));
    }
}
