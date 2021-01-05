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

package com.heretere.hac.api.packet.factory;

import com.google.common.collect.Sets;
import com.heretere.hac.api.packet.wrapper.WrappedPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class PacketFactory<T extends WrappedPacket> {
    /**
     * The nms packet class that this factory handles.
     */
    private final @NotNull Set<Class<?>> nmsClasses;

    protected PacketFactory(
        final @NotNull Class<?> base,
        final @NotNull Class<?>... nmsClasses
    ) {
        //since were only storing classes identityHashSet is beneficial here for speed
        this.nmsClasses = Sets.newIdentityHashSet();
        this.nmsClasses.add(base);
        this.nmsClasses.addAll(Sets.newHashSet(nmsClasses));
    }

    /**
     * Creates a T wrapped packet instance based on the passed in NMS packet.
     *
     * @param player The HACPlayer
     * @param packet The NMS packet
     * @return A T wrapped packet
     */
    public abstract @NotNull T create(
        HACPlayer player,
        Object packet
    );

    /**
     * The T wrapped class this factory creates.
     *
     * @return T wrapped class
     */
    public abstract @NotNull Class<T> getWrappedClass();

    /**
     * Passes the IdentityHashSet to check if this factory handles a specific class.
     *
     * @return The IdentityHashSet
     */
    public final @NotNull Set<Class<?>> getPacketClasses() {
        return this.nmsClasses;
    }
}
