package com.heretere.hac.movement.player.data;

import com.flowpowered.math.vector.Vector3f;
import lombok.Data;

@Data
public final class SimulatorData {
    private boolean onGround;
    private Vector3f location;
    private Vector3f motion;
    private Vector3f velocity;
    private float fallDistance;
    private float jumpMovementFactor;
    private boolean inWeb;
    private Object motOffset;

    public void apply(SimulatorData data) {
        this.onGround = data.isOnGround();
        this.velocity = data.getVelocity();
        this.location = data.getLocation();
        this.motion = data.getMotion();
        this.fallDistance = data.getFallDistance();
        this.jumpMovementFactor = data.getJumpMovementFactor();
        this.inWeb = data.isInWeb();
        this.motOffset = data.motOffset;
    }
}