package com.heretere.hac.api.event;

import com.google.common.collect.Maps;
import com.heretere.hac.api.event.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

/**
 * The Event Manager handles executing tasks on specific threads.
 */
public final class EventManager {
    /**
     * The event executors this manager handles.
     */
    private final @NotNull Map<Class<? extends WrappedPacket>,
        SimpleImmutableEntry<PacketEventHandler, PacketEventHandler>> executors;

    /**
     * Creates a new Event Manager.
     */
    public EventManager() {
        this.executors = Maps.newIdentityHashMap();
    }

    /**
     * Register a packet event executor.
     *
     * @param executor The executor instance.
     * @param <T>      WrappedPacket type
     */
    public <T extends WrappedPacket> void registerPacketEventExecutor(
        final @NotNull PacketEventExecutor<T> executor
    ) {
        if (executor.isSync()) {
            this.getSyncPacketEventHandler(executor.getWrappedClass())
                .addExecutor(executor);
        } else {
            this.getAsyncPacketEventHandler(executor.getWrappedClass())
                .addExecutor(executor);
        }
    }

    /**
     * Unregister a packet event executor.
     *
     * @param executor The executor instance
     * @param <T>      WrappedPacket type
     */
    public <T extends WrappedPacket> void unregisterPacketEventExecutor(
        final @NotNull PacketEventExecutor<T> executor
    ) {
        if (executor.isSync()) {
            this.getSyncPacketEventHandler(executor.getWrappedClass())
                .removeExecutor(executor);
        } else {
            this.getAsyncPacketEventHandler(executor.getWrappedClass())
                .removeExecutor(executor);
        }
    }

    private @NotNull PacketEventHandler getAsyncPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.getEventHandlers(packetClass).getKey();
    }

    private @NotNull PacketEventHandler getSyncPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.getEventHandlers(packetClass).getValue();
    }

    private @NotNull SimpleImmutableEntry<PacketEventHandler, PacketEventHandler> getEventHandlers(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.executors.computeIfAbsent(
            packetClass,
            pc -> new SimpleImmutableEntry<>(
                new PacketEventHandler(),
                new PacketEventHandler()
            )
        );
    }

    /**
     * Calls a packet event.
     *
     * @param player The HACPlayer
     * @param packet The WrappedPacket
     */
    public void callPacketEvent(
        final @NotNull HACPlayer player,
        final @NotNull WrappedPacket packet
    ) {
        SimpleImmutableEntry<PacketEventHandler, PacketEventHandler> syncAsyncHandlerEntry =
            this.getEventHandlers(packet.getClass());

        if (syncAsyncHandlerEntry.getKey().getSize() != 0) {
            player.getFutureChain()
                  .addAsyncTask(() -> syncAsyncHandlerEntry.getKey().execute(player, packet));
        }

        if (syncAsyncHandlerEntry.getValue().getSize() != 0) {
            player.getFutureChain()
                  .addServerMainThreadTask(() -> syncAsyncHandlerEntry.getValue().execute(player, packet));
        }
    }
}
