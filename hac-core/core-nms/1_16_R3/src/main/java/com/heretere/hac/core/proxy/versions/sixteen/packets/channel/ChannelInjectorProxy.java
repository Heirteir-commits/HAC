package com.heretere.hac.core.proxy.versions.sixteen.packets.channel;

import com.heretere.hac.core.proxy.packets.channel.AbstractChannelInjector;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ChannelInjectorProxy extends AbstractChannelInjector {
    /**
     * Instantiates a new Channel injector base.
     *
     * @param parent the parent plugin
     */
    public ChannelInjectorProxy(@NotNull final AbstractHACPlugin parent) {
        super(parent);
    }

    @Override
    protected ChannelPipeline getPipeline(@NotNull final Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
