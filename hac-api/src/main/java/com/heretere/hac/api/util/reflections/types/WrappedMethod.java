package com.heretere.hac.api.util.reflections.types;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
public final class WrappedMethod {

    private final WrappedClass parent;
    private final Method raw;

    WrappedMethod(WrappedClass parent, Method raw) {
        this.parent = parent;
        this.raw = raw;
        this.raw.setAccessible(true);
    }

    /**
     * Method used to invoke method from instance object.
     *
     * @param instance object instance to invoke method in
     * @param args     arguments for invoked method
     * @param <T>      expected return type
     * @return value returned from invoked method
     */
    public <T> T invoke(@NotNull Class<T> clazz, @Nullable Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return clazz.cast(this.raw.invoke(instance, args));
    }
}
