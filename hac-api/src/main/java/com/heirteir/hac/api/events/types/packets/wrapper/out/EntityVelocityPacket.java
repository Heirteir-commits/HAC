package com.heirteir.hac.api.events.types.packets.wrapper.out;

import com.heirteir.hac.api.events.types.packets.PacketConstants;
import com.heirteir.hac.api.events.types.packets.wrapper.AbstractWrappedPacketOut;
import lombok.Getter;

//PacketPlayOutEntityVelocity
@Getter
public final class EntityVelocityPacket extends AbstractWrappedPacketOut {
    public static final EntityVelocityPacket DEFAULT;
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

    public EntityVelocityPacket() {
        super(PacketConstants.Out.ENTITY_VELOCITY);
    }
}
