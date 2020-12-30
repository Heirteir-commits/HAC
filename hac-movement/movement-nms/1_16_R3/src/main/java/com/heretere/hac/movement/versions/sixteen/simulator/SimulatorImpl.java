package com.heretere.hac.movement.versions.sixteen.simulator;

import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import com.heretere.hac.movement.simulator.SimulationPoint;
import com.heretere.hac.movement.simulator.Simulator;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.SoundEffect;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.jetbrains.annotations.NotNull;

public final class SimulatorImpl extends EntityHuman implements Simulator {
    private static final float DRAG = 0.98F;
    private static final int SNEAKING_FLAG = 1;
    private static final int SPRINTING_FLAG = 3;
    private static final int ELYTRA_FLAG = 7;

    private final @NotNull HACPlayer player;
    private final @NotNull PlayerData playerData;

    private float strafe;
    private float forward;

    public SimulatorImpl(final @NotNull HACPlayer player) {
        super(
            ((CraftWorld) player.getBukkitPlayer().orElseThrow(IllegalArgumentException::new).getWorld()).getHandle(),
            new BlockPosition(0, 0, 0),
            0,
            new GameProfile(null, "HAC-Simulator")
        );

        this.player = player;
        this.playerData = this.player.getDataManager()
                                     .getData(PlayerData.class)
                                     .orElseThrow(IllegalArgumentException::new);

        this.justCreated = false;
        this.abilities.isInvulnerable = true;
    }

    @Override public boolean isSpectator() {
        return false;
    }

    @Override public boolean isCreative() {
        return false;
    }

    private void updateCurrentData(final @NotNull SimulationPoint input) {
        this.world = ((CraftWorld) this.player.getBukkitPlayer()
                                              .orElseThrow(IllegalArgumentException::new).getWorld()).getHandle();

        this.setPositionRaw(input.getLocation().getX(), input.getLocation().getY(), input.getLocation().getZ());
        this.setYawPitch(input.getDirection().getX(), input.getDirection().getY());
        this.setMot(input.getMotion().getX(), input.getMotion().getY(), input.getMotion().getZ());

        this.onGround = input.isOnGround();

        this.fallDistance = input.getFallDistance();
        this.aE = input.getJumpSpeedFactor();

        this.hacSetFlag(SNEAKING_FLAG, this.playerData.getCurrent().isSneaking());
        this.hacSetFlag(SPRINTING_FLAG, this.playerData.getCurrent().isSprinting());
        this.hacSetFlag(ELYTRA_FLAG, this.playerData.getCurrent().isElytraFlying());

        this.aR = input.getStrafe();
        this.aT = input.getForward();

        this.strafe = input.getStrafe();
        this.forward = input.getForward();
        this.jumping = input.isJumping();
    }

    private void hacSetFlag(
        final int i,
        final boolean flag
    ) {
        super.setFlag(i, flag);
    }

    @Override public void setFlag(
        final int i,
        final boolean flag
    ) {
        //Stops the entity human class from updating flags during simulation.
    }

    @Override public @NotNull SimulationPoint processTick(final @NotNull SimulationPoint input) {
        this.updateCurrentData(input);

        this.tick();

        SimulationPoint output = input.copy();
        output.getLocation().set(this.locX(), this.locY(), this.locZ());
        output.getDirection().set(this.yaw, this.pitch);
        output.getMotion().set(this.getMot().getX(), this.getMot().getY(), this.getMot().getZ());
        output.getVelocity().set(
            output.getLocation().getX() - input.getLocation().getX(),
            output.getLocation().getY() - input.getLocation().getY(),
            output.getLocation().getZ() - input.getLocation().getZ()
        );
        output.setOnGround(this.onGround);
        output.setFallDistance(this.fallDistance);
        output.setJumpSpeedFactor(this.aE);
        return output;
    }

    @Override public void checkMovement(
        final double d0,
        final double d1,
        final double d2
    ) {
        //Unneeded method for simulator
    }

    @Override public void g(final Vec3D vec3d) {
        super.g(new Vec3D(this.strafe * DRAG, vec3d.y, this.forward * DRAG));
    }

    @Override protected void collideNearby() {
        //Disable collision calculations
    }

    @Override public void collide(final Entity entity) {
        //Disable collisions
    }

    @Override public void playSound(
        final SoundEffect soundeffect,
        final float f,
        final float f1
    ) {
        //Stop sound effects
    }


}
