package com.heirteir.hac.movement.dynamic;

import com.google.common.collect.ImmutableMap;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.human.HACHumanCreator;
import javassist.ClassPool;
import javassist.LoaderClassPath;

public final class DynamicClassManager {
    private final Movement movement;
    private final ImmutableMap<Class<? extends AbstractDynamicClassCreator>, ? extends AbstractDynamicClassCreator> creators;

    public DynamicClassManager(Movement movement) {
        this.movement = movement;

        ClassPool pool = new ClassPool(false);
        pool.appendClassPath(new LoaderClassPath(DynamicClassManager.class.getClassLoader()));

        this.creators = ImmutableMap.of(
                HACHumanCreator.class, new HACHumanCreator(movement, pool)
        );
    }

    public <T extends AbstractDynamicClassCreator> T getDynamicClass(Class<T> clazz) {
        return clazz.cast(this.creators.get(clazz));
    }

    public void load() {
        this.creators.values().forEach(creator -> {
            this.movement.getLog().info(String.format("Attaching dynamic class '%s'.", creator.getName()));
            creator.load();
        });
    }

    public void unload() {
        this.creators.values().forEach(creator -> {
            this.movement.getLog().info(String.format("Detaching dynamic class '%s'.", creator.getName()));
            creator.unload();
        });
    }
}
