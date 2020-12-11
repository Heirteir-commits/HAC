package com.heretere.hac.core.proxy.versions.eight;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import com.heretere.hac.core.proxy.versions.eight.packets.builder.clientside.AbilitiesPacketBuilder;
import com.heretere.hac.core.proxy.versions.eight.packets.builder.clientside.EntityActionPacketBuilder;
import com.heretere.hac.core.proxy.versions.eight.packets.builder.clientside.FlyingPacketBuilder;
import com.heretere.hac.core.proxy.versions.eight.packets.builder.serverside.EntityVelocityPacketBuilder;
import com.heretere.hac.core.proxy.versions.eight.packets.channel.ChannelInjectorProxy;
import org.bukkit.plugin.Plugin;

public final class Proxy extends CoreVersionProxy {
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
    public AbstractChannelInjector getChannelInjector() {
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
