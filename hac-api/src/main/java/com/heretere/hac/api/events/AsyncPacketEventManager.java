package com.heretere.hac.api.events;

import com.google.common.collect.Maps;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;

public class AsyncPacketEventManager {
    /**
     * The packet executors that this event manager handles.
     */
    private final @NotNull Map<Class<? extends WrappedPacket>, PacketEventHandler> packetExecutors;

    /**
     * Creates a new AsyncPacketEventManager.
     */
    public AsyncPacketEventManager() {
        this.packetExecutors = Maps.newIdentityHashMap();
    }

    /**
     * Register a packet event executor.
     *
     * @param executor The executor instance.
     * @param <T>      WrappedPacket type
     */
    public <T extends WrappedPacket> void registerPacketEventExecutor(
        final @NotNull AbstractPacketEventExecutor<T> executor
    ) {
        this.getPacketEventHandler(executor.getWrappedClass()).addExecutor(executor);
    }

    /**
     * Unregisters a packet event executor.
     *
     * @param executor The executor instance
     * @param <T>      WrappedPacket type
     */
    public <T extends WrappedPacket> void unregisterPacketEventExecutor(
        final @NotNull AbstractPacketEventExecutor<T> executor
    ) {
        this.getPacketEventHandler(executor.getWrappedClass()).removeExecutor(executor);
    }

    private @NotNull PacketEventHandler getPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.packetExecutors.computeIfAbsent(packetClass, pc -> new PacketEventHandler());
    }

    /**
     * Calls a packet event.
     *
     * @param player       The HACPlayer
     * @param packet       The WrappedPacket
     * @param errorHandler An optional error handler.
     */
    public void callPacketEvent(
        final @NotNull HACPlayer player,
        final @NotNull WrappedPacket packet,
        final @Nullable BiConsumer<? super Void, ? super Throwable> errorHandler
    ) {
        player.runTaskAsync(() -> this.getPacketEventHandler(packet.getClass()).execute(player, packet), errorHandler);
    }

    /**
     * Delegates to {@link AsyncPacketEventManager#callPacketEvent(HACPlayer, WrappedPacket, BiConsumer)}.
     * Just sets the error handler to null.
     *
     * @param player The HACPlayer
     * @param packet The wrapped packet
     */
    public void callPacketEvent(
        final @NotNull HACPlayer player,
        final @NotNull WrappedPacket packet
    ) {
        this.callPacketEvent(player, packet, null);
    }
}
