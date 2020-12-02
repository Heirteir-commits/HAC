package com.heretere.hac.api.events.types.packets.wrapper.clientside;


import com.heretere.hac.api.events.types.packets.wrapper.WrappedPacketIn;

/**
 * This is the wrapped version of the PacketPlayInFlyingPacket.
 */
public final class FlyingPacket implements WrappedPacketIn {
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private final double pitch;
    private final boolean onGround;

    /**
     * Instantiates a new Flying packet.
     *
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param yaw      the yaw
     * @param pitch    the pitch
     * @param onGround the on ground
     */
    public FlyingPacket(double x, double y, double z, double yaw, double pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
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
     * Whether or not the packet says the player is on the ground.
     *
     * @return the boolean
     */
    public boolean isOnGround() {
        return onGround;
    }
}

