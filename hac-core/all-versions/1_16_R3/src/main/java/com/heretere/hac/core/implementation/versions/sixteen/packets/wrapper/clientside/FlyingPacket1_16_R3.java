package com.heretere.hac.core.implementation.versions.sixteen.packets.wrapper.clientside;

import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying;

public class FlyingPacket1_16_R3 extends FlyingPacket {
    public FlyingPacket1_16_R3(Object flyingPacket) {
        PacketPlayInFlying flying = (PacketPlayInFlying) flyingPacket;

        super.x = flying.a(FlyingPacket.UNIQUE_DOUBLE);
        super.y = flying.b(FlyingPacket.UNIQUE_DOUBLE);
        super.z = flying.c(FlyingPacket.UNIQUE_DOUBLE);
        super.yaw = flying.a(FlyingPacket.UNIQUE_FLOAT);
        super.pitch = flying.b(FlyingPacket.UNIQUE_FLOAT);
        super.hasPos = super.x != FlyingPacket.UNIQUE_DOUBLE || super.y != FlyingPacket.UNIQUE_DOUBLE || super.z != FlyingPacket.UNIQUE_DOUBLE;
        super.hasLook = super.yaw != FlyingPacket.UNIQUE_FLOAT || super.pitch != FlyingPacket.UNIQUE_FLOAT;

        super.x = super.x == FlyingPacket.UNIQUE_DOUBLE ? 0 : super.x;
        super.y = super.y == FlyingPacket.UNIQUE_DOUBLE ? 0 : super.y;
        super.z = super.z == FlyingPacket.UNIQUE_DOUBLE ? 0 : super.z;
        super.yaw = super.yaw == FlyingPacket.UNIQUE_DOUBLE ? 0 : super.yaw;
        super.pitch = super.pitch == FlyingPacket.UNIQUE_DOUBLE ? 0 : super.pitch;
    }

}
