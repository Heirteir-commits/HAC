package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3d;
import com.heretere.hac.api.events.packets.factory.PacketFactory;
import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
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
        PlayerData playerData = player.getDataManager()
                                      .getData(PlayerData.class)
                                      .orElseThrow(IllegalArgumentException::new);

        Vector3d location = playerData.getCurrent().getLocation();
        Vector2f direction = playerData.getCurrent().getDirection();

        double x = flying.a(location.getX());
        double y = flying.b(location.getY());
        double z = flying.c(location.getZ());

        float yaw = flying.a(direction.getX());
        float pitch = flying.b(direction.getY());

        return new FlyingPacket(x, y, z, yaw, pitch, flying.b() /* onGround */);
    }

    @Override
    public @NotNull Class<FlyingPacket> getWrappedClass() {
        return FlyingPacket.class;
    }
}
