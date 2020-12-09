package com.heretere.hac.api.player.builder;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.player.HACPlayerBuilder;
import com.heretere.hac.api.player.HACPlayerList;

/**
 * This class is used to build dynamic player data at runtime. After created an instance it needs to be registered
 * by using {@link HACPlayerBuilder#registerDataBuilder(Class, AbstractDataBuilder)}. You can get the
 * {@link HACPlayerBuilder} from {@link HACPlayerList#getBuilder()}.
 *
 * @param <T> The Data class that this builder creates.
 */
public abstract class AbstractDataBuilder<T> {
    private final ImmutableSet<AbstractPacketEventExecutor<?>> events;

    /**
     * You can include a vararg set of {@link AbstractPacketEventExecutor} these will be
     * registered for you from {@link AbstractDataBuilder#registerUpdaters()}.
     *
     * @param events The instances of {@link AbstractPacketEventExecutor}
     */
    protected AbstractDataBuilder(AbstractPacketEventExecutor<?>... events) {
        this.events = ImmutableSet.copyOf(events);
    }


    /**
     * Use to build t data object from a HACPlayer instance.
     *
     * @param player The HACPlayer instance.
     * @return A built t data object.
     */
    public abstract T build(HACPlayer player);


    /**
     * Registered updaters from the supplied array in {@link AbstractDataBuilder#AbstractDataBuilder(AbstractPacketEventExecutor[])}
     */
    public void registerUpdaters() {
        this.events.forEach(HACAPI.getInstance().getEventManager()::registerPacketEventExecutor);
    }

    /**
     * unregisters updaters from the supplied array in {@link AbstractDataBuilder#AbstractDataBuilder(AbstractPacketEventExecutor[])}
     */
    public void unregisterUpdaters() {
        this.events.forEach(HACAPI.getInstance().getEventManager()::unregisterPacketEventExecutor);
    }

    /**
     * Gets an {@link ImmutableSet} the {@link AbstractPacketEventExecutor} linked to this builder.
     *
     * @return An {@link ImmutableSet} of the events linked by this builder.
     */
    public ImmutableSet<AbstractPacketEventExecutor<?>> getEvents() {
        return this.events;
    }
}
