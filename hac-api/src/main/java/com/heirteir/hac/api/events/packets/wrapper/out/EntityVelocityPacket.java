package com.heirteir.hac.api.events.packets.wrapper.out;

import com.heirteir.hac.api.events.packets.PacketConstants;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketOut;
import lombok.Getter;

//PacketPlayOutEntityVelocity
@Getter
public final class EntityVelocityPacket extends AbstractWrappedPacketOut {
    public static final EntityVelocityPacket DEFAULT;

    static {
        DEFAULT = new EntityVelocityPacket();
        DEFAULT.x = 0;
        DEFAULT.y = 0;
        DEFAULT.z = 0;
    }

    public static final double CONVERSION = 8000D;
    private double x;
    private double y;
    private double z;

    public EntityVelocityPacket() {
        super(PacketConstants.Out.ENTITY_VELOCITY);
    }
}
