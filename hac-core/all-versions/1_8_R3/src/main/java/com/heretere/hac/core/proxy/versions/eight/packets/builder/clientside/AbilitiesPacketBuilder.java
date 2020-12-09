package com.heretere.hac.core.proxy.versions.eight.packets.builder.clientside;

import com.heretere.hac.api.events.packets.builder.AbstractPacketBuilder;
import com.heretere.hac.api.events.packets.wrapper.clientside.AbilitiesPacket;
import com.heretere.hac.api.player.HACPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInAbilities;

public final class AbilitiesPacketBuilder extends AbstractPacketBuilder<AbilitiesPacket> {

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
