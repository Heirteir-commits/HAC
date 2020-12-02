package com.heretere.hac.core.implementation.versions.sixteen.packets.builder.clientside;

import com.heretere.hac.api.events.types.packets.builder.PacketBuilder;
import com.heretere.hac.api.events.types.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInAbilities;

public final class AbilitiesPacketBuilder extends PacketBuilder<AbilitiesPacket> {

    public AbilitiesPacketBuilder() {
        super(PacketPlayInAbilities.class);
    }

    @Override
    public AbilitiesPacket create(HACPlayer player, Object packet) {
        PacketPlayInAbilities abilities = (PacketPlayInAbilities) packet;

        return new AbilitiesPacket(abilities.isFlying());
    }

    @Override
    public Class<AbilitiesPacket> getWrappedClass() {
        return AbilitiesPacket.class;
    }
}
