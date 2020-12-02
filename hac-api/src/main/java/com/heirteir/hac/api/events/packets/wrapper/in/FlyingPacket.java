package com.heirteir.hac.api.events.packets.wrapper.in;

import com.heirteir.hac.api.events.packets.PacketConstants;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketIn;
import lombok.Getter;

//PacketPlayInFlying
@Getter
public final class FlyingPacket extends AbstractWrappedPacketIn {
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

