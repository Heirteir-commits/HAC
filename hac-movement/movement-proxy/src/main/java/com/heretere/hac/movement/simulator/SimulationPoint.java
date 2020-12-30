package com.heretere.hac.movement.simulator;

import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import org.jetbrains.annotations.NotNull;

public final class SimulationPoint {
    private final @NotNull MutableVector3F location;
    private final @NotNull MutableVector2F direction;

    private final @NotNull MutableVector3F velocity;
    private final @NotNull MutableVector3F motion;

    private boolean onGround;

    private float strafe;
    private float forward;
    private boolean jumping;

    private float fallDistance;
    private float jumpSpeedFactor;

    public SimulationPoint() {
        this.location = new MutableVector3F(0, 0, 0);
        this.direction = new MutableVector2F(0, 0);

        this.velocity = new MutableVector3F(0, 0, 0);
        this.motion = new MutableVector3F(0, 0, 0);
    }

    public void apply(final @NotNull SimulationPoint other) {
        this.location.set(other.location);
        this.direction.set(other.direction);
        this.velocity.set(other.velocity);
        this.motion.set(other.motion);

        this.onGround = other.onGround;

        this.strafe = other.strafe;
        this.forward = other.forward;
        this.jumping = other.jumping;

        this.fallDistance = other.fallDistance;
        this.jumpSpeedFactor = other.jumpSpeedFactor;
    }

    public @NotNull MutableVector3F getLocation() {
        return location;
    }

    public @NotNull MutableVector2F getDirection() {
        return direction;
    }

    public @NotNull MutableVector3F getVelocity() {
        return velocity;
    }

    public @NotNull MutableVector3F getMotion() {
        return motion;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public void setFallDistance(float fallDistance) {
        this.fallDistance = fallDistance;
    }

    public float getJumpSpeedFactor() {
        return jumpSpeedFactor;
    }

    public void setJumpSpeedFactor(float jumpSpeedFactor) {
        this.jumpSpeedFactor = jumpSpeedFactor;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public SimulationPoint copy() {
        SimulationPoint point = new SimulationPoint();
        point.apply(this);
        return point;
    }
}
