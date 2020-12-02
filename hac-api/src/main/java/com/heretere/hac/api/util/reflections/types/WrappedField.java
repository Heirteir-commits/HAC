package com.heretere.hac.api.util.reflections.types;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Getter
public final class WrappedField {

    private final WrappedClass parent;
    private final Field raw;
    private boolean removedFinal = false;

    WrappedField(WrappedClass parent, Field raw) {
        this.parent = parent;
        this.raw = raw;
        this.raw.setAccessible(true);
    }

    /**
     * Get value of field from instance object.
     *
     * @param instance Object instance to get value from
     * @param <T>      type to convert to
     * @return value of field
     */
    public <T> T get(@NotNull Class<T> clazz, @Nullable Object instance) throws IllegalAccessException {
        return clazz.cast(this.raw.get(instance));
    }

    /**
     * Set the value of field inside of instance object.
     *
     * @param instance Object instance to change the field value in
     * @param value    new Value for specified Field
     */
    public void set(@Nullable Object instance, Object value) throws IllegalAccessException, NoSuchFieldException {
        if (!this.removedFinal) {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(this.raw, (this.raw.getModifiers() & ~Modifier.FINAL));
            this.removedFinal = true;
        }

        this.raw.set(instance, value);
    }
}
