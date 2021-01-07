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

package com.heretere.hac.api.event.executor;

import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.api.util.generics.TypeDefinition;
import org.jetbrains.annotations.NotNull;

/**
 * This is the most basic Event Executor it simply runs based on the passed in event with no conditions.
 * <p>
 * * If you need an event executor to conditionally run use {@link DynamicStateEventExecutor}.
 * * If you need an event executor to stop all other event executors use {@link StoppableEventExecutor}.
 *
 * @param <T> The {@link WrappedPacket} type this event executor handles.
 */
public interface EventExecutor<T extends WrappedPacket> extends TypeDefinition<T> {
    /**
     * This is called when the relevant {@link WrappedPacket} is passed in to the
     * {@link com.heretere.hac.api.event.EventManager}.
     *
     * @param player The {@link HACPlayer} related to this event.
     * @param packet The {@link WrappedPacket} related to this event.
     * @return Should always be true. Return is disregarded unless it's an instance of {@link StoppableEventExecutor}.
     */
    boolean execute(
        @NotNull HACPlayer player,
        @NotNull T packet
    );
}
