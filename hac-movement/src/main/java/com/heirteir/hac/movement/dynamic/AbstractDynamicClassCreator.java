package com.heirteir.hac.movement.dynamic;

import com.heirteir.hac.movement.Movement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDynamicClassCreator {
    @Getter(AccessLevel.PUBLIC)
    private final String name;
    private final Movement movement;

    public abstract void load();

    public abstract void unload();
}
