package com.heretere.hac.api.events;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.types.AbstractEventExecutor;
import com.heretere.hac.api.events.types.Event;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.events.types.packets.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * The type A sync packet event manager.
 */
public final class ASyncPacketEventManager {
    private final List<AbstractPacketEventExecutor<?>> packetEvents;
    private final List<AbstractEventExecutor<?>> events;

    /**
     * Instantiates a new A sync packet event manager.
     */
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

    /**
     * Add packet event.
     *
     * @param event the event
     */
    public void addPacketEvent(@NotNull AbstractPacketEventExecutor<?> event) {
        this.packetEvents.add(event);
    }

    /**
     * Remove packet event.
     *
     * @param event the event
     */
    public void removePacketEvent(@NotNull AbstractPacketEventExecutor<?> event) {
        this.packetEvents.remove(event);
    }

    /**
     * Add event.
     *
     * @param event the event
     */
    public void addEvent(@NotNull AbstractEventExecutor<?> event) {
        this.events.add(event);
    }

    /**
     * Remove event.
     *
     * @param event the event
     */
    public void removeEvent(@NotNull AbstractEventExecutor<?> event) {
        this.events.remove(event);
    }

    /**
     * Call packet event.
     *
     * @param player the player
     * @param packet the packet
     */
    public void callPacketEvent(@NotNull HACPlayer player, @NotNull WrappedPacket packet) {
        this.callPacketEvent(player, packet, null);
    }

    /**
     * Call packet event.
     *
     * @param player       the player
     * @param packet       the packet
     * @param errorHandler the error handler
     */
    public void callPacketEvent(@NotNull HACPlayer player, @NotNull WrappedPacket packet, @Nullable BiConsumer<? super Void, ? super Throwable> errorHandler) {
        player.runTaskASync(() -> this.packetEvents.stream()
                .filter(e -> e.getWrappedClass().equals(packet.getClass()))
                .filter(e -> !e.update(player, packet))
                .findFirst()
                .ifPresent(e -> e.onStop(player, packet)), errorHandler);
    }

    /**
     * Call event.
     *
     * @param event the event
     */
    public void callEvent(@NotNull Event event) {
        HACPlayer player = HACAPI.getInstance().getHacPlayerList().getPlayer(event.getPlayer().getUniqueId());

        player.runTaskASync(() -> this.events.stream()
                .filter(e -> e.getEventClass().equals(event.getClass()))
                .forEach(e -> e.run(player, event)), null);
    }

    /**
     * Gets current packet events.
     *
     * @return the current packet events
     */
    public List<AbstractPacketEventExecutor<?>> getCurrentPacketEvents() {
        return this.packetEvents;
    }
}
