package com.heirteir.hac.api.events;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public final class ASyncPacketEventManager {
    private final Map<Class<? extends WrappedPacket>, Set<AbstractPacketEvent<?>>> test;

    public ASyncPacketEventManager() {
        this.test = Maps.newHashMap();
    }

    public void addEvent(@NotNull AbstractPacketEvent<?> event) {
        this.getEvents(event.getWrappedClass()).add(event);
    }

    public void removeEvent(@NotNull AbstractPacketEvent<?> event) {
        this.getEvents(event.getWrappedClass()).remove(event);
    }

    private Set<AbstractPacketEvent<?>> getEvents(Class<? extends WrappedPacket> clazz) {
        return this.test.computeIfAbsent(clazz, t -> Sets.newTreeSet());
    }

    public void run(@NotNull HACPlayer player, @NotNull Class<? extends WrappedPacket> type, @NotNull WrappedPacket packet) {
        this.getEvents(type).stream()
                .filter(event -> !event.update(player, packet))
                .findFirst()
                .ifPresent(event -> event.onStop(player, packet));
    }
}
