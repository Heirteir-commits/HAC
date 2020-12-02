package com.heirteir.hac.api.player.builder;

import com.google.common.collect.Sets;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.events.AbstractPacketEvent;
import com.heirteir.hac.api.player.HACPlayer;

import java.util.Set;

public abstract class AbstractDataBuilder<T> {
    private final Set<AbstractPacketEvent<?>> events;

    protected AbstractDataBuilder(AbstractPacketEvent<?>... events) {
        this.events = Sets.newHashSet(events);
    }

    public abstract T build(HACPlayer player);

    public final void registerUpdaters() {
        this.events.forEach(event -> API.INSTANCE.getEventManager().addEvent(event));
    }

    public final void removeUpdaters() {
        this.events.forEach(event -> API.INSTANCE.getEventManager().removeEvent(event));
    }
}
