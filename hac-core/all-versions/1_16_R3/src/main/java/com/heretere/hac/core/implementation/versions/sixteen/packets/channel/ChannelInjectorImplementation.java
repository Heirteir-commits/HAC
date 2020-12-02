package com.heretere.hac.core.implementation.versions.sixteen.packets.channel;

import com.heretere.hac.core.implementation.packets.channel.ChannelInjector;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChannelInjectorImplementation extends ChannelInjector {
    @Override
    protected ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }

    public class ChannelHandlerSixteen extends ChannelDuplexHandler {

    }
}
