package com.heretere.hac.movement.proxy.simulation;

import org.bukkit.World;

public interface HumanAccessorFactory {
    HumanAccessor createHumanAccessor(World world);
}
