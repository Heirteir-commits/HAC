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
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Channel injector base.
 */
public abstract class AbstractChannelInjector {
    /**
     * This is the name of the channel handler we want to place our custom channel handler before in the netty channel
     * pipeline.
     */
    private static final @NotNull String AFTER_KEY = "packet_handler";
    /**
     * The name of HAC's channel handler identifier.
     */
    private static final @NotNull String HANDLER_KEY = "hac_packet_handler";
    /**
     * The HACAPI reference.
     */
    private final @NotNull AbstractHACPlugin parent;
    /**
     * This executor service is responsible for attaching our channel handler in a way that doesn't
     * hinder the player's login speed.
     */
    private final @NotNull ExecutorService channelChangeExecutor;

    /**
     * Instantiates a new Channel injector base.
     *
     * @param parent The parent HACPlugin instance.
     */
    protected AbstractChannelInjector(final @NotNull AbstractHACPlugin parent) {
        this.parent = parent;
        this.channelChangeExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Injects the HAC Channel Handler into the player's pipeline.
     *
     * @param player the player
     */
    public void inject(final @NotNull HACPlayer player) {
        player.getBukkitPlayer().ifPresent(bukkitPlayer -> {
            this.remove(player);
            this.channelChangeExecutor.execute(() -> this.getPipeline(bukkitPlayer).addBefore(
                AbstractChannelInjector.AFTER_KEY,
                AbstractChannelInjector.HANDLER_KEY,
                new HACChannelHandler(this.parent, player)
            ));
        });
    }

    /**
     * Removes the HAC Channel Handler from the player's pipeline.
     *
     * @param player the player
     */
    public void remove(final @NotNull HACPlayer player) {
        player.getBukkitPlayer().ifPresent(bukkitPlayer -> {
            ChannelPipeline pipeline = this.getPipeline(bukkitPlayer);

            this.channelChangeExecutor.execute(() -> {
                if (pipeline.get(AbstractChannelInjector.HANDLER_KEY) != null) {
                    pipeline.remove(AbstractChannelInjector.HANDLER_KEY);
                }
            });
        });
    }

    /**
     * Shuts down the channel change executor.
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
    protected abstract @NotNull ChannelPipeline getPipeline(Player player);

    private static final class HACChannelHandler extends ChannelDuplexHandler {
        /**
         * The plugin instance that is hosting HACChannelHandler.
         */
        private final @NotNull AbstractHACPlugin parent;

        /**
         * The HACPlayer this ChannelHandler is attached to.
         */
        private final @NotNull HACPlayer player;

        private HACChannelHandler(
            final @NotNull AbstractHACPlugin parent,
            final @NotNull HACPlayer player
        ) {
            super();
            this.parent = parent;
            this.player = player;
        }

        @Override
        public void write(
            final ChannelHandlerContext ctx,
            final Object msg,
            final ChannelPromise promise
        ) throws Exception {
            super.write(ctx, msg, promise);

            try {
                this.handle(msg, false);
            } catch (Exception e) {
                this.parent.getLog().severe(e);
            }
        }

        @Override
        public void channelRead(
            final ChannelHandlerContext ctx,
            final Object msg
        ) throws Exception {
            super.channelRead(ctx, msg);

            try {
                this.handle(msg, true);
            } catch (Exception e) {
                this.parent.getLog().severe(e);
            }
        }

        private void handle(
            final @NotNull Object packet,
            final boolean clientSide
        ) {
            Optional<PacketReferences.PacketReference<?>> optionalReference = clientSide
                ? HACAPI.getInstance()
                        .getPacketReferences()
                        .getClientSide()
                        .get(packet.getClass())
                : HACAPI.getInstance().getPacketReferences().getServerSide().get(packet.getClass());


            optionalReference.ifPresent(reference -> {
                WrappedPacket wrappedPacket = reference.getBuilder().create(this.player, packet);

                if (clientSide) {
                    HACAPI.getInstance().getEventManager().callPacketEvent(this.player, wrappedPacket);
                } else {
                    this.player.getBukkitPlayer().ifPresent(bukkitPlayer -> {
                        if (((AbstractWrappedPacketOut) wrappedPacket).getEntityId() == bukkitPlayer.getEntityId()) {
                            HACAPI.getInstance().getEventManager().callPacketEvent(this.player, wrappedPacket);
                        }
                    });
                }
            });

        }
    }
}
