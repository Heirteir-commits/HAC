package com.heretere.hac.api.events.types;

import com.heretere.hac.api.player.HACPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractEventExecutor<T extends Event> {
    private final Class<T> eventClass;
    private final Priority priority;

    public final void run(HACPlayer player, Object event) {
        this.run(player, this.eventClass.cast(event));
    }

    protected abstract void run(HACPlayer player, T event);
}
