/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3d;
import com.heretere.hac.api.event.packet.factory.PacketFactory;
import com.heretere.hac.api.event.packet.wrapper.clientside.FlyingPacket;
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
