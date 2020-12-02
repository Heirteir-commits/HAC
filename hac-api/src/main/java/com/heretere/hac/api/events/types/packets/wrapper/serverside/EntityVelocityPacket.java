package com.heretere.hac.api.events.types.packets.wrapper.serverside;

import com.heretere.hac.api.events.types.packets.PacketConstants;
import com.heretere.hac.api.events.types.packets.wrapper.AbstractWrappedPacketOut;

/**
 * A Wrapped version of the PacketPlayOutEntityVelocityPacket
 */
public final class EntityVelocityPacket extends AbstractWrappedPacketOut {
    /**
     * The constant DEFAULT. Mainly used for testing.
     */
    public static final EntityVelocityPacket DEFAULT;
    /**
     * The constant CONVERSION. Used to convert the incoming values usually it will be (input / CONVERSION).
     */
    public static final double CONVERSION = 8000D;

    static {
        DEFAULT = new EntityVelocityPacket();
        DEFAULT.x = 0;
        DEFAULT.y = 0;
        DEFAULT.z = 0;
    }

    private double x;
    private double y;
    private double z;

    /**
     * Instantiates a new Entity velocity packet.
     */
    public EntityVelocityPacket() {
        super(PacketConstants.Out.ENTITY_VELOCITY);
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
