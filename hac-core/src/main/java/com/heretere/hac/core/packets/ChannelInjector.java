package com.heretere.hac.core.packets;

import com.heretere.hac.api.API;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.util.reflections.types.WrappedField;
import com.heretere.hac.core.Core;
import com.heretere.hac.core.packets.builder.PacketBuilders;
import com.heretere.hac.core.util.reflections.helper.EntityHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ChannelInjector {
    private static final String AFTER_KEY = "packet_handler";
    private static final String HANDLER_KEY = "hac_packet_handler";

    private final Core core;

    private WrappedField networkManager;
    private WrappedField channel;

    @Getter
    private PacketBuilders builders;

    private ExecutorService channelChangeExecutor;

    public ChannelInjector(Core core) {
        this.core = core;
        try {
            this.networkManager = API.INSTANCE.getReflections().getNMSClass("PlayerConnection").getFieldByName("networkManager");
            this.channel = API.INSTANCE.getReflections().getNMSClass("NetworkManager").getFieldByType(Channel.class, 0);

            this.builders = new PacketBuilders(core);
            this.channelChangeExecutor = Executors.newSingleThreadExecutor();
        } catch (NoSuchFieldException | IndexOutOfBoundsException e) {
            this.core.getLog().reportFatalError(e);
        }
    }

    public void inject(HACPlayer player) {
        this.remove(player.getBukkitPlayer());
        this.channelChangeExecutor.execute(() -> this.getPipeline(player.getBukkitPlayer()).addBefore(ChannelInjector.AFTER_KEY, ChannelInjector.HANDLER_KEY, new ChannelHandler(this.core, this.builders, API.INSTANCE.getEventManager(), player)));
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
        this.channelChangeExecutor.shutdownNow();
    }

    private ChannelPipeline getPipeline(Player player) {
        ChannelPipeline output;
        try {

            output = this.channel.get(Channel.class,
                    this.networkManager.get(Object.class,
                            API.INSTANCE.getReflections().getHelpers().getHelper(EntityHelper.class).getPlayerConnection(player)
                    )).pipeline();
        } catch (Exception e) {
            output = null;
            this.core.getLog().reportFatalError(e);
        }
        return output;
    }
}
