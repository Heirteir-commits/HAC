package com.heretere.hac.core.proxy.versions.eight.packets.builder.clientside;

import com.heretere.hac.api.events.types.packets.builder.PacketBuilder;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import mikera.vectorz.Vector2;
import mikera.vectorz.Vector3;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public final class FlyingPacketBuilder extends PacketBuilder<FlyingPacket> {
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

        Vector3 location = playerData.getCurrent().getLocation();
        boolean hasPos = flying.g();
        double x = hasPos ? flying.a() : location.getX();
        double y = hasPos ? flying.b() : location.getY();
        double z = hasPos ? flying.c() : location.getZ();

        Vector2 direction = playerData.getCurrent().getDirection();
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
