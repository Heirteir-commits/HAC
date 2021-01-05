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

package com.heretere.hac.core.proxy.player;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3d;
import com.heretere.hac.api.packet.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles basic player information
 * for things such as position and velocity along with various player states.
 */
public final class PlayerData {
    /**
     * The current data object this player data represents.
     */
    private final @NotNull Data current;
    /**
     * The data from the previous tick.
     */
    private final @NotNull Data previous;

    /**
     * Instantiates a new Player data.
     *
     * @param player the player
     */
    public PlayerData(final @NotNull HACPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer().orElseThrow(IllegalStateException::new);

        this.current = new Data(bukkitPlayer);
        this.previous = new Data(bukkitPlayer);
    }

    /**
     * Update the player's information based on the latest FlyingPacket received.
     *
     * @param flyingPacket the flying packet
     */
    public void update(final @NotNull FlyingPacket flyingPacket) {
        this.previous.apply(this.current);

        this.current.setLocation(Vector3d.from(flyingPacket.getX(), flyingPacket.getY(), flyingPacket.getZ()));
        this.current.setVelocity(this.current.getLocation().sub(this.previous.getLocation()));
        this.current.setDirection(Vector2f.from((float) flyingPacket.getYaw(), (float) flyingPacket.getPitch()));

        this.current.setOnGround(flyingPacket.isOnGround());
    }

    /**
     * Gets most recent player data information.
     *
     * @return the current
     */
    public @NotNull Data getCurrent() {
        return this.current;
    }

    /**
     * Gets the previous player data information.
     *
     * @return the previous
     */
    public @NotNull Data getPrevious() {
        return this.previous;
    }

    /**
     * Data storage to allow us to create a current and previous data instance. These classes are mutable to avoid
     * creating a ton of instances since we will be processing a lot of flying packets.
     */
    public static final class Data {
        /* These vectors are mutable to avoid unneeded object creation at runtime. */

        /**
         * The player's location.
         */
        private @NotNull Vector3d location;
        /**
         * The player's velocity.
         */
        private @NotNull Vector3d velocity;
        /**
         * The player's direction yaw being x and pitch being y.
         */
        private @NotNull Vector2f direction;

        /**
         * Whether or not the player say's they're on the ground.
         */
        private boolean onGround;
        /**
         * The sneaking state of the player.
         */
        private boolean sneaking;
        /**
         * The sprinting state of the player.
         */
        private boolean sprinting;
        /**
         * If the player is flying with elytra.
         */
        private boolean elytraFlying;
        /**
         * If the player is flying.
         */
        private boolean flying;

        /**
         * Instantiates a new Data instance shouldn't be used more than once.
         *
         * @param player the bukkit player
         */
        public Data(final @NotNull Player player) {
            this.location = Vector3d.from(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()
            );

            this.velocity = Vector3d.ZERO;
            this.direction = Vector2f.from(player.getLocation().getYaw(), player.getLocation().getPitch());

            this.onGround = true;
            this.sneaking = player.isSneaking();
            this.sprinting = player.isSprinting();
            this.elytraFlying = false;
            this.flying = player.isFlying();
        }

        /**
         * Applies the data from another data object to this object.
         *
         * @param other Data
         */
        public void apply(final @NotNull Data other) {
            this.location = other.location;
            this.velocity = other.velocity;
            this.direction = other.direction;

            this.onGround = other.onGround;
            this.sneaking = other.sneaking;
            this.sprinting = other.sprinting;
            this.elytraFlying = other.elytraFlying;
            this.flying = other.flying;
        }


        /**
         * @return The player's location
         */
        public @NotNull Vector3d getLocation() {
            return this.location;
        }

        /**
         * @param location The player's location
         */
        public void setLocation(final @NotNull Vector3d location) {
            this.location = location;
        }

        /**
         * @return The player's velocity
         */
        public @NotNull Vector3d getVelocity() {
            return this.velocity;
        }

        /**
         * @param velocity The player's velocity
         */
        public void setVelocity(final @NotNull Vector3d velocity) {
            this.velocity = velocity;
        }


        /**
         * @return The player's yaw/pitch. (yaw x, pitch y)
         */
        public @NotNull Vector2f getDirection() {
            return this.direction;
        }

        /**
         * @param direction The player's yaw/pitch. (yaw x, pitch y)
         */
        public void setDirection(final @NotNull Vector2f direction) {
            this.direction = direction;
        }

        /**
         * Is on ground boolean.
         *
         * @return the boolean
         */
        public boolean isOnGround() {
            return this.onGround;
        }

        /**
         * Sets on ground.
         *
         * @param onGround the on ground
         */
        public void setOnGround(final boolean onGround) {
            this.onGround = onGround;
        }

        /**
         * Is sneaking boolean.
         *
         * @return the boolean
         */
        public boolean isSneaking() {
            return this.sneaking;
        }

        /**
         * Sets sneaking.
         *
         * @param sneaking the sneaking
         */
        public void setSneaking(final boolean sneaking) {
            this.sneaking = sneaking;
        }

        /**
         * Is sprinting boolean.
         *
         * @return the boolean
         */
        public boolean isSprinting() {
            return this.sprinting;
        }

        /**
         * Sets sprinting.
         *
         * @param sprinting the sprinting
         */
        public void setSprinting(final boolean sprinting) {
            this.sprinting = sprinting;
        }

        /**
         * Is elytra flying boolean.
         *
         * @return the boolean
         */
        public boolean isElytraFlying() {
            return this.elytraFlying;
        }

        /**
         * Sets elytra flying.
         *
         * @param elytraFlying the elytra flying
         */
        public void setElytraFlying(final boolean elytraFlying) {
            this.elytraFlying = elytraFlying;
        }

        /**
         * Is flying boolean.
         *
         * @return the boolean
         */
        public boolean isFlying() {
            return this.flying;
        }

        /**
         * Sets flying.
         *
         * @param flying the flying
         */
        public void setFlying(final boolean flying) {
            this.flying = flying;
        }
    }

}
