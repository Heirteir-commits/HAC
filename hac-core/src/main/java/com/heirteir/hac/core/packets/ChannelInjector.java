package com.heirteir.hac.core.packets;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.util.reflections.types.WrappedField;
import com.heirteir.hac.core.packets.builder.PacketBuilders;
import com.heirteir.hac.core.util.reflections.helper.PlayerHelper;
import com.heirteir.hac.util.logging.Log;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ChannelInjector {
    private static final String AFTER_KEY = "packet_handler";
    private static final String HANDLER_KEY = "hac_player_handler";

    private WrappedField networkManager;
    private WrappedField channel;

    @Getter
    private PacketBuilders builders;

    private ExecutorService channelChangeExecutor;
    private ExecutorService pool;

    public ChannelInjector() {
        try {
            this.networkManager = API.INSTANCE.getReflections().getNMSClass("PlayerConnection").getFieldByName("networkManager");
            this.channel = API.INSTANCE.getReflections().getNMSClass("NetworkManager").getFieldByType(Channel.class, 0);

            this.builders = new PacketBuilders();
            this.channelChangeExecutor = Executors.newSingleThreadExecutor();
            this.pool = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("hac-channel-handler-thread-%d").build());
        } catch (NoSuchFieldException | IndexOutOfBoundsException e) {
            Log.INSTANCE.reportFatalError(e);
        }
    }

    public void inject(HACPlayer player) {
        this.remove(player.getBukkitPlayer());
        this.channelChangeExecutor.execute(() -> this.getPipeline(player.getBukkitPlayer()).addBefore(ChannelInjector.AFTER_KEY, ChannelInjector.HANDLER_KEY, new ChannelHandler(this.builders, this.pool, API.INSTANCE.getEventManager(), player)));
    }

    public void remove(Player player) {
        ChannelPipeline pipeline = this.getPipeline(player);

        this.channelChangeExecutor.execute(() -> {
            if (pipeline.get(ChannelInjector.HANDLER_KEY) != null) {
                pipeline.remove(ChannelInjector.HANDLER_KEY);
            }
        });
    }

    public void unload() {
        this.pool.shutdownNow();
        this.channelChangeExecutor.shutdownNow();
    }

    private ChannelPipeline getPipeline(Player player) {
        ChannelPipeline output;
        try {
            output = this.channel.get(Channel.class, this.networkManager.get(Object.class, API.INSTANCE.getReflections().getHelpers().getHelper(PlayerHelper.class).getPlayerConnection(player))).pipeline();
        } catch (IllegalAccessException e) {
            output = null;
            Log.INSTANCE.reportFatalError(e);
        }
        return output;
    }
}
