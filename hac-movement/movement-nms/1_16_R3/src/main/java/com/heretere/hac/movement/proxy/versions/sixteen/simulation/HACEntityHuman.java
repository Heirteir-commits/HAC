package com.heretere.hac.movement.proxy.versions.sixteen.simulation;

import com.heretere.hac.core.util.math.vector.MutableVector3F;
import com.heretere.hac.movement.proxy.simulation.HumanAccessor;
import com.heretere.hac.movement.proxy.simulation.SimulationData;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class HACEntityHuman extends EntityHuman implements HumanAccessor {
    protected HACEntityHuman(org.bukkit.World world) {
        super(((CraftWorld) world).getHandle(), new BlockPosition(0, 0, 0), 0, new GameProfile(null, "HAC-Simulator"));
        this.setInvulnerable(true);
        this.justCreated = false;
    }

    @Override
    public SimulationData simulationTick(SimulationData input) {
        this.setPositionRaw(input.getLocation().getX(), input.getLocation().getY(), input.getLocation().getZ());
        this.setYawPitch(input.getDirection().getX(), input.getDirection().getY());
//        this.setMot(input.getMotion().getX(), input.getMotion().getY(), input.getMotion().getZ());

        this.fallDistance = input.getFallDistance();
        this.aE = input.getJumpSpeedFactor();

        this.setFlag(1, input.isSneaking());
        this.setFlag(3, input.isSprinting());
        this.setFlag(7, input.isElytraFlying());

        this.aR = input.getStrafe();
        this.aT = input.getForward();
        this.jumping = input.isJumping();

        System.out.println(new MutableVector3F(this.locX(), this.locY(), this.locZ()));
        this.tick();
        System.out.println(new MutableVector3F(this.locX(), this.locY(), this.locZ()));

        SimulationData output = new SimulationData();
        output.setLocation(this.locX(), this.locY(), this.locZ());
        output.setDirection(this.yaw, this.pitch);
        MutableVector3F velocity = output.getLocation().copy().subtract(input.getLocation());
        output.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
        output.setMotion(this.getMot().x, this.getMot().y, this.getMot().z);

        output.setOnGround(this.onGround);
        output.setFallDistance(this.fallDistance);
        output.setJumpSpeedFactor(this.aE);

        return output;
    }

    @Override public boolean isSpectator() {
        return false;
    }

    @Override public boolean isCreative() {
        return false;
    }

}
