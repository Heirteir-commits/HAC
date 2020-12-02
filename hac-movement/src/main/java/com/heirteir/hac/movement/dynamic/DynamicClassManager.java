package com.heirteir.hac.movement.dynamic;

import com.google.common.collect.ImmutableMap;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.firework.FireworksEntityCreator;
import com.heirteir.hac.movement.dynamic.entity.human.HACHumanCreator;

public final class DynamicClassManager {
    private final Movement movement;

    private final ImmutableMap<Class<? extends AbstractDynamicClassCreator>, ? extends AbstractDynamicClassCreator> creators;

    public DynamicClassManager(Movement movement) {
        this.movement = movement;

        this.movement.getLog().info("Installing implementation agent.");

        this.creators = ImmutableMap.of(
                HACHumanCreator.class, new HACHumanCreator(movement),
                FireworksEntityCreator.class, new FireworksEntityCreator(movement)
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
