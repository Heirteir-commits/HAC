package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.heretere.hac.api.events.packets.factory.PacketFactory;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
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
