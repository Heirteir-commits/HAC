package com.heirteir.hac.api.player.builder;

import com.google.common.collect.ImmutableSet;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.events.AbstractPacketEvent;
import com.heirteir.hac.api.player.HACPlayer;
import lombok.Getter;

public abstract class AbstractDataBuilder<T> {
    @Getter
    private final ImmutableSet<AbstractPacketEvent<?>> events;

    protected AbstractDataBuilder(AbstractPacketEvent<?>... events) {
        this.events = ImmutableSet.copyOf(events);
    }

    public abstract T build(HACPlayer player);

    public final void registerUpdaters() {
        this.events.forEach(event -> API.INSTANCE.getEventManager().addEvent(event));
    }

    public final void removeUpdaters() {
        this.events.forEach(event -> API.INSTANCE.getEventManager().removeEvent(event));
    }
}
