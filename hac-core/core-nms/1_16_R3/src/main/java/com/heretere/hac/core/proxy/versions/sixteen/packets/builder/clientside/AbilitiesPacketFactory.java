package com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside;

import com.heretere.hac.api.events.packets.factory.AbstractPacketFactory;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInAbilities;
import org.jetbrains.annotations.NotNull;

public final class AbilitiesPacketFactory extends AbstractPacketFactory<AbilitiesPacket> {

    /**
     * Builder for Abilities Packet.
     */
    public AbilitiesPacketFactory() {
        super(PacketPlayInAbilities.class);
    }

    @Override
    public AbilitiesPacket create(
            @NotNull final HACPlayer player,
            @NotNull final Object packet
    ) {
        PacketPlayInAbilities abilities = (PacketPlayInAbilities) packet;

        return new AbilitiesPacket(abilities.isFlying());
    }

    @Override
    public Class<AbilitiesPacket> getWrappedClass() {
        return AbilitiesPacket.class;
    }
}
