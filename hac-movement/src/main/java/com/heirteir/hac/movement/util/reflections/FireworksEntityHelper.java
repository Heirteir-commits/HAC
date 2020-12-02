package com.heirteir.hac.movement.util.reflections;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.types.WrappedField;
import com.heirteir.hac.api.util.reflections.types.WrappedMethod;
import com.heirteir.hac.movement.Movement;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.InvocationTargetException;

public class FireworksEntityHelper {

    private final Movement movement;

    private WrappedField entityLiving;
    private WrappedMethod getBukkitEntity;

    public FireworksEntityHelper(Movement movement) {
        this.movement = movement;

        try {
            this.entityLiving = API.INSTANCE.getReflections().getNMSClass("EntityFireworks")
                    .getFieldByType(API.INSTANCE.getReflections().getNMSClass("EntityLiving").getRaw(), 0);
            this.getBukkitEntity = API.INSTANCE.getReflections().getNMSClass("Entity")
                    .getMethod("getBukkitEntity");
        } catch (NoSuchMethodException e) {
            this.movement.getLog().reportFatalError(e);
        }
    }

    public LivingEntity getLivingEntity(Object rocketEntity) {
        LivingEntity entity;
        try {
            entity = this.getBukkitEntity.invoke(LivingEntity.class, this.entityLiving.get(Object.class, rocketEntity));
        } catch (InvocationTargetException | IllegalAccessException e) {
            entity = null;
            this.movement.getLog().reportFatalError(e);
        }
        return entity;
    }

}
