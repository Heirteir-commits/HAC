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

package com.heretere.hac.api.event;

import com.google.common.collect.Maps;
import com.heretere.hac.api.event.annotation.Priority;
import com.heretere.hac.api.event.annotation.SyncState;
import com.heretere.hac.api.event.exception.InvalidExecutorException;
import com.heretere.hac.api.event.executor.EventExecutor;
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.util.annotation.UniqueIdentifier;
import com.heretere.hac.api.util.generics.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * This class is responsible for calling events throughout HAC.
 * {@link EventExecutor} types are registered to their respective {@link WrappedPacket} type.
 */
public final class EventManager {
    /**
     * This stores the {@link EventHandler} for a wrapped packet.
     * The first value in the pair is the sync handler (these are ran through the Bukkit scheduler).
     * The second value in the pair is the async handler (these are ran in the HAC thread pool).
     */
    private final Map<Class<? extends WrappedPacket>, ImmutablePair<@NotNull EventHandler<?>, @NotNull EventHandler<?>>>
        handlers;

    /**
     * Creates a new instance of the Event Manager. Typically this should only be called by
     * {@link com.heretere.hac.api.HACAPI}.
     */
    public EventManager() {
        /* Using a IdentityHashMap because we only need reference equality since we are only using class types as
        keys.*/
        this.handlers = Maps.newIdentityHashMap();
    }

    /**
     * Register an {@link EventExecutor}.
     *
     * @param executor The {@link EventExecutor}.
     * @param <T>      The {@link WrappedPacket} type the event executor handles.
     */
    public <T extends WrappedPacket> void registerPacketEventExecutor(
        final @NotNull EventExecutor<T> executor
    ) {
        /* All event executors need a unique id attached. This is used instead of #equals. */
        if (!Optional.ofNullable(executor.getClass().getAnnotation(UniqueIdentifier.class)).isPresent()) {
            throw new InvalidExecutorException(String.format(
                "Please add a %s annotation to the executor. (%s)",
                UniqueIdentifier.class,
                executor.getClass()
            ));
        }

        /* All event executors need a priority attached. So the event handler knows what order to run it in. */
        if (!Optional.ofNullable(executor.getClass().getAnnotation(Priority.class)).isPresent()) {
            throw new InvalidExecutorException(String.format(
                "Please add a %s annotation to the executor. (%s)",
                Priority.class,
                executor.getClass()
            ));
        }

        /* SyncState just defines if an executor should be ran on the server thread or a separate thread. */
        /* If the annotation isn't present, async is the assumed state. */
        Optional<SyncState> syncState = Optional.ofNullable(executor.getClass().getAnnotation(SyncState.class));

        if (syncState.isPresent() && syncState.get().value() == SyncState.State.SYNCHRONOUS) {
            this.getSyncPacketEventHandler(executor.getGenericType()).addExecutor(executor);
        } else {
            this.getAsyncPacketEventHandler(executor.getGenericType()).addExecutor(executor);
        }
    }

    /**
     * Unregister a {@link EventExecutor}.
     *
     * @param executor The {@link EventExecutor}.
     * @param <T>      The {@link WrappedPacket} type the event executor handles.
     */
    public <T extends WrappedPacket> void unregisterPacketEventExecutor(
        final @NotNull EventExecutor<T> executor
    ) {
        Optional<SyncState> syncState = Optional.ofNullable(executor.getClass().getAnnotation(SyncState.class));

        if (syncState.isPresent() && syncState.get().value() == SyncState.State.SYNCHRONOUS) {
            this.getSyncPacketEventHandler(executor.getGenericType()).removeExecutor(executor);
        } else {
            this.getAsyncPacketEventHandler(executor.getGenericType()).removeExecutor(executor);
        }
    }

    /**
     * Gets the first value in the pair from {@link EventManager#getEventHandlers(Class)}.
     *
     * @param packetClass the {@link WrappedPacket} type.
     * @return The sync event handler for this {@link WrappedPacket} type.
     */
    private @NotNull EventHandler<?> getSyncPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.getEventHandlers(packetClass).getA();
    }

    /**
     * Gets the second value in the pair from {@link EventManager#getEventHandlers(Class)}.
     *
     * @param packetClass the {@link WrappedPacket} type.
     * @return The async event handler for this {@link WrappedPacket} type.
     */
    private @NotNull EventHandler<?> getAsyncPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.getEventHandlers(packetClass).getB();
    }

    /**
     * Gets the {@link ImmutablePair} for this {@link WrappedPacket} type.
     * If it does not exist it creates one.
     *
     * @param packetClass the {@link WrappedPacket} type.
     * @return The {@link ImmutablePair} for this {@link WrappedPacket} type.
     */
    private @NotNull ImmutablePair<@NotNull EventHandler<?>, @NotNull EventHandler<?>> getEventHandlers(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.handlers.computeIfAbsent(
            packetClass, clazz ->
                new ImmutablePair<>(new EventHandler<>(clazz), new EventHandler<>(clazz))
        );
    }

    /**
     * @param player        The HACPlayer that caused this wrappedPacket.
     * @param wrappedPacket The {@link WrappedPacket} for this event.
     * @param <T>           The {@link WrappedPacket} type.
     */
    public <T extends WrappedPacket> void callPacketEvent(
        final @NotNull HACPlayer player,
        final @NotNull T wrappedPacket
    ) {
        /* Retrieves the event handlers for this wrapped packet. */
        ImmutablePair<EventHandler<?>, EventHandler<?>> syncAsyncPair =
            this.getEventHandlers(wrappedPacket.getClass());


        /* We run the async event handler first, because most data updaters are ran async. */
        if (syncAsyncPair.getB().isNotEmpty()) { //Don't bother passing the event chain if there are no events to run.
            player.getFutureChain()
                  .addAsyncTask(() -> syncAsyncPair.getB().runEventExecutors(player, wrappedPacket));
        }

        /* Run the sync event handler */
        if (syncAsyncPair.getA().isNotEmpty()) { //Don't bother passing the event chain if there are no events to run.
            player.getFutureChain()
                  .addServerMainThreadTask(() -> syncAsyncPair.getA().runEventExecutors(player, wrappedPacket));
        }
    }


}
