package com.heretere.hac.movement.proxy.versions.sixteen.simulation;

import com.heretere.hac.movement.proxy.simulation.HumanAccessor;
import com.heretere.hac.movement.proxy.simulation.HumanAccessorFactory;
import org.bukkit.World;

public class HACEntityHumanFactory implements HumanAccessorFactory {
    @Override public HumanAccessor createHumanAccessor(World world) {
        return new HACEntityHuman(world);
    }
}
