package com.heretere.hac.movement.proxy.versions.sixteen.simulation;

import com.flowpowered.math.GenericMath;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.EnumMainHand;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.SoundEffect;
import net.minecraft.server.v1_16_R3.TagsFluid;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class HACEntityHuman extends EntityLiving {
    protected HACEntityHuman(org.bukkit.World world) {
        super(EntityTypes.PLAYER, ((CraftWorld) world).getHandle());
        this.setInvulnerable(true);
        this.justCreated = false;
    }


    @Override
    public void tick() {
        this.movementTick();
    }

    @Override
    public void movementTick() {
        this.setMot(
            Math.abs(this.getMot().getX()) < GenericMath.DBL_EPSILON ? 0.0D : this.getMot().getX(),
            Math.abs(this.getMot().getY()) < GenericMath.DBL_EPSILON ? 0.0D : this.getMot().getY(),
            Math.abs(this.getMot().getZ()) < GenericMath.DBL_EPSILON ? 0.0D : this.getMot().getZ()
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

            this.travel(new Vec3D(this.aR, 0, this.aT));
        }
    }

    private void travel(Vec3D vec3D) {
        double d3;

        if (this.isSwimming()) {
            d3 = this.getLookDirection().y;
            double d4 = d3 < -0.2D ? 0.085D : 0.06D;
            if (d3 <= 0.0D || this.jumping || !this.world.getType(new BlockPosition(
                this.locX(),
                this.locY() + 1.0D - 0.1D,
                this.locZ()
            )).getFluid().isEmpty()) {
                Vec3D tmpMot = this.getMot();
                this.setMot(tmpMot.add(0.0D, (d3 - tmpMot.y) * d4, 0.0D));
            }
        }

//        if (play) {
//            d3 = this.getMot().y;
//            float f = this.aE;
//            this.aE = this.abilities.flySpeed * (this.isSprinting() ? 2 : 1);
//            super.g(vec3D);
//            Vec3D vec3d2 = this.getMot();
//            this.setMot(vec3d2.x, d3 * 0.6D, vec3d2.z);
//            this.aE = f;
//            this.fallDistance = 0.0F;
//        } else {
        super.g(vec3D);
//        }
    }

    @Override
    public void entityBaseTick() {
        //Override entitybasetick to do nothing.
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquipment(EnumItemSlot enumItemSlot) {
        return null;
    }

    @Override
    public void setSlot(
        EnumItemSlot enumItemSlot,
        ItemStack itemStack
    ) {
        //override setSlot to do nothing
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public void setFlag(
        int i,
        boolean flag
    ) { }

    @Override
    protected final void collideNearby() { }

    @Override
    public EnumMainHand getMainHand() {
        return null;
    }

    @Override
    public void playSound(
        SoundEffect soundeffect,
        float f,
        float f1
    ) { }

    @Override
    protected boolean playStepSound() {
        return false;
    }
}
