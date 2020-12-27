package com.heretere.hac.movement.proxy.simulation;

import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * The type Simulation data.
 */
public class SimulationData {
    /**
     * Location of the simulator.
     */
    private final MutableVector3F location;
    /**
     * Velocity of the simulator.
     */
    private final MutableVector3F velocity;
    /**
     * Motion applied to the next tick of the simulator.
     */
    private final MutableVector3F motion;

    private final MutableVector2F direction;

    /**
     * Current bukkit world of the simulator.
     */
    private World world;

    /**
     * If the simulator is located on the ground.
     */
    private boolean onGround;
    /**
     * Whether the simulator is sprinting.
     */
    private boolean sprinting;
    /**
     * Whether the simulator is sneaking.
     */
    private boolean sneaking;
    /**
     * Whether the simulator is flying.
     */
    private boolean flying;
    /**
     * Whether the simulator is flying with elytra.
     */
    private boolean elytraFlying;

    /**
     * The fall distance of the simulator.
     */
    private float fallDistance;
    /**
     * The speed factor of the simulator.
     */
    private float jumpSpeedFactor;

    private float forward;
    private float strafe;
    private boolean jumping;

    /**
     * Instantiates a new Simulation data.
     */
    public SimulationData() {
        this.location = new MutableVector3F(0, 0, 0);
        this.velocity = new MutableVector3F(0, 0, 0);
        this.motion = new MutableVector3F(0, 0, 0);
        this.direction = new MutableVector2F(0, 0);

        this.onGround = true;
    }

    /**
     * Apply.
     *
     * @param other the other
     */
    public void apply(final @NotNull SimulationData other) {
        this.world = other.world;
        this.location.set(other.location);
        this.velocity.set(other.velocity);
        this.motion.set(other.motion);
        this.direction.set(other.direction);

        this.onGround = other.onGround;
        this.sprinting = other.sprinting;
        this.sneaking = other.sneaking;
        this.flying = other.flying;
        this.elytraFlying = other.elytraFlying;

        this.fallDistance = other.fallDistance;
        this.jumpSpeedFactor = other.jumpSpeedFactor;

        this.forward = other.forward;
        this.strafe = other.strafe;
        this.jumping = other.jumping;
    }

    public void setDirection(
        final double yaw,
        final double pitch
    ) {
        this.direction.set(yaw, pitch);
    }

    public MutableVector2F getDirection() {
        return direction;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(final float forward) {
        this.forward = forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(final float strafe) {
        this.strafe = strafe;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(final boolean jumping) {
        this.jumping = jumping;
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
     * Gets motion.
     *
     * @return the motion
     */
    public MutableVector3F getMotion() {
        return this.motion;
    }

    /**
     * Sets motion.
     *
     * @param dx the dx
     * @param dy the dy
     * @param dz the dz
     */
    public void setMotion(
        final double dx,
        final double dy,
        final double dz
    ) {
        this.motion.set(dx, dy, dz);
    }

    /**
     * Gets world.
     *
     * @return the world
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Sets world.
     *
     * @param world the world
     */
    public void setWorld(final @NotNull World world) {
        this.world = world;
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
     * Gets fall distance.
     *
     * @return the fall distance
     */
    public float getFallDistance() {
        return this.fallDistance;
    }

    /**
     * Sets fall distance.
     *
     * @param fallDistance the fall distance
     */
    public void setFallDistance(final float fallDistance) {
        this.fallDistance = fallDistance;
    }

    /**
     * Gets jump speed factor.
     *
     * @return the jump speed factor
     */
    public float getJumpSpeedFactor() {
        return this.jumpSpeedFactor;
    }

    /**
     * Sets jump speed factor.
     *
     * @param jumpSpeedFactor the jump speed factor
     */
    public void setJumpSpeedFactor(final float jumpSpeedFactor) {
        this.jumpSpeedFactor = jumpSpeedFactor;
    }

    public SimulationData copy() {
        SimulationData data = new SimulationData();
        data.apply(this);
        return data;
    }
}
