package com.heretere.hac.api.player.factory;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.AbstractPacketEventExecutor;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to build dynamic player data at runtime. After created an instance it needs to be registered
 * by using {@link com.heretere.hac.api.player.HACPlayerFactory#registerDataBuilder(Class, AbstractDataFactory)}.
 * You can get the {@link com.heretere.hac.api.player.HACPlayerFactory} from
 * {@link com.heretere.hac.api.player.HACPlayerList#getFactory()}.
 *
 * @param <T> The Data class that this builder creates.
 */
public abstract class AbstractDataFactory<T> {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;

    /**
     * These are automatically registered and unregistered by {@link com.heretere.hac.api.player.HACPlayerFactory}.
     */
    private final @NotNull ImmutableSet<AbstractPacketEventExecutor<?>> events;

    /**
     * You can include a vararg set of {@link AbstractPacketEventExecutor} these will be
     * registered for you from {@link AbstractDataFactory#registerUpdaters()}.
     *
     * @param api    The HACAPI reference
     * @param events The instances of {@link AbstractPacketEventExecutor}
     */
    protected AbstractDataFactory(
        final @NotNull HACAPI api,
        final @NotNull AbstractPacketEventExecutor<?>... events
    ) {
        this.api = api;
        this.events = ImmutableSet.copyOf(events);
    }


    /**
     * Use to build t data object from a HACPlayer instance.
     *
     * @param player The HACPlayer instance.
     * @return A built t data object.
     */
    public abstract @NotNull T build(@NotNull HACPlayer player);


    /**
     * Registered updaters from the supplied array in
     * {@link AbstractDataFactory#AbstractDataFactory(HACAPI, AbstractPacketEventExecutor[])}.
     */
    public void registerUpdaters() {
        this.events.forEach(this.api.getEventManager()::registerPacketEventExecutor);
    }

    /**
     * unregisters updaters from the supplied array in
     * {@link AbstractDataFactory#AbstractDataFactory(HACAPI, AbstractPacketEventExecutor[])}.
     */
    public void unregisterUpdaters() {
        this.events.forEach(this.api.getEventManager()::unregisterPacketEventExecutor);
    }

    /**
     * Gets an {@link ImmutableSet} the {@link AbstractPacketEventExecutor} linked to this builder.
     *
     * @return An {@link ImmutableSet} of the events linked by this builder.
     */
    public @NotNull ImmutableSet<AbstractPacketEventExecutor<?>> getEvents() {
        return this.events;
    }
}
