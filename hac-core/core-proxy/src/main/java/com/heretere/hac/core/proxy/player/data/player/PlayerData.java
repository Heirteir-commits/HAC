package com.heretere.hac.core.proxy.player.data.player;

import com.heretere.hac.api.events.packets.wrapper.clientside.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles basic player information for things such as position and velocity along with various player states.
 */
public final class PlayerData {
    private final Data current;
    private final Data previous;

    /**
     * Instantiates a new Player data.
     *
     * @param player the player
     */
    protected PlayerData(@NotNull HACPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        this.current = new Data(bukkitPlayer);
        this.previous = new Data(bukkitPlayer);
    }

    /**
     * Update the player's information based on the latest FlyingPacket received.
     *
     * @param flyingPacket the flying packet
     */
    public void update(@NotNull FlyingPacket flyingPacket) {
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
        private final MutableVector3F location;
        private final MutableVector3F velocity;
        private final MutableVector2F direction;

        private boolean onGround;
        private boolean sneaking;
        private boolean sprinting;
        private boolean elytraFlying;
        private boolean flying;

        public Data(@NotNull Player player) {
            this.location = new MutableVector3F(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ()
            );

            this.velocity = new MutableVector3F(0, 0, 0);

            this.direction = new MutableVector2F(
                    player.getLocation().getYaw(),
                    player.getLocation().getPitch()
            );

            this.onGround = true;
            this.sneaking = player.isSneaking();
            this.sprinting = player.isSprinting();
            this.elytraFlying = false;
            this.flying = player.isFlying();
        }

        public void apply(@NotNull Data other) {
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
        public void setLocation(double x, double y, double z) {
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
        public void setVelocity(double dx, double dy, double dz) {
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
        public void setDirection(double yaw, double pitch) {
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
        public void setOnGround(boolean onGround) {
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
        public void setSneaking(boolean sneaking) {
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
        public void setSprinting(boolean sprinting) {
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
        public void setElytraFlying(boolean elytraFlying) {
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
        public void setFlying(boolean flying) {
            this.flying = flying;
        }
    }

}
