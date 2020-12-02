package com.heretere.hac.core.implementation.packets.channel;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ChannelInjector {
    private static final String AFTER_KEY = "packet_handler";
    private static final String HANDLER_KEY = "hac_packet_handler";

    private final ExecutorService channelChangeExecutor;

    protected ChannelInjector() {
        this.channelChangeExecutor = Executors.newSingleThreadExecutor();
    }

    public void inject(Player player) {
        Bukkit.getOfflinePlayer()
        this.remove(player);
        this.channelChangeExecutor.execute(() -> this.getPipeline(player)
                .addBefore(ChannelInjector.AFTER_KEY, ChannelInjector.HANDLER_KEY, new HACChannelHandler()));
    }

    public void remove(Player player) {
        ChannelPipeline pipeline = this.getPipeline(player);

        this.channelChangeExecutor.execute(() -> {
            if (pipeline.get(ChannelInjector.HANDLER_KEY) != null) {
                pipeline.remove(ChannelInjector.HANDLER_KEY);
            }
        });
    }

    public void shutdown() {
        this.channelChangeExecutor.shutdown();
    }

    protected abstract ChannelPipeline getPipeline(Player player);

    private static class HACChannelHandler extends ChannelDuplexHandler {

    }
}
