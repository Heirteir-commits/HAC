package com.heretere.hac.movement.proxy.versions.sixteen.simulation;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.SoundCategory;
import net.minecraft.server.v1_16_R3.SoundEffect;
import net.minecraft.server.v1_16_R3.TagsFluid;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class HACEntityHuman extends EntityHuman {
    private static final double D_EPSILON = 0.003;

    public HACEntityHuman(org.bukkit.World world) {
        super(((CraftWorld) world).getHandle(), new BlockPosition(0, 0, 0), 0, new GameProfile(null, "HAC-Simulator"));
    }

    public HACEntityHuman(World world, BlockPosition blockposition, float f, GameProfile gameprofile) {
        super(world, blockposition, f, gameprofile);

        this.enderChest = null;
        this.activeContainer = null;
        this.foodData = null;

        this.abilities.isInvulnerable = true;

        super.tick();
    }

    @Override
    public void tick() {
        this.et(); //EntityHuman
        this.movementTick();
        this.eu(); //EntityHuman
    }

    @Override
    public void movementTick() {
        this.setMot(
                Math.abs(this.getMot().getX()) < D_EPSILON ? 0.0D : this.getMot().getX(),
                Math.abs(this.getMot().getY()) < D_EPSILON ? 0.0D : this.getMot().getY(),
                Math.abs(this.getMot().getZ()) < D_EPSILON ? 0.0D : this.getMot().getZ()
        );

        if (this.jumping) {
            double yOffset;
            if (this.isInLava()) {
                yOffset = this.b(TagsFluid.LAVA);
            } else {
                yOffset = this.b(TagsFluid.WATER);
            }

            boolean swimmingUp = this.isInWater() && yOffset > 0;
            double headHeight = this.cx();


            if (!swimmingUp || this.onGround && yOffset <= headHeight) {
                if (!this.isInLava() || this.onGround && yOffset <= headHeight) {
                    if (this.onGround || swimmingUp) {
                        this.jump();
                    }
                } else {
                    this.c(TagsFluid.LAVA);
                }
            } else {
                this.c(TagsFluid.WATER);
            }

            this.aR *= 0.98F; //move strafe
            this.aT *= 0.98F; //move forward

            this.g(new Vec3D(this.aR, 0, this.aT));
        }
    }

    @Override
    public void g(Vec3D vec3D) {

    }

    private void travel(Vec3D vec3D) {
        double x = this.locX();
        double y = this.locY();
        double z = this.locZ();
        double d3;

        if (this.isSwimming()) {
            d3 = this.getLookDirection().y;
            double d4 = d3 < -0.2D ? 0.085D : 0.06D;
            if (d3 <= 0.0D || this.jumping || !this.world.getType(new BlockPosition(this.locX(), this.locY() + 1.0D - 0.1D, this.locZ())).getFluid().isEmpty()) {
                Vec3D tmpMot = this.getMot();
                this.setMot(tmpMot.add(0.0D, (d3 - tmpMot.y) * d4, 0.0D));
            }
        }

        if (this.abilities.isFlying) {
            d3 = this.getMot().y;
            float f = this.aE;
            this.aE = this.abilities.flySpeed * (this.isSprinting() ? 2 : 1);
            super.g(vec3D);
            Vec3D vec3d2 = this.getMot();
            this.setMot(vec3d2.x, d3 * 0.6D, vec3d2.z);
            this.aE = f;
            this.fallDistance = 0.0F;
        } else {
            super.g(vec3D);
        }
    }

    private void baseTravel(Vec3D vec3D) {

    }

    @Override
    public void entityBaseTick() { }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    public void setFlag(int i, boolean flag) { }

    @Override
    protected final void collideNearby() { }

    @Override
    public void playSound(SoundEffect soundeffect, float f, float f1) { }

    @Override
    public void checkMovement(double d0, double d1, double d2) { }

    @Override
    public void a(SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) { }

    @Override
    public void a(MinecraftKey key) { }

    @Override
    public void applyExhaustion(float f) {
        super.applyExhaustion(f);
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }
}
