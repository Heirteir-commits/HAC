package com.heirteir.hac.api.events.packets.wrapper.in;

import com.heirteir.hac.api.events.packets.PacketConstants;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;

//PacketPlayInFlying
@Getter
public final class FlyingPacket extends AbstractWrappedPacketIn {
    public static final FlyingPacket DEFAULT;

    static {
        DEFAULT = new FlyingPacket();
        DEFAULT.x = 0;
        DEFAULT.y = 0;
        DEFAULT.z = 0;
        DEFAULT.yaw = 0;
        DEFAULT.pitch = 0;
        DEFAULT.hasLook = false;
        DEFAULT.hasPos = true;
        DEFAULT.onGround = true;
    }

    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private boolean hasLook;
    private boolean hasPos;
    private boolean onGround;

    public FlyingPacket() {
        super(PacketConstants.In.FLYING);
    }
}

