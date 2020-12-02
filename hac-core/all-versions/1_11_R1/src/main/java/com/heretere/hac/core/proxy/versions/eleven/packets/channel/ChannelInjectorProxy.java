package com.heretere.hac.core.proxy.versions.eleven.packets.channel;

import com.heretere.hac.core.proxy.packets.channel.ChannelInjectorBase;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ChannelInjectorProxy extends ChannelInjectorBase {
    @Override
    protected ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
