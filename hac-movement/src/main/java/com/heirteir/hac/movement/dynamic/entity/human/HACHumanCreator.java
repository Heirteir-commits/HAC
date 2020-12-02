package com.heirteir.hac.movement.dynamic.entity.human;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.helper.StringHelper;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.AbstractDynamicClassCreator;
import com.heirteir.hac.movement.util.mapping.builder.MappingBuilder;
import com.heirteir.hac.movement.util.mapping.builder.MappingUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public final class HACHumanCreator extends AbstractDynamicClassCreator {
    private static final String PATH = "com.heirteir.hac.movement.dynamic.entity.HACHuman";
    private CtClass human;

    public HACHumanCreator(Movement movement, ClassPool pool) {
        super("HACHuman", movement, pool);
    }

    private void createClass() {
        try {
            this.human = super.getPool().makeClass(HACHumanCreator.PATH);
            this.human.setSuperclass(super.getPool().get(API.INSTANCE.getReflections().getHelpers().getHelper(StringHelper.class).replaceString("%nms%.EntityHuman")));
            this.human.addInterface(super.getPool().get(EntityHumanAccessor.class.getName()));

            MappingBuilder builder = MappingUtil.mapFromInterface(super.getMovement(), super.getPool(), this.human, EntityHumanAccessor.class);

            for (CtMethod method : builder.build()) {
                this.human.addMethod(method);
            }

            super.setDynamic(API.INSTANCE.getReflections().getClass(this.human.toClass(EntityHumanAccessor.class.getClassLoader(), EntityHumanAccessor.class.getProtectionDomain())).getConstructorAtIndex(0));
        } catch (Exception e) {
            super.getMovement().getLog().reportFatalError(e);
        }
    }

    @Override
    public void load() {
        this.createClass();
    }

    @Override
    public void unload() {
        if (this.human != null) {
            this.human.detach();
        }
    }
}
