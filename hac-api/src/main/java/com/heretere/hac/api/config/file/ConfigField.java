package com.heretere.hac.api.config.file;

import com.heretere.hac.api.HACAPI;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigField extends ConfigPath {
    private final WeakReference<?> instance;
    private Method getter;
    private Method setter;
    private Object lastKnownValue;

    public ConfigField(Object instance, String path, String... comments) {
        super(Type.VALUE, path, comments);
        this.instance = new WeakReference<>(instance);
    }


    public void setGetter(Method getter) {
        this.getter = getter;
        this.lastKnownValue = this.getValue();
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Object getValue() {
        Object output;
        if (this.instance.get() == null) {
            output = this.lastKnownValue;
        } else {
            try {
                output = this.lastKnownValue = this.getter.invoke(this.instance.get());
            } catch (IllegalAccessException | InvocationTargetException e) {
                output = null;
                HACAPI.getInstance().getErrorHandler().getHandler().accept(e);
            }
        }

        return output;
    }

    public void setValue(Object value) {
        if (this.instance.get() == null) {
            this.lastKnownValue = value;
        } else {
            try {
                this.setter.invoke(this.instance.get(), value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                HACAPI.getInstance().getErrorHandler().getHandler().accept(e);
            }
        }
    }

}
