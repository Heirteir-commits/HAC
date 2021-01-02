package com.heretere.hac.core.proxy.packets.channel;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.packets.PacketReferences;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacketOut;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.HACPlugin;
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
 * This class is responsible for injecting a channel handler into the netty pipeline. So HAC can process,
 * incoming and outgoing packets for the checks.
 */
public abstract class ChannelInjector {
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
    private final @NotNull HACAPI api;
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACPlugin parent;
    /**
     * This executor service is responsible for attaching our channel handler in a way that doesn't
     * hinder the player's login speed.
     */
    private final @NotNull ExecutorService channelChangeExecutor;

    /**
     * Instantiates a new Channel injector base.
     *
     * @param api the HACAPI reference.
     * @param parent The parent HACPlugin instance.
     */
    protected ChannelInjector(
        final @NotNull HACAPI api,
        final @NotNull HACPlugin parent
    ) {
        this.api = api;
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
                ChannelInjector.AFTER_KEY,
                ChannelInjector.HANDLER_KEY,
                new HACChannelHandler(this.api, this.parent, player)
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
                if (pipeline.get(ChannelInjector.HANDLER_KEY) != null) {
                    pipeline.remove(ChannelInjector.HANDLER_KEY);
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
     * Gets the pipeline, this is implemented in core-nms.
     *
     * @param player the player
     * @return the pipeline
     */
    protected abstract @NotNull ChannelPipeline getPipeline(Player player);

    private static final class HACChannelHandler extends ChannelDuplexHandler {
        /**
         * The HACAPI reference.
         */
        private final @NotNull HACAPI api;

        /**
         * The plugin instance that is hosting HACChannelHandler.
         */
        private final @NotNull HACPlugin parent;


        /**
         * The HACPlayer this ChannelHandler is attached to.
         */
        private final @NotNull HACPlayer player;

        private HACChannelHandler(
            final @NotNull HACAPI api,
            final @NotNull HACPlugin parent,
            final @NotNull HACPlayer player
        ) {
            super();
            this.api = api;
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
                ? this.api
                .getPacketReferences()
                .getClientSide()
                .get(packet.getClass())
                : this.api.getPacketReferences().getServerSide().get(packet.getClass());


            optionalReference.ifPresent(reference -> {
                WrappedPacket wrappedPacket = reference.getBuilder().create(this.player, packet);

                if (clientSide) {
                    this.api.getEventManager().callPacketEvent(this.player, wrappedPacket);
                } else {
                    this.player.getBukkitPlayer().ifPresent(bukkitPlayer -> {
                        if (((WrappedPacketOut) wrappedPacket).getEntityId() == bukkitPlayer.getEntityId()) {
                            this.api.getEventManager().callPacketEvent(this.player, wrappedPacket);
                        }
                    });
                }
            });

        }
    }
}
