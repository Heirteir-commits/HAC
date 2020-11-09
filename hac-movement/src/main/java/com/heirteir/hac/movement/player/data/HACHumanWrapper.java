package com.heirteir.hac.movement.player.data;

import com.flowpowered.math.vector.Vector3f;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.player.data.location.PlayerData;
import com.heirteir.hac.movement.dynamic.entity.human.EntityHumanAccessor;
import lombok.Getter;

import java.util.Objects;

public final class HACHumanWrapper {
    private final PlayerData playerData;
    private final EntityHumanAccessor base;

    @Getter
    private final SimulatorData simulatorData;

    private final int strafe;
    private final int forward;
    private final boolean jump;

    public HACHumanWrapper(HACPlayer player, EntityHumanAccessor base, int strafe, int forward, int jump) {
        Objects.requireNonNull(base);
        this.playerData = player.getDataManager().getData(PlayerData.class);
        this.simulatorData = new SimulatorData();
        this.base = base;
        this.strafe = strafe;
        this.forward = forward;
        this.jump = jump == 1;
    }

    private void resetInputs() {
        this.base.setStrafe(this.strafe);
        this.base.setForward(this.forward);
        this.base.setJumping(this.jump);
    }

    private void updateEmulatorData() {
        Vector3f newLocation = new Vector3f(this.base.getLocX(), this.base.getLocY(), this.base.getLocZ());
        this.simulatorData.setVelocity(newLocation.sub(this.simulatorData.getLocation()));
        this.simulatorData.setLocation(newLocation);
        this.simulatorData.setMotion(new Vector3f(this.base.getMotX(), this.base.getMotY(), this.base.getMotZ()));
        this.simulatorData.setOnGround(this.base.getOnGround());
        this.simulatorData.setFallDistance(this.base.getFallDistance());
        this.simulatorData.setJumpMovementFactor(this.base.getJumpMovementFactor());
        this.simulatorData.setInWeb(this.base.getInWeb());
        this.simulatorData.setMotOffset(this.base.getMotOffset());
    }

    private void passEmulatorData() {
        this.base.setOnGround(this.simulatorData.isOnGround());
        this.base.setLocation(
                this.simulatorData.getLocation().getX(),
                this.simulatorData.getLocation().getY(),
                this.simulatorData.getLocation().getZ(),
                this.playerData.getCurrent().getDirection().getX(),
                this.playerData.getCurrent().getDirection().getY());
        this.base.setMotX(this.simulatorData.getMotion().getX());
        this.base.setMotY(this.simulatorData.getMotion().getY());
        this.base.setMotZ(this.simulatorData.getMotion().getZ());
        this.base.setFallDistance(this.simulatorData.getFallDistance());
        this.base.setJumpMovementFactor(this.simulatorData.getJumpMovementFactor());
        this.base.setInWeb(this.simulatorData.isInWeb());
        this.base.setMotOffset(this.simulatorData.getMotOffset());
    }

    private void updateModifiers() {
        this.base.setSprinting(this.playerData.getCurrent().isSprinting());
        this.base.setSneaking(this.playerData.getCurrent().isSneaking());
        //TODO: Add Fly Speed
        this.base.setFlying(this.playerData.getCurrent().isFlying());
        this.base.setInvulnerable(true);


        try {
            API.INSTANCE.getReflections().getNMSClass("EntityLiving").getFieldByName("bn").set(this.base, 0);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void update(SimulatorData simulatorData) {
        this.simulatorData.apply(simulatorData);
        this.passEmulatorData();
        this.resetInputs();
        this.updateModifiers();
        this.base.tick();
        this.updateEmulatorData();
    }
}
