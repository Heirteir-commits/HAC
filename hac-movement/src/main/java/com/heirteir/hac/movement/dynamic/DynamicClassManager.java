package com.heirteir.hac.movement.dynamic;

import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.human.HACHumanCreator;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import lombok.Getter;

public final class DynamicClassManager {
    private final Movement movement;
    @Getter
    private final HACHumanCreator humanCreator;

    public DynamicClassManager(Movement movement) {
        this.movement = movement;

        ClassPool pool = new ClassPool(false);
        pool.appendClassPath(new LoaderClassPath(DynamicClassManager.class.getClassLoader()));

        this.humanCreator = new HACHumanCreator(movement, pool);
    }

    public void load() {
        this.movement.getLog().info("Creating dynamic class HACHuman.");
        this.humanCreator.load();
    }

    public void unload() {
        this.movement.getLog().info("Detaching dynamic class HACHuman.");
        this.humanCreator.unload();
    }
}
