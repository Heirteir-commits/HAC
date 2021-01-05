/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.api.player.factory;

import com.google.common.collect.ImmutableSet;
import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.event.EventExecutor;
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
    private final @NotNull ImmutableSet<EventExecutor<?>> events;

    /**
     * You can include a vararg set of {@link EventExecutor} these will be
     * registered for you from {@link DataFactory#registerUpdaters()}.
     *
     * @param api    The HACAPI reference
     * @param events The instances of {@link EventExecutor}
     */
    protected DataFactory(
        final @NotNull HACAPI api,
        final @NotNull EventExecutor<?>... events
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
     * {@link DataFactory#DataFactory(HACAPI, EventExecutor[])}.
     */
    public void registerUpdaters() {
        this.events.forEach(this.api.getEventManager()::registerPacketEventExecutor);
    }

    /**
     * unregisters updaters from the supplied array in
     * {@link DataFactory#DataFactory(HACAPI, EventExecutor[])}.
     */
    public void unregisterUpdaters() {
        this.events.forEach(this.api.getEventManager()::unregisterPacketEventExecutor);
    }

    /**
     * Gets an {@link ImmutableSet} the {@link EventExecutor} linked to this builder.
     *
     * @return An {@link ImmutableSet} of the events linked by this builder.
     */
    public @NotNull ImmutableSet<EventExecutor<?>> getEvents() {
        return this.events;
    }
}
