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

package com.heretere.hac.api.packet.wrapper.clientside;


import com.heretere.hac.api.packet.wrapper.WrappedPacketIn;

/**
 * This is the wrapped version of the PacketPlayInFlyingPacket.
 */
public final class FlyingPacket implements WrappedPacketIn {
    /**
     * The x location of the packet.
     */
    private final double x;
    /**
     * The y location of the packet.
     */
    private final double y;
    /**
     * The z location of the packet.
     */
    private final double z;
    /**
     * The yaw of the packet.
     */
    private final float yaw;
    /**
     * The pitch of the packet.
     */
    private final float pitch;

    /**
     * if that packet is on the ground.
     */
    private final boolean onGround;

    /**
     * Instantiates a new Flying packet.
     *
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param yaw      the yaw
     * @param pitch    the pitch
     * @param onGround the on ground
     */
    public FlyingPacket(
        final double x,
        final double y,
        final double z,
        final float yaw,
        final float pitch,
        final boolean onGround
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Gets yaw.
     *
     * @return the yaw
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Gets pitch.
     *
     * @return the pitch
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Whether or not the packet says the player is on the ground.
     *
     * @return the boolean
     */
    public boolean isOnGround() {
        return this.onGround;
    }
}

