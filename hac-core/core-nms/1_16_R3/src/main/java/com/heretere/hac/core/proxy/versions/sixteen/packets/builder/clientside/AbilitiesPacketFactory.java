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

import com.heretere.hac.api.packet.factory.PacketFactory;
import com.heretere.hac.api.packet.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInAbilities;
import org.jetbrains.annotations.NotNull;

public final class AbilitiesPacketFactory extends PacketFactory<AbilitiesPacket> {

    /**
     * Builder for Abilities Packet.
     */
    public AbilitiesPacketFactory() {
        super(PacketPlayInAbilities.class);
    }

    @Override
    public @NotNull AbilitiesPacket create(
        final @NotNull HACPlayer player,
        final @NotNull Object packet
    ) {
        PacketPlayInAbilities abilities = (PacketPlayInAbilities) packet;

        return new AbilitiesPacket(abilities.isFlying());
    }

    @Override
    public @NotNull Class<AbilitiesPacket> getWrappedClass() {
        return AbilitiesPacket.class;
    }
}
