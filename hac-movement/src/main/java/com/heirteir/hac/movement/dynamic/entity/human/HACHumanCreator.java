package com.heirteir.hac.movement.dynamic.entity.human;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.helper.StringHelper;
import com.heirteir.hac.movement.Movement;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HACHumanCreator {
    private static final String PATH = "com.heirteir.hac.movement.dynamic.entity.HACHuman";

    private final Movement movement;
    private final ClassPool pool;

    private CtClass human;

    private void createClass() {
        try {
            this.human = this.pool.makeClass(HACHumanCreator.PATH);
            this.human.setSuperclass(this.pool.get(API.INSTANCE.getReflections().getHelpers().getHelper(StringHelper.class).replaceString("%nms%.EntityHuman")));
            this.human.addInterface(this.pool.get(EntityHumanAccessor.class.getName()));

            new ClassPool();
        } catch (Exception e) {
            this.movement.getLog().reportFatalError(e);
        }
    }


    public void load() {
        this.createClass();
    }

    public void unload() {
        if (this.human != null) {
            this.human.detach();
        }
    }

}
