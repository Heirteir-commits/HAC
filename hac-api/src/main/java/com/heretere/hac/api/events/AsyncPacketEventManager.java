package com.heretere.hac.api.events;

import com.google.common.collect.Maps;
import com.heretere.hac.api.events.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;

public class AsyncPacketEventManager {
    private final Map<Class<? extends WrappedPacket>, PacketEventHandler> packetExecutors;

    public AsyncPacketEventManager() {
        this.packetExecutors = Maps.newHashMap();
    }

    public <T extends WrappedPacket> void registerPacketEventExecutor(AbstractPacketEventExecutor<T> executor) {
        this.getPacketEventHandler(executor.getWrappedClass()).addExecutor(executor);
    }

    public <T extends WrappedPacket> void unregisterPacketEventExecutor(AbstractPacketEventExecutor<T> executor) {
        this.getPacketEventHandler(executor.getWrappedClass()).removeExecutor(executor);
    }

    private PacketEventHandler getPacketEventHandler(Class<? extends WrappedPacket> packetClass) {
        return this.packetExecutors.computeIfAbsent(packetClass, pc -> new PacketEventHandler());
    }

    public void callPacketEvent(@NotNull HACPlayer player, @NotNull WrappedPacket packet, @Nullable BiConsumer<? super Void, ? super Throwable> errorHandler) {
        player.runTaskASync(
                () -> this.getPacketEventHandler(packet.getClass()).execute(player, packet)
                , errorHandler
        );
    }
}
