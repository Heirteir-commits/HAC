package com.heirteir.hac.api.events;

import com.google.common.collect.Maps;
import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class ASyncPacketEventManager {
    private final Map<Class<? extends WrappedPacket>, List<AbstractPacketEvent<?>>> events;

    public ASyncPacketEventManager() {
        this.events = Maps.newHashMap();
    }

    public void addEvent(@NotNull AbstractPacketEvent<?> event) {
        this.getEvents(event.getWrappedClass()).add(event);
    }

    public void removeEvent(@NotNull AbstractPacketEvent<?> event) {
        this.getEvents(event.getWrappedClass()).remove(event);
    }

    private List<AbstractPacketEvent<?>> getEvents(Class<? extends WrappedPacket> clazz) {
        return this.events.computeIfAbsent(clazz, t -> new ArrayList<AbstractPacketEvent<?>>() {
            @Override
            public boolean add(AbstractPacketEvent<?> abstractPacketEvent) {
                boolean add = super.add(abstractPacketEvent);
                this.sort(Comparator.comparingInt(event -> event.getPriority().ordinal()));
                return add;
            }
        });
    }

    public void run(@NotNull HACPlayer player, @NotNull Class<? extends WrappedPacket> type, @NotNull WrappedPacket packet) {
        this.getEvents(type).stream()
                .filter(event -> !event.update(player, packet))
                .findFirst()
                .ifPresent(event -> event.onStop(player, packet));
    }

    public List<AbstractPacketEvent<?>> getCurrentEvents() {
        return this.events.values().stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(event -> event.getPriority().ordinal()))
                .collect(Collectors.toList());
    }
}
