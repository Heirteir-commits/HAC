package com.heretere.hac.core.proxy.versions.fifteen.packets.channel;

import com.heretere.hac.core.proxy.packets.channel.ChannelInjectorBase;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class ChannelInjectorProxy extends ChannelInjectorBase {
    @Override
    protected ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
