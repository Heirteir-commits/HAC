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

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import com.heretere.hac.api.event.annotation.Priority;
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.util.annotation.UniqueIdentifier;
import com.heretere.hac.api.util.generics.TypeDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Set;

/**
 * This class is responsible for
 *
 * @param <T> The wrapped packet this class handles for.
 */
final class EventHandler<T extends WrappedPacket> implements TypeDefinition<T> {
    /**
     * A comparator that compares:
     * 1. {@link Priority#value()}
     * 2. if it's an instance of {@link StoppableEventExecutor} 1 otherwise 0.
     * 3. {@link UniqueIdentifier#value()}
     */
    private static final @NotNull Comparator<EventExecutor<?>> COMPARATOR =
        Comparator.<EventExecutor<?>>comparingInt(executor -> executor.getClass()
                                                                      .getAnnotation(Priority.class)
                                                                      .value())
            .thenComparingInt(executor -> executor instanceof StoppableEventExecutor ? 1 : 0)
            .thenComparing(executor -> executor.getClass().getAnnotation(UniqueIdentifier.class).value());

    private final @NotNull Class<T> type;
    private @NotNull Set<@NotNull EventExecutor<T>> executors;

    EventHandler(final @NotNull Class<T> type) {
        this.type = type;
        this.executors = ImmutableSortedSet.of();
    }

    void execute(
        final @NotNull HACPlayer player,
        final @NotNull WrappedPacket packet
    ) {
        if (packet.getClass() != this.getGenericType()) {
            throw new IllegalArgumentException("Invalid Packet Type.");
        }

        for (EventExecutor<T> executor : this.executors) {
            if (!executor.execute(
                player,
                this.getGenericType().cast(packet)
            ) && executor instanceof StoppableEventExecutor) {
                ((StoppableEventExecutor<T>) executor).onStop(player, this.getGenericType().cast(packet));
                break;
            }
        }
    }

    private void checkExecutor(final @NotNull EventExecutor<?> executor) {
        if (executor.getGenericType() != this.getGenericType()) {
            throw new IllegalArgumentException("Invalid executor.");
        }
    }

    @SuppressWarnings("unchecked") //The type is checked by checkExecutor
    public void addExecutor(final @NotNull EventExecutor<?> executor) {
        this.checkExecutor(executor);

        Set<EventExecutor<T>> set = this.tempTreeSet();
        set.add((EventExecutor<T>) executor);

        this.executors = ImmutableSortedSet.copyOf(EventHandler.COMPARATOR, set);
    }

    public void removeExecutor(final @NotNull EventExecutor<?> executor) {
        this.checkExecutor(executor);

        Set<EventExecutor<T>> set = this.tempTreeSet();
        set.remove(executor);

        this.executors = ImmutableSortedSet.copyOf(EventHandler.COMPARATOR, set);
    }

    private @NotNull Set<EventExecutor<T>> tempTreeSet() {
        Set<EventExecutor<T>> set = Sets.newTreeSet(EventHandler.COMPARATOR);
        set.addAll(this.executors);
        return set;
    }

    @Override public @NotNull Class<T> getGenericType() {
        return this.type;
    }

    public boolean isEmpty() {
        return this.executors.isEmpty();
    }
}
