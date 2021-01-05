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
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.util.plugin.HACPlugin;
import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
     * @param api    the HACAPI reference.
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

}
