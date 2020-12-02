package com.heirteir.hac.movement.player.data;

import com.flowpowered.math.vector.Vector3f;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.core.player.data.location.PlayerData;
import com.heirteir.hac.core.util.reflections.helper.PlayerHelper;
import com.heirteir.hac.movement.dynamic.entity.human.EntityHumanAccessor;
import lombok.Getter;

import java.util.Objects;

public final class HACHumanWrapper {
    private final HACPlayer player;
    private final PlayerData playerData;

    /* Accessors */
    private final EntityHumanAccessor base;

    @Getter
    private final SimulatorData simulatorData;

    private final int strafe;
    private final int forward;
    private final boolean jump;

    public HACHumanWrapper(HACPlayer player, EntityHumanAccessor base, Object foodMeta, int strafe, int forward, int jump) {
        this.player = player;
        this.playerData = player.getDataManager().getData(PlayerData.class);

        this.base = Objects.requireNonNull(base);
        this.base.setFoodData(foodMeta);
        this.base.setJustCreated(false);

        this.simulatorData = new SimulatorData();

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
        this.base.setWorld(API.INSTANCE.getReflections().getHelpers().getHelper(PlayerHelper.class).getWorld(this.player.getBukkitPlayer()));
        this.base.setFlySpeed(this.player.getBukkitPlayer().getFlySpeed());
        this.base.setFlying(this.playerData.getCurrent().isFlying());
        this.base.setInvulnerable(true);
        this.base.hacSetFlag(1, this.playerData.getCurrent().isSneaking());
        this.base.hacSetFlag(3, this.playerData.getCurrent().isSprinting());
        this.base.hacSetFlag(7, this.playerData.getCurrent().isElytraFlying());
        this.base.setJumpTicks(0);
    }

    private void updateFlying() {
        if (this.playerData.getCurrent().isFlying()) {
            if (this.playerData.getCurrent().isSneaking()) {
                this.base.setMotY(this.base.getFlySpeed() * -3F);
            }
            if (this.jump) {
                this.base.setMotY(this.base.getFlySpeed() * 3F);
            }
        }
    }

    public void update(SimulatorData simulatorData) {
        this.simulatorData.apply(simulatorData);
        this.passEmulatorData();
        this.resetInputs();
        this.updateModifiers();
        this.updateFlying();
        this.base.hacTick();
        this.updateEmulatorData();
    }
}
