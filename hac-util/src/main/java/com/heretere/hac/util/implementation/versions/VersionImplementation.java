package com.heretere.hac.util.implementation.versions;

import com.google.common.collect.Maps;
import org.apache.commons.lang.NotImplementedException;

import java.util.Map;

public abstract class VersionImplementation {
    private final String version;
    private final Map<Class<?>, ? super Object> implementations;

    protected VersionImplementation(String version) {
        this.version = version;
        this.implementations = Maps.newHashMap();
    }

    protected <T> void registerImplementation(Class<T> clazz, T instance) {
        this.implementations.put(clazz, instance);
    }

    public <T> T getImplementation(Class<T> clazz) {
        Object implementation = this.implementations.get(clazz);

        if (implementation == null) {
            throw new NotImplementedException(String.format("No Implementation found for class '%s' version adapter v'%s'.", clazz.getSimpleName(), this.version));
        }

        return clazz.cast(implementation);
    }

    public String getVersion() {
        return version;
    }
}
