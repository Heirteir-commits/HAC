package com.heirteir.hac.movement.dynamic.entity.firework;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.events.types.FireworkPropelEvent;
import com.heirteir.hac.core.util.reflections.helper.EntityHelper;
import com.heirteir.hac.movement.util.reflections.FireworksEntityHelper;
import net.bytebuddy.implementation.bind.annotation.This;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class EntityFireworksInterceptor {

    public void advisorTick(@This Object rocket) {
        LivingEntity entity = API.INSTANCE.getReflections().getHelpers().getHelper(FireworksEntityHelper.class)
                .getLivingEntity(rocket);

        if (entity instanceof Player) {
            boolean gliding = API.INSTANCE.getReflections().getHelpers().getHelper(EntityHelper.class).isElytraFlying((Player) entity);
            FireworkPropelEvent event = new FireworkPropelEvent((Player) entity);

            if (gliding) {
                API.INSTANCE.getEventManager().callEvent(event);
            }
        }
    }

}
