package com.heretere.hac.api.events.types.packets.wrapper.clientside;


import com.heretere.hac.api.events.types.packets.PacketConstants;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketIn;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the wrapped version of the PacketPlayInFlyingPacket.
 */
public abstract class FlyingPacket extends AbstractWrappedPacketIn {
    protected static final double UNIQUE_DOUBLE = ThreadLocalRandom.current().nextDouble(600, 100000);
    protected static final float UNIQUE_FLOAT = ThreadLocalRandom.current().nextFloat();

    protected double x;
    protected double y;
    protected double z;
    protected double yaw;
    protected double pitch;
    protected boolean hasLook;
    protected boolean hasPos;
    protected boolean onGround;

    /**
     * Instantiates a new Flying packet.
     */
    public FlyingPacket() {
        super(PacketConstants.In.FLYING);
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * Gets yaw.
     *
     * @return the yaw
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Gets pitch.
     *
     * @return the pitch
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Whether or not the packet contains look information.
     *
     * @return the boolean
     */
    public boolean isHasLook() {
        return hasLook;
    }

    /**
     * Whether or not the packet contains position information
     *
     * @return the boolean
     */
    public boolean isHasPos() {
        return hasPos;
    }

    /**
     * Whether or not the packet says the player is on the ground.
     *
     * @return the boolean
     */
    public boolean isOnGround() {
        return onGround;
    }
}

