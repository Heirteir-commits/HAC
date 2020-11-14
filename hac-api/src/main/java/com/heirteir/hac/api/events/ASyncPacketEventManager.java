package com.heirteir.hac.api.events;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.events.types.AbstractEventExecutor;
import com.heirteir.hac.api.events.types.Event;
import com.heirteir.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heirteir.hac.api.events.types.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public final class ASyncPacketEventManager {
    private final List<AbstractPacketEventExecutor<?>> packetEvents;
    private final List<AbstractEventExecutor<?>> events;

    public ASyncPacketEventManager() {
        this.packetEvents = new ArrayList<AbstractPacketEventExecutor<?>>() {
            @Override
            public boolean add(AbstractPacketEventExecutor<?> abstractPacketEventExecutor) {
                boolean output = super.add(abstractPacketEventExecutor);
                this.sort(Comparator.comparingInt(event -> event.getPriority().ordinal()));
                return output;
            }
        };

        this.events = new ArrayList<AbstractEventExecutor<?>>() {
            @Override
            public boolean add(AbstractEventExecutor<?> abstractEventExecutor) {
                boolean output = super.add(abstractEventExecutor);
                this.sort(Comparator.comparingInt(event -> event.getPriority().ordinal()));
                return output;
            }
        };
    }

    public void addPacketEvent(@NotNull AbstractPacketEventExecutor<?> event) {
        this.packetEvents.add(event);
    }

    public void removePacketEvent(@NotNull AbstractPacketEventExecutor<?> event) {
        this.packetEvents.remove(event);
    }

    public void addEvent(@NotNull AbstractEventExecutor<?> event) {
        this.events.add(event);
    }

    public void removeEvent(@NotNull AbstractEventExecutor<?> event) {
        this.events.remove(event);
    }

    public void callPacketEvent(@NotNull HACPlayer player, @NotNull WrappedPacket packet) {
        this.callPacketEvent(player, packet, null);
    }

    public void callPacketEvent(@NotNull HACPlayer player, @NotNull WrappedPacket packet, @Nullable BiConsumer<? super Void, ? super Throwable> errorHandler) {
        player.runTask(() -> this.packetEvents.stream()
                .filter(e -> e.getWrappedClass().equals(packet.getClass()))
                .filter(e -> !e.update(player, packet))
                .findFirst()
                .ifPresent(e -> e.onStop(player, packet)), errorHandler);
    }

    public void callEvent(@NotNull Event event) {
        HACPlayer player = API.INSTANCE.getHacPlayerList().getPlayer(event.getPlayer().getUniqueId());

        player.runTask(() -> this.events.stream()
                .filter(e -> e.getEventClass().equals(event.getClass()))
                .forEach(e -> e.run(player, event)), null);
    }

    public List<AbstractPacketEventExecutor<?>> getCurrentPacketEvents() {
        return this.packetEvents;
    }
}
