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

    public FlyingPacket(double x, double y, double z, double yaw, double pitch, boolean hasLook, boolean hasPos, boolean onGround) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.hasLook = hasLook;
        this.hasPos = hasPos;
        this.onGround = onGround;
    }
}

