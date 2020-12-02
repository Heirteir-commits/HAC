package com.heretere.hac.api.player.builder;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.API;
import com.heretere.hac.api.events.types.packets.AbstractPacketEventExecutor;
import com.heretere.hac.api.player.HACPlayer;
import lombok.Getter;

public abstract class AbstractDataBuilder<T> {
    @Getter
    private final ImmutableSet<AbstractPacketEventExecutor<?>> events;

    protected AbstractDataBuilder(AbstractPacketEventExecutor<?>... events) {
        this.events = ImmutableSet.copyOf(events);
    }

    public abstract T build(HACPlayer player);

    public void registerUpdaters() {
        this.events.forEach(event -> API.INSTANCE.getEventManager().addPacketEvent(event));
    }

    public void removeUpdaters() {
        this.events.forEach(event -> API.INSTANCE.getEventManager().removePacketEvent(event));
    }
}
