package com.heretere.hac.core.proxy.packets.channel;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.AbstractWrappedPacketOut;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.AbstractHACPlugin;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Channel injector base.
 */
public abstract class AbstractChannelInjector {
    private final AbstractHACPlugin parent;

    private static final String AFTER_KEY = "packet_handler";
    private static final String HANDLER_KEY = "hac_packet_handler";

    private final ExecutorService channelChangeExecutor;

    /**
     * Instantiates a new Channel injector base.
     */
    protected AbstractChannelInjector(AbstractHACPlugin parent) {
        this.parent = parent;
        this.channelChangeExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Injects the HAC Channel Handler into the player's pipeline.
     *
     * @param player the player
     */
    public void inject(HACPlayer player) {
        this.remove(player);
        this.channelChangeExecutor.execute(() -> this.getPipeline(player.getBukkitPlayer())
                .addBefore(AbstractChannelInjector.AFTER_KEY, AbstractChannelInjector.HANDLER_KEY, new HACChannelHandler(this.parent, player)));
    }

    /**
     * Removes the HAC Channel Handler from the player's pipeline
     *
     * @param player the player
     */
    public void remove(HACPlayer player) {
        ChannelPipeline pipeline = this.getPipeline(player.getBukkitPlayer());

        this.channelChangeExecutor.execute(() -> {
            if (pipeline.get(AbstractChannelInjector.HANDLER_KEY) != null) {
                pipeline.remove(AbstractChannelInjector.HANDLER_KEY);
            }
        });
    }

    /**
     * Shuts down the channel change executor
     */
    public void shutdown() {
        this.channelChangeExecutor.shutdown();
    }

    /**
     * Gets pipeline.
     *
     * @param player the player
     * @return the pipeline
     */
    protected abstract ChannelPipeline getPipeline(Player player);

    private static final class HACChannelHandler extends ChannelDuplexHandler {
        private final AbstractHACPlugin parent;
        private final HACPlayer player;

        private HACChannelHandler(AbstractHACPlugin parent, HACPlayer player) {
            super();
            this.parent = parent;
            this.player = player;
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);

            try {
                this.handle(msg, false);
            } catch (Exception e) {
                this.parent.getLog().severe(e);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);

            try {
                this.handle(msg, true);
            } catch (Exception e) {
                this.parent.getLog().severe(e);
            }
        }

        private void handle(Object packet, boolean clientSide) {
            PacketReferences.PacketReference<?> reference =
                    clientSide ?
                            HACAPI.getInstance().getPacketReferences().getClientSide().get(packet.getClass()) :
                            HACAPI.getInstance().getPacketReferences().getServerSide().get(packet.getClass());

            if (reference == null) {
                return;
            }

            WrappedPacket wrappedPacket = reference.getBuilder().create(this.player, packet);

            if (clientSide || ((AbstractWrappedPacketOut) wrappedPacket).getEntityId() == player.getBukkitPlayer().getEntityId()) {
                HACAPI.getInstance().getEventManager().callPacketEvent(
                        this.player,
                        wrappedPacket,
                        null
                );
            }
        }
    }
}
