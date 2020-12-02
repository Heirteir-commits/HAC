package com.heretere.hac.core.proxy.versions.ten.packets.builder.clientside;

import com.heretere.hac.api.events.types.packets.builder.PacketBuilder;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import mikera.vectorz.Vector2;
import mikera.vectorz.Vector3;
import net.minecraft.server.v1_10_R1.PacketPlayInFlying;

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
        double x = flying.a(location.getX());
        double y = flying.b(location.getY());
        double z = flying.c(location.getZ());

        Vector2 direction = playerData.getCurrent().getDirection();
        double yaw = flying.a((float) direction.getX());
        double pitch = flying.a((float) direction.getY());

        return new FlyingPacket(
                x,
                y,
                z,
                yaw,
                pitch,
                flying.a() //onGround
        );
    }

    @Override
    public Class<FlyingPacket> getWrappedClass() {
        return FlyingPacket.class;
    }
}
