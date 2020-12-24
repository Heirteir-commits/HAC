package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.heretere.hac.api.events.packets.factory.PacketFactory;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying;
import org.jetbrains.annotations.NotNull;

/**
 * The type Flying packet factory.
 */
public final class FlyingPacketFactory extends PacketFactory<FlyingPacket> {
    /**
     * Instantiates a new Flying packet factory.
     */
    public FlyingPacketFactory() {
        super(
            PacketPlayInFlying.class,
            PacketPlayInFlying.PacketPlayInPositionLook.class,
            PacketPlayInFlying.PacketPlayInPosition.class,
            PacketPlayInFlying.PacketPlayInLook.class
        );
    }

    @Override
    public @NotNull FlyingPacket create(
        final @NotNull HACPlayer player,
        final @NotNull Object packet
    ) {
        PacketPlayInFlying flying = (PacketPlayInFlying) packet;
        PlayerData playerData = player.getDataManager().getData(PlayerData.class).orElse(null);

        MutableVector3F location;
        MutableVector2F direction;

        if (playerData == null) {
            location = new MutableVector3F(0, 0, 0);
            direction = new MutableVector2F(0, 0);
        } else {
            location = playerData.getCurrent().getLocation();
            direction = playerData.getCurrent().getDirection();
        }

        double x = flying.a((double) location.getX()); //Must cast to double due to obfuscation
        double y = flying.b((double) location.getY()); //Must cast to double due to obfuscation
        double z = flying.c(location.getZ()); //Don't need to cast to double

        double yaw = flying.a(direction.getX());
        double pitch = flying.b(direction.getY());

        return new FlyingPacket(x, y, z, yaw, pitch, flying.b() /* onGround */);
    }

    @Override
    public @NotNull Class<FlyingPacket> getWrappedClass() {
        return FlyingPacket.class;
    }
}
