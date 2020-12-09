package com.heretere.hac.api.events.packets.wrapper.serverside;

import com.heretere.hac.api.events.packets.wrapper.AbstractWrappedPacketOut;

/**
 * A Wrapped version of the PacketPlayOutEntityVelocityPacket
 */
public final class EntityVelocityPacket extends AbstractWrappedPacketOut {
    /**
     * The constant CONVERSION. Used to convert the incoming values usually it will be (input / CONVERSION).
     */
    public static final double CONVERSION = 8000D;

    private final double x;
    private final double y;
    private final double z;

    /**
     * Instantiates a new Entity velocity packet.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public EntityVelocityPacket(int entityId, double x, double y, double z) {
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
}
