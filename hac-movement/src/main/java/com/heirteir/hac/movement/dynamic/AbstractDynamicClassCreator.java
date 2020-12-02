package com.heirteir.hac.movement.dynamic;

import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.movement.Movement;
import javassist.ClassPool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDynamicClassCreator {
    @Getter(AccessLevel.PUBLIC)
    private final String name;
    private final Movement movement;
    private final ClassPool pool;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PROTECTED)
    private WrappedConstructor dynamic;

    public abstract void load();

    public abstract void unload();
}
