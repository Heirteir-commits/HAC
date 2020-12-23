package com.heretere.hac.movement.proxy.simulation;

import com.heretere.hac.core.util.math.vector.MutableVector3F;
import org.bukkit.World;

public class SimulationData {
    private World world;
    private MutableVector3F location;
    private MutableVector3F velocity;
    private MutableVector3F motion;

    private boolean onGround;
    private boolean sprinting;
    private boolean sneaking;
    private boolean flying;
    private boolean elytraFlying;

    private float fallDistance;
    private float jumpSpeedFactor;
}
