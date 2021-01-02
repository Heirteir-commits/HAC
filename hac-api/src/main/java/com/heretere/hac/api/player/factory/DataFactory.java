package com.heretere.hac.api.player.factory;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.events.PacketEventExecutor;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to build dynamic player data at runtime. After created an instance it needs to be registered
 * by using {@link com.heretere.hac.api.player.HACPlayerFactory#registerDataBuilder(Class, DataFactory)}.
 * You can get the {@link com.heretere.hac.api.player.HACPlayerFactory} from
 * {@link com.heretere.hac.api.player.HACPlayerList#getFactory()}.
 *
 * @param <T> The Data class that this builder creates.
 */
public abstract class DataFactory<T> {
    /**
     * The HACAPI reference.
     */
    private final @NotNull HACAPI api;

    /**
     * These are automatically registered and unregistered by {@link com.heretere.hac.api.player.HACPlayerFactory}.
     */
    private final @NotNull ImmutableSet<PacketEventExecutor<?>> events;

    /**
     * You can include a vararg set of {@link PacketEventExecutor} these will be
     * registered for you from {@link DataFactory#registerUpdaters()}.
     *
     * @param api    The HACAPI reference
     * @param events The instances of {@link PacketEventExecutor}
     */
    protected DataFactory(
        final @NotNull HACAPI api,
        final @NotNull PacketEventExecutor<?>... events
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
     * {@link DataFactory#DataFactory(HACAPI, PacketEventExecutor[])}.
     */
    public void registerUpdaters() {
        this.events.forEach(this.api.getEventManager()::registerPacketEventExecutor);
    }

    /**
     * unregisters updaters from the supplied array in
     * {@link DataFactory#DataFactory(HACAPI, PacketEventExecutor[])}.
     */
    public void unregisterUpdaters() {
        this.events.forEach(this.api.getEventManager()::unregisterPacketEventExecutor);
    }

    /**
     * Gets an {@link ImmutableSet} the {@link PacketEventExecutor} linked to this builder.
     *
     * @return An {@link ImmutableSet} of the events linked by this builder.
     */
    public @NotNull ImmutableSet<PacketEventExecutor<?>> getEvents() {
        return this.events;
    }
}
