package com.heirteir.hac.api.events;

import com.google.common.collect.Lists;
import com.heirteir.hac.api.events.packets.wrapper.WrappedPacket;
import com.heirteir.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ASyncPacketEventManager {
    private final List<AbstractPacketEvent<?>> events;

    public ASyncPacketEventManager() {
        this.events = Lists.newArrayList();
    }

    public void addEvent(@NotNull AbstractPacketEvent<?> event) {
        this.events.add(event);
    }

    public void removeEvent(@NotNull AbstractPacketEvent<?> event) {
        this.events.remove(event);
    }

    public void run(@NotNull HACPlayer player, @NotNull WrappedPacket packet) {
        this.events.stream()
                .filter(event -> event.getWrappedClass().equals(packet.getClass()))
                .filter(event -> !event.update(player, packet))
                .findFirst()
                .ifPresent(event -> event.onStop(player, packet));
    }

    public List<AbstractPacketEvent<?>> getCurrentEvents() {
        return this.events;
    }
}
