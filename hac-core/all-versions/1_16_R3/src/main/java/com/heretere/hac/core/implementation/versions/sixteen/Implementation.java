package com.heretere.hac.core.implementation.versions.sixteen;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.types.packets.PacketReferences;
import com.heretere.hac.core.implementation.packets.channel.ChannelInjectorBase;
import com.heretere.hac.core.implementation.versions.VersionImplementation;
import com.heretere.hac.core.implementation.versions.sixteen.packets.builder.clientside.AbilitiesPacketBuilder;
import com.heretere.hac.core.implementation.versions.sixteen.packets.builder.clientside.EntityActionPacketBuilder;
import com.heretere.hac.core.implementation.versions.sixteen.packets.builder.clientside.FlyingPacketBuilder;
import com.heretere.hac.core.implementation.versions.sixteen.packets.builder.serverside.EntityVelocityPacketBuilder;
import com.heretere.hac.core.implementation.versions.sixteen.packets.channel.ChannelInjector;

public class Implementation implements VersionImplementation {
    private final ChannelInjector channelInjector;

    public Implementation() {
        channelInjector = new ChannelInjector();
    }

    @Override
    public void registerPackets() {
        PacketReferences packetReferences = HACAPI.getInstance().getPacketReferences();

        //clientside
        packetReferences.getClientSide().getAbilities().register(new AbilitiesPacketBuilder());
        packetReferences.getClientSide().getEntityAction().register(new EntityActionPacketBuilder());
        packetReferences.getClientSide().getFlying().register(new FlyingPacketBuilder());

        //serverside
        packetReferences.getServerSide().getEntityVelocity().register(new EntityVelocityPacketBuilder());
    }

    @Override
    public ChannelInjectorBase getChannelInjector() {
        return this.channelInjector;
    }
}
