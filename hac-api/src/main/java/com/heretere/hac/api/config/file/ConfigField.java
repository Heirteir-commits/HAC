package com.heretere.hac.api.config.file;

import com.heretere.hac.api.HACAPI;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigField<T> extends ConfigPath {
    private final Class<T> type;
    private final WeakReference<?> instance;
    private Method getter;
    private Method setter;
    private T lastKnownValue;

    public ConfigField(Class<T> type, Object instance, String path, String... comments) {
        super(Type.VALUE, path, comments);
        this.type = type;
        this.instance = new WeakReference<>(instance);
    }

    public void setGetter(Method getter) {
        this.getter = getter;
        this.lastKnownValue = this.getValue();
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public T getValue() {
        T output;
        Object tmpInstance = this.instance.get();
        if (tmpInstance == null) {
            output = this.lastKnownValue;
        } else {
            try {
                output = this.lastKnownValue = this.type.cast(this.getter.invoke(tmpInstance));
            } catch (IllegalAccessException | InvocationTargetException e) {
                output = null;
                HACAPI.getInstance().getErrorHandler().getHandler().accept(e);
            }
        }

        return output;
    }

    public void setValue(T value) {
        this.lastKnownValue = value;
        Object tmpInstance = this.instance.get();
        if (tmpInstance != null) {
            try {
                this.setter.invoke(tmpInstance, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                HACAPI.getInstance().getErrorHandler().getHandler().accept(e);
            }
        }
    }

    public void setValueRaw(Object value) {
        this.setValue(this.type.cast(value));
    }
}
