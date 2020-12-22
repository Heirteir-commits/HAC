package com.heretere.hac.core.proxy.player;

import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
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
    private final Data current;
    /**
     * The data from the previous tick.
     */
    private final Data previous;

    /**
     * Instantiates a new Player data.
     *
     * @param player the player
     */
    protected PlayerData(@NotNull final HACPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        this.current = new Data(bukkitPlayer);
        this.previous = new Data(bukkitPlayer);
    }

    /**
     * Update the player's information based on the latest FlyingPacket received.
     *
     * @param flyingPacket the flying packet
     */
    public void update(@NotNull final FlyingPacket flyingPacket) {
        this.previous.apply(current);

        this.current.getLocation().set(flyingPacket.getX(), flyingPacket.getY(), flyingPacket.getZ());

        MutableVector3F currentLocation = this.current.getLocation();
        MutableVector3F previousLocation = this.previous.getLocation();

        this.current.getVelocity().set(
            currentLocation.getX() - previousLocation.getX(),
            currentLocation.getY() - previousLocation.getY(),
            currentLocation.getZ() - previousLocation.getZ()
        );

        this.current.getDirection().set(flyingPacket.getYaw(), flyingPacket.getPitch());

        this.current.setOnGround(flyingPacket.isOnGround());
    }

    /**
     * Gets most recent player data information.
     *
     * @return the current
     */
    public Data getCurrent() {
        return this.current;
    }

    /**
     * Gets the previous player data information.
     *
     * @return the previous
     */
    public Data getPrevious() {
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
        private final MutableVector3F location;
        /**
         * The player's velocity.
         */
        private final MutableVector3F velocity;
        /**
         * The player's direction yaw being x and pitch being y.
         */
        private final MutableVector2F direction;

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
        public Data(@NotNull final Player player) {
            this.location = new MutableVector3F(
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()
            );

            this.velocity = new MutableVector3F(0, 0, 0);

            this.direction = new MutableVector2F(player.getLocation().getYaw(), player.getLocation().getPitch());

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
        public void apply(@NotNull final Data other) {
            this.location.set(other.location);
            this.velocity.set(other.velocity);
            this.direction.set(other.direction);

            this.onGround = other.onGround;
            this.sneaking = other.sneaking;
            this.sprinting = other.sprinting;
            this.elytraFlying = other.elytraFlying;
            this.flying = other.flying;
        }

        /**
         * Gets location.
         *
         * @return the location
         */
        public MutableVector3F getLocation() {
            return this.location;
        }

        /**
         * Sets location.
         *
         * @param x the x
         * @param y the y
         * @param z the z
         */
        public void setLocation(
            final double x,
            final double y,
            final double z
        ) {
            this.location.set(x, y, z);
        }

        /**
         * Gets velocity.
         *
         * @return the velocity
         */
        public MutableVector3F getVelocity() {
            return this.velocity;
        }

        /**
         * Sets velocity.
         *
         * @param dx the dx
         * @param dy the dy
         * @param dz the dz
         */
        public void setVelocity(
            final double dx,
            final double dy,
            final double dz
        ) {
            this.velocity.set(dx, dy, dz);
        }

        /**
         * Gets direction.
         *
         * @return the direction
         */
        public MutableVector2F getDirection() {
            return this.direction;
        }

        /**
         * Sets direction.
         *
         * @param yaw   the yaw
         * @param pitch the pitch
         */
        public void setDirection(
            final double yaw,
            final double pitch
        ) {
            this.direction.set(yaw, pitch);
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
