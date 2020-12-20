package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.heretere.hac.api.events.packets.builder.AbstractPacketBuilder;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying;

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
        double x = flying.a((double) location.getX()); //Must cast to double due to obfuscation
        double y = flying.b((double) location.getY()); //Must cast to double due to obfuscation
        double z = flying.c(location.getZ()); //Don't need to cast to double

        MutableVector2F direction = playerData.getCurrent().getDirection();
        double yaw = flying.a(direction.getX());
        double pitch = flying.b(direction.getY());

        return new FlyingPacket(
                x,
                y,
                z,
                yaw,
                pitch,
                flying.b() //onGround
        );
    }

    @Override
    public Class<FlyingPacket> getWrappedClass() {
        return FlyingPacket.class;
    }
}