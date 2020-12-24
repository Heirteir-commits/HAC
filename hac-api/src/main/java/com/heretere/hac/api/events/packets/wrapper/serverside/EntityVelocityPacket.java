package com.heretere.hac.api.events.packets.wrapper.serverside;

import com.heretere.hac.api.events.packets.wrapper.WrappedPacketOut;

/**
 * A Wrapped version of the PacketPlayOutEntityVelocityPacket.
 */
public final class EntityVelocityPacket extends WrappedPacketOut {
    /**
     * The constant CONVERSION. Used to convert the incoming values usually it will be (input / CONVERSION).
     */
    public static final double CONVERSION = 8000D;

    /**
     * The dx of this velocity packet.
     */
    private final double x;
    /**
     * the dy of this velocity packet.
     */
    private final double y;
    /**
     * The dz of this velocity packet.
     */
    private final double z;

    /**
     * Instantiates a new Entity velocity packet.
     *
     * @param entityId the entityId
     * @param x        the x
     * @param y        the y
     * @param z        the z
     */
    public EntityVelocityPacket(
        final int entityId,
        final double x,
        final double y,
        final double z
    ) {
        super(entityId);
        this.x = x;
        this.y = y;
        this.z = z;
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
}
