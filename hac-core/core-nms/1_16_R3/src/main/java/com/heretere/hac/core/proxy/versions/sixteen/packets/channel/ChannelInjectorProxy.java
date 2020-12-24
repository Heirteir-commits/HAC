package com.heretere.hac.core.proxy.versions.sixteen.packets.channel;

import com.heretere.hac.core.proxy.packets.channel.ChannelInjector;
import com.heretere.hac.util.plugin.HACPlugin;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ChannelInjectorProxy extends ChannelInjector {
    /**
     * Instantiates a new Channel injector base.
     *
     * @param parent the parent plugin
     */
    public ChannelInjectorProxy(final @NotNull HACPlugin parent) {
        super(parent);
    }

    @Override
    protected @NotNull ChannelPipeline getPipeline(final @NotNull Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
