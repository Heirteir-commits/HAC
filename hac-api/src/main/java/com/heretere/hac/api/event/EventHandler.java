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
import com.heretere.hac.api.event.executor.DynamicStateEventExecutor;
import com.heretere.hac.api.event.executor.EventExecutor;
import com.heretere.hac.api.event.executor.StoppableEventExecutor;
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.util.annotation.UniqueIdentifier;
import com.heretere.hac.api.util.generics.TypeDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Set;

/**
 * This class is responsible for storing and executing all the event executors for a {@link WrappedPacket} type.
 *
 * @param <T> The wrapped packet this class handles for.
 */
final class EventHandler<T extends WrappedPacket> implements TypeDefinition<T> {
    /**
     * This is used for equality instead of #equals
     * <p>
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

    /**
     * The {@link WrappedPacket} type this event handler processes event executors for.
     */
    private final @NotNull Class<T> type;

    /**
     * The event executors for this event handler.
     */
    private @NotNull Set<@NotNull EventExecutor<T>> executors;

    EventHandler(final @NotNull Class<T> type) {
        this.type = type;
        /* ImmutableSortedSet's are backed by an array for faster iteration. They also allow us
         * to use equality with EventHandler.COMPARATOR instead of using #equals. */
        this.executors = ImmutableSortedSet.of();
    }

    /**
     * Loops through all the {@link EventExecutor}s.
     *
     * @param player The {@link HACPlayer} that caused this event.
     * @param packet the {@link WrappedPacket} for this event.
     */
    void runEventExecutors(
        final @NotNull HACPlayer player,
        final @NotNull WrappedPacket packet
    ) {
        /* Loop through all the event executors in this event handler. */
        for (EventExecutor<T> executor : this.executors) {
            /* This makes sure that the passed in wrapped packet matches the generic type of this packet handler. */
            T wrappedPacket = this.getGenericType().cast(packet);

            /* This checks if this event executor should be ran.
             * If it's of the type DynamicStateEventExecutor that means there are conditions for it to run. */
            boolean shouldRun = !(executor instanceof DynamicStateEventExecutor)
                || ((DynamicStateEventExecutor<T>) executor).canRun(player, wrappedPacket);

            /* This checks if the loop should be stopped.
             * Only StoppableEventExecutors can stop the loop. */
            boolean stopLoop = shouldRun
                && !executor.execute(player, wrappedPacket)
                && executor instanceof StoppableEventExecutor;

            /* This stops running the loop if a StoppableEventExecutor tells the loop to stop executing. */
            if (stopLoop) {
                ((StoppableEventExecutor<T>) executor).onStop(player, wrappedPacket);
                break;
            }
        }
    }

    /**
     * This makes sure that any passed in event executor is of the correct type.
     *
     * @param executor The {@link EventExecutor} that needs to be checked.
     */
    private void checkExecutor(final @NotNull EventExecutor<?> executor) {
        if (executor.getGenericType() != this.getGenericType()) {
            throw new IllegalArgumentException("Invalid executor.");
        }
    }

    /**
     * Adds a new {@link EventExecutor} to this event handler.
     *
     * @param executor The {@link EventExecutor} to add to this event handler.
     */
    @SuppressWarnings("unchecked") //The type is checked by #checkExecutor
    public void addExecutor(final @NotNull EventExecutor<?> executor) {
        this.checkExecutor(executor);

        Set<EventExecutor<T>> set = this.tempTreeSet();
        set.add((EventExecutor<T>) executor);

        this.executors = ImmutableSortedSet.copyOf(EventHandler.COMPARATOR, set);
    }

    /**
     * Removes a {@link EventExecutor} from this event handler.
     *
     * @param executor The {@link EventHandler} to remove from this event handler.
     */
    public void removeExecutor(final @NotNull EventExecutor<?> executor) {
        this.checkExecutor(executor);

        Set<EventExecutor<T>> set = this.tempTreeSet();
        set.remove(executor);

        this.executors = ImmutableSortedSet.copyOf(EventHandler.COMPARATOR, set);
    }

    /**
     * A temporary tree set is created so we can add executors to the immutable sorted set.
     * We use a TreeSet because it allows us to use EventHandler.COMPARATOR for equality instead of #equals.
     * It's not the main collection type, because it's not backed by an array.
     *
     * @return A {@link java.util.TreeSet} of {@link EventHandler#executors}.
     */
    private @NotNull Set<EventExecutor<T>> tempTreeSet() {
        Set<65EventExecutor<T>> set = Sets.newTreeSet(EventHandler.COMPARATOR);
        set.addAll(this.executors);
        return set;
    }

    @Override public @NotNull Class<T> getGenericType() {
        return this.type;
    }

    /**
     * @return True if this event handler has no event executors attached.
     */
    public boolean isNotEmpty() {
        return !this.executors.isEmpty();
    }
}
