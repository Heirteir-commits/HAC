package com.heirteir.hac.core.util.reflections.helper;

import com.heirteir.hac.api.API;
import com.heirteir.hac.core.Core;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractCoreHelper<T> {
    @Getter(AccessLevel.PROTECTED)
    private final Core core;

    private final Class<T> baseClass;

    protected AbstractCoreHelper(Core core, Class<T> baseClass) {
        this.core = core;
        this.baseClass = baseClass;
    }

    public final void load() {
        this.core.getLog().info(String.format("Registering Reflections Helper '%s'.", this.baseClass.getSimpleName()));
        API.INSTANCE.getReflections().getHelpers().registerHelper(this.baseClass, this.baseClass.cast(this));
    }

    public final void unload() {
        this.core.getLog().info(String.format("Unregistering Reflections Helper '%s'.", this.baseClass.getSimpleName()));
        API.INSTANCE.getReflections().getHelpers().unregisterHelper(this.baseClass);
    }
}
