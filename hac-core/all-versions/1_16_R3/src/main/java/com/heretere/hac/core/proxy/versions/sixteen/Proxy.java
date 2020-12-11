package com.heretere.hac.core.proxy.versions.sixteen;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.core.proxy.CoreVersionProxy;
import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside.AbilitiesPacketBuilder;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside.EntityActionPacketBuilder;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.clientside.FlyingPacketBuilder;
import com.heretere.hac.core.proxy.versions.sixteen.packets.builder.serverside.EntityVelocityPacketBuilder;
import com.heretere.hac.core.proxy.versions.sixteen.packets.channel.ChannelInjectorProxy;
import com.heretere.hac.util.plugin.AbstractHACPlugin;

public final class Proxy extends CoreVersionProxy {
    private final AbstractHACPlugin parent;
    private final ChannelInjectorProxy channelInjectorProxy;

    public Proxy(AbstractHACPlugin parent) {
        super(parent);
        this.parent = parent;
        channelInjectorProxy = new ChannelInjectorProxy(this.parent);
    }

    @Override
    protected void registerPackets() {
        PacketReferences packetReferences = HACAPI.getInstance().getPacketReferences();

        //clientside
        packetReferences.getClientSide().getAbilities().register(new AbilitiesPacketBuilder());
        packetReferences.getClientSide().getEntityAction().register(new EntityActionPacketBuilder());
        packetReferences.getClientSide().getFlying().register(new FlyingPacketBuilder());

        //serverside
        packetReferences.getServerSide().getEntityVelocity().register(new EntityVelocityPacketBuilder(this.parent));
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
