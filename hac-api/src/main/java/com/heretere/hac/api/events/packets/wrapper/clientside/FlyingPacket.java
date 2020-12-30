package com.heretere.hac.api.events.packets.wrapper.clientside;


import com.flowpowered.math.GenericMath;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacketIn;

/**
 * This is the wrapped version of the PacketPlayInFlyingPacket.
 */
public final class FlyingPacket implements WrappedPacketIn {
    private static final float ANGLE_WRAP = 360F;

    /**
     * The x location of the packet.
     */
    private final double x;
    /**
     * The y location of the packet.
     */
    private final double y;
    /**
     * The z location of the packet.
     */
    private final double z;
    /**
     * The yaw of the packet.
     */
    private final double yaw;
    /**
     * The pitch of the packet.
     */
    private final double pitch;

    /**
     * if that packet is on the ground.
     */
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
    public FlyingPacket(
        final double x,
        final double y,
        final double z,
        final double yaw,
        final double pitch,
        final boolean onGround
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw % ANGLE_WRAP;
        this.pitch = GenericMath.wrapAnglePitchDeg((float) pitch);
        this.onGround = onGround;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Gets yaw.
     *
     * @return the yaw
     */
    public double getYaw() {
        return this.yaw;
    }

    /**
     * Gets pitch.
     *
     * @return the pitch
     */
    public double getPitch() {
        return this.pitch;
    }

    /**
     * Whether or not the packet says the player is on the ground.
     *
     * @return the boolean
     */
    public boolean isOnGround() {
        return this.onGround;
    }
}

