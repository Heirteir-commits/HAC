package com.heirteir.hac.core.packets;

import com.heirteir.hac.api.events.ASyncPacketEventManager;
import com.heirteir.hac.api.events.types.packets.PacketConstants;
import com.heirteir.hac.api.events.types.packets.wrapper.AbstractWrappedPacketOut;
import com.heirteir.hac.api.events.types.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.Core;
import com.heirteir.hac.core.packets.builder.PacketBuilders;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class ChannelHandler extends ChannelDuplexHandler {

    private final Core core;
    private final PacketBuilders builders;
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

            this.handle(builders.get(type.getWrappedClass()).build(msg));
        } catch (Exception e) {
            this.core.getLog().severe(e);
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
                this.handle(out);
            }
        } catch (Exception e) {
            this.core.getLog().severe(e);
        }
    }

    private void handle(WrappedPacket packet) {
        if (this.future == null) {
            this.future = CompletableFuture.allOf();
        }

        this.eventManager.callPacketEvent(this.player, packet);
    }
}
