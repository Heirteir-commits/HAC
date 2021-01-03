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

package com.heretere.hac.api.event.packet.wrapper.clientside;

import com.heretere.hac.api.event.packet.wrapper.WrappedPacketIn;
import org.jetbrains.annotations.NotNull;

/**
 * This is the wrapped version of the PacketPlayInEntityActionPacket.
 */
public final class EntityActionPacket implements WrappedPacketIn {
    /**
     * The action of this packet.
     */
    private final @NotNull Action action;

    /**
     * Instantiates a new Entity action packet.
     *
     * @param action The action
     */
    public EntityActionPacket(final @NotNull Action action) {
        this.action = action;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public @NotNull Action getAction() {
        return this.action;
    }

    /**
     * This maps the Action enum from PacketPlayInEntityAction.
     */
    public enum Action {
        /**
         * Start sneaking action.
         */
        START_SNEAKING,
        /**
         * Stop sneaking action.
         */
        STOP_SNEAKING,
        /**
         * Start sprinting action.
         */
        START_SPRINTING,
        /**
         * Stop sprinting action.
         */
        STOP_SPRINTING,
        /**
         * Start flying with Elytra.
         */
        START_FALL_FLYING,
        /**
         * This is passed if the passed in action wasn't any of the above.
         */
        INVALID
    }
}
