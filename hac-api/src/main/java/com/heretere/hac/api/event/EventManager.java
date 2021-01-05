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
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.util.annotation.UniqueIdentifier;
import com.heretere.hac.api.util.generics.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public final class EventManager {
    private final Map<Class<? extends WrappedPacket>, ImmutablePair<EventHandler<?>, EventHandler<?>>>
        handlers;

    public EventManager() {
        this.handlers = Maps.newIdentityHashMap();
    }

    public <T extends WrappedPacket> void registerPacketEventExecutor(
        final @NotNull EventExecutor<T> executor
    ) {
        if (!Optional.ofNullable(executor.getClass().getAnnotation(UniqueIdentifier.class)).isPresent()) {
            throw new InvalidExecutorException(String.format(
                "Please add a %s annotation to the executor.",
                UniqueIdentifier.class
            ));
        }

        if (!Optional.ofNullable(executor.getClass().getAnnotation(Priority.class)).isPresent()) {
            throw new InvalidExecutorException(String.format(
                "Please add a %s annotation to the executor.",
                Priority.class
            ));
        }

        Optional<SyncState> syncState = Optional.ofNullable(executor.getClass().getAnnotation(SyncState.class));

        if (syncState.isPresent() && syncState.get().value() == SyncState.State.SYNCHRONOUS) {
            this.getSyncPacketEventHandler(executor.getGenericType()).addExecutor(executor);
        } else {
            this.getAsyncPacketEventHandler(executor.getGenericType()).addExecutor(executor);
        }
    }

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

    private @NotNull EventHandler<?> getSyncPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> clazz
    ) {
        return this.getEventHandlers(clazz).getA();
    }

    private @NotNull EventHandler<?> getAsyncPacketEventHandler(
        final @NotNull Class<? extends WrappedPacket> clazz
    ) {
        return this.getEventHandlers(clazz).getB();
    }

    private @NotNull ImmutablePair<EventHandler<?>, EventHandler<?>> getEventHandlers(
        final @NotNull Class<? extends WrappedPacket> packetClass
    ) {
        return this.handlers.computeIfAbsent(
            packetClass, clazz ->
                new ImmutablePair<>(new EventHandler<>(clazz), new EventHandler<>(clazz))
        );
    }

    public <T extends WrappedPacket> void callPacketEvent(
        final @NotNull HACPlayer player,
        final @NotNull T packet
    ) {
        ImmutablePair<EventHandler<?>, EventHandler<?>> syncAsyncPair =
            this.getEventHandlers(packet.getClass());

        if (!syncAsyncPair.getB().isEmpty()) {
            player.getFutureChain()
                  .addAsyncTask(() -> syncAsyncPair.getB().execute(player, packet));
        }

        if (!syncAsyncPair.getA().isEmpty()) {
            player.getFutureChain()
                  .addServerMainThreadTask(() -> syncAsyncPair.getA().execute(player, packet));
        }
    }


}
