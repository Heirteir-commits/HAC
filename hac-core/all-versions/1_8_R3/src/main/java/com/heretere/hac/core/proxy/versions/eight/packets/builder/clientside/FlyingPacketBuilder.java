package com.heretere.hac.core.proxy.versions.eight.packets.builder.clientside;

import com.heretere.hac.api.events.packets.builder.AbstractPacketBuilder;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import com.heretere.hac.core.util.vector.MutableVector2F;
import com.heretere.hac.core.util.vector.MutableVector3F;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public final class FlyingPacketBuilder extends AbstractPacketBuilder<FlyingPacket> {
    public FlyingPacketBuilder() {
        super(
                PacketPlayInFlying.class,
                PacketPlayInFlying.PacketPlayInPositionLook.class,
                PacketPlayInFlying.PacketPlayInPosition.class,
                PacketPlayInFlying.PacketPlayInLook.class
        );
    }

    @Override
    public FlyingPacket create(HACPlayer player, Object packet) {
        PacketPlayInFlying flying = (PacketPlayInFlying) packet;
        PlayerData playerData = player.getDataManager().getData(PlayerData.class);

        MutableVector3F location = playerData.getCurrent().getLocation();
        boolean hasPos = flying.g();
        double x = hasPos ? flying.a() : location.getX();
        double y = hasPos ? flying.b() : location.getY();
        double z = hasPos ? flying.c() : location.getZ();

        MutableVector2F direction = playerData.getCurrent().getDirection();
        boolean hasLook = flying.h();
        double yaw = hasLook ? flying.d() : direction.getX();
        double pitch = hasLook ? flying.e() : direction.getY();

        return new FlyingPacket(
                x,
                y,
                z,
                yaw,
                pitch,
                flying.f() //onGround
        );
    }

    @Override
    public Class<FlyingPacket> getWrappedClass() {
        return FlyingPacket.class;
    }
}
