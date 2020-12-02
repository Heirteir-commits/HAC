package com.heirteir.hac.core.packets;

import com.heirteir.hac.api.events.ASyncPacketEventManager;
import com.heirteir.hac.api.events.packets.PacketConstants;
import com.heirteir.hac.api.events.packets.wrapper.AbstractWrappedPacketOut;
import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.packets.builder.PacketBuilders;
import com.heirteir.hac.util.logging.Log;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public final class ChannelHandler extends ChannelDuplexHandler {

    private final PacketBuilders builders;
    private final ExecutorService pool;
    private final ASyncPacketEventManager eventManager;
    private final HACPlayer player;

    private CompletableFuture<Void> future;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        try {
            PacketConstants.In type = PacketConstants.getPacketTypeFromString(PacketConstants.In.class, msg.getClass().getSimpleName());

            if (type.equals(PacketConstants.In.UNKNOWN)) {
                return;
            }

            this.handle(type.getWrappedClass(), builders.get(type.getWrappedClass()).build(msg));
        } catch (Exception e) {
            Log.INSTANCE.severe(e);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);

        try {
            PacketConstants.Out type = PacketConstants.getPacketTypeFromString(PacketConstants.Out.class, msg.getClass().getSimpleName());

            if (type.equals(PacketConstants.Out.UNKNOWN)) {
                return;
            }

            AbstractWrappedPacketOut out = (AbstractWrappedPacketOut) this.builders.get(type.getWrappedClass()).build(msg);

            if (out.getEntityId() == this.player.getBukkitPlayer().getEntityId()) {
                this.handle(type.getWrappedClass(), out);
            }
        } catch (Exception e) {
            Log.INSTANCE.severe(e);
        }
    }

    private void handle(Class<? extends WrappedPacket> type, WrappedPacket packet) {
        if (this.future == null) {
            this.future = CompletableFuture.allOf();
        }

        this.future = this.future
                .thenRunAsync(() -> this.eventManager.run(this.player, type, packet), this.pool)
                .whenCompleteAsync((msg, ex) -> {
                    if (ex != null) {
                        Log.INSTANCE.severe(ex);
                    }
                }, this.pool);
    }
}
