package com.heretere.hac.core.proxy.versions.fifteen;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.types.packets.PacketReferences;
import com.heretere.hac.core.proxy.VersionProxy;
import com.heretere.hac.core.proxy.packets.channel.ChannelInjectorBase;
import com.heretere.hac.core.proxy.versions.fifteen.packets.builder.clientside.AbilitiesPacketBuilder;
import com.heretere.hac.core.proxy.versions.fifteen.packets.builder.clientside.EntityActionPacketBuilder;
import com.heretere.hac.core.proxy.versions.fifteen.packets.builder.clientside.FlyingPacketBuilder;
import com.heretere.hac.core.proxy.versions.fifteen.packets.builder.serverside.EntityVelocityPacketBuilder;
import com.heretere.hac.core.proxy.versions.fifteen.packets.channel.ChannelInjectorProxy;
import org.bukkit.plugin.Plugin;

public final class Proxy extends VersionProxy {
    private final ChannelInjectorProxy channelInjectorProxy;

    public Proxy(Plugin parent) {
        super(parent);
        channelInjectorProxy = new ChannelInjectorProxy();
    }

    @Override
    protected void registerPackets() {
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
        return this.channelInjectorProxy;
    }

    @Override
    protected void load() {
        //Nothing yet
    }

    @Override
    protected void unload() {
        this.channelInjectorProxy.shutdown();
    }
}
