package com.heretere.hac.core.proxy.versions.ten.packets.channel;

import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ChannelInjectorProxy extends AbstractChannelInjector {
    @Override
    protected ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
