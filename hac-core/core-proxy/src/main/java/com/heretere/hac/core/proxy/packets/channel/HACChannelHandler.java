/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.core.proxy.packets.channel;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.packet.PacketReference;
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.packet.wrapper.WrappedPacketOut;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.HACPlugin;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

final class HACChannelHandler extends ChannelDuplexHandler {
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

    HACChannelHandler(
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
        Optional<PacketReference<?>> optionalReference = clientSide
            ? this.api.getPacketReferences().getClientSide().getReferenceFromNMSClass(packet.getClass())
            : this.api.getPacketReferences().getServerSide().getReferenceFromNMSClass(packet.getClass());


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
