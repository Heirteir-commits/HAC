package com.heretere.hac.movement.proxy.player.data.simulator.entity;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.TrigMath;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.data.player.PlayerData;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import com.heretere.hac.movement.proxy.AbstractMovementVersionProxy;
import com.heretere.hac.movement.proxy.util.math.box.MutableBox3F;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.stream.Stream;

public class SimulatorEntity {
    private static final float GRAVITY = 0.08F;

    private final AbstractMovementVersionProxy parent;

    private final HACPlayer player;
    private final PlayerData playerData;

    public SimulatorEntity(@NotNull AbstractMovementVersionProxy parent, @NotNull HACPlayer player) {
        this.parent = parent;

        this.player = player;
        this.playerData = player.getDataManager().getData(PlayerData.class);
    }

    public void tick(@NotNull Data data) {
        if (this.playerData.getCurrent().getVelocity().length() > GenericMath.FLT_EPSILON) {
            this.updatePose(data);
            this.updateModifiers(data);
            this.applyHeading(data, 0, 0.98F);

            System.out.println(data.getMotion());
        }

//        System.out.println(data.getLocation());
    }

    private void updateModifiers(@NotNull Data data) {
        Player bukkitPlayer = this.player.getBukkitPlayer();

        data.getDirection().set(this.playerData.getCurrent().getDirection());

        data.setWorld(bukkitPlayer.getWorld());
        data.setWalkSpeed(bukkitPlayer.getWalkSpeed());
        data.setSneaking(this.playerData.getCurrent().isSneaking());
        data.setSprinting(this.playerData.getCurrent().isSprinting());
    }

    private void updatePose(@NotNull Data data) {
        PlayerData.Data current = playerData.getCurrent();
        Pose.State state = Pose.State.STANDING;

        if (current.isSneaking()) {
            state = Pose.State.SNEAKING;
        }

        data.pose.updateState(state);
    }

    private void applyHeading(@NotNull Data data, float strafe, float forward) {
        float friction = 0.91F;

        if (data.isOnGround()) {
            AbstractMap.SimpleImmutableEntry<Material, Float> stats = this.parent.getWorldHelper()
                    .getBlockStats(data.getWorld(), data.getLocation().copy().subtract(0, 0.1, 0));

            friction = stats.getValue() * 0.91F;
        }

        float multiplier;

        if (data.isOnGround()) {
            multiplier = 0.1F * (0.16277136F / (friction * friction * friction));
        } else {
            multiplier = 0.02F;
        }

        System.out.println(multiplier);

        this.moveFlying(data, strafe, forward, multiplier);

        this.move(data);

        data.getMotion().subtract(0, SimulatorEntity.GRAVITY, 0);
        data.getMotion().mul(friction, 0.98F, friction);
    }

    private void moveFlying(@NotNull Data data, float strafe, float forward, float multiplier) {
        float lengthSqr = strafe * strafe + forward * forward;

        if (lengthSqr > GenericMath.FLT_EPSILON) {
            lengthSqr = (float) GenericMath.sqrt(lengthSqr);

            lengthSqr = Math.max(lengthSqr, 1F);

            lengthSqr = multiplier / lengthSqr;

            strafe = strafe * lengthSqr;
            forward = forward * lengthSqr;

            float sin = TrigMath.sin(data.getDirection().getX() * TrigMath.DEG_TO_RAD);
            float cos = TrigMath.cos(data.getDirection().getX() * TrigMath.DEG_TO_RAD);

            data.getMotion().add(
                    strafe * cos - forward * sin,
                    0F,
                    forward * cos + strafe * sin
            );
        }
    }

    private void move(@NotNull Data data) {
        MutableVector3F offset = data.getLocation().copy().add(data.getMotion());
        MutableBox3F offsetBox = data.getPose().getBox().copy()
                .addCoord(
                        data.getMotion().getX(),
                        data.getMotion().getY(),
                        data.getMotion().getZ()
                );

        Stream<MutableBox3F> boxes = this.parent.getWorldHelper().getCollisions(data.getWorld(), offset, offsetBox);

        offsetBox.toAbsolute(data.getLocation());

        MutableVector3F collisionCorrection = new MutableVector3F(0, 0, 0);

        boxes.forEach(box -> collisionCorrection.set(
                offsetBox.calculateXOffset(box, collisionCorrection.getX(), data.getMotion().getX()),
                offsetBox.calculateYOffset(box, collisionCorrection.getY(), data.getMotion().getY()),
                offsetBox.calculateZOffset(box, collisionCorrection.getZ(), data.getMotion().getZ())
        ));

        data.getMotion().add(collisionCorrection);
        data.getLocation().add(data.getMotion());
    }

    public static class Data {
        private final Pose pose;

        private final MutableVector3F location;
        private final MutableVector2F direction;
        private final MutableVector3F motion;
        private final MutableVector3F velocity;

        private World world;

        private boolean onGround;
        private boolean sneaking;
        private boolean sprinting;

        private float walkSpeed;

        public Data() {
            this.pose = new Pose();
            this.location = new MutableVector3F(0, 0, 0);
            this.direction = new MutableVector2F(0, 0);
            this.motion = new MutableVector3F(0, 0, 0);
            this.velocity = new MutableVector3F(0, 0, 0);

            this.onGround = true;
            this.sneaking = false;
            this.sprinting = false;
            this.walkSpeed = 0;
        }

        public void apply(Data other) {
            this.pose.updateState(other.pose.getState());

            this.location.set(other.location);
            this.direction.set(other.direction);
            this.velocity.set(other.velocity);
            this.motion.set(other.motion);

            this.world = other.world;

            this.onGround = other.onGround;
            this.sneaking = other.sneaking;
            this.sprinting = other.sprinting;
            this.walkSpeed = other.walkSpeed;
        }

        public void setLocation(double x, double y, double z) {
            this.location.set(x, y, z);
        }

        public void setDirection(double yaw, double pitch) {
            this.direction.set(yaw, pitch);
        }

        public void setMotion(double dx, double dy, double dz) {
            this.motion.set(dx, dy, dz);
        }

        public void setVelocity(double dx, double dy, double dz) {
            this.velocity.set(dx, dy, dz);
        }

        public void setPoseState(Pose.State state) {
            this.pose.updateState(state);
        }

        public MutableVector3F getLocation() {
            return location;
        }

        public MutableVector2F getDirection() {
            return direction;
        }

        public MutableVector3F getMotion() {
            return motion;
        }

        public MutableVector3F getVelocity() {
            return velocity;
        }

        public boolean isOnGround() {
            return onGround;
        }

        public void setOnGround(boolean onGround) {
            this.onGround = onGround;
        }

        public boolean isSneaking() {
            return sneaking;
        }

        public void setSneaking(boolean sneaking) {
            this.sneaking = sneaking;
        }

        public boolean isSprinting() {
            return sprinting;
        }

        public void setSprinting(boolean sprinting) {
            this.sprinting = sprinting;
        }

        public float getWalkSpeed() {
            return walkSpeed;
        }

        public void setWalkSpeed(float walkSpeed) {
            this.walkSpeed = walkSpeed;
        }

        public Pose getPose() {
            return pose;
        }

        public World getWorld() {
            return world;
        }

        public void setWorld(World world) {
            this.world = world;
        }

        public Data copy() {
            Data output = new Data();
            output.apply(this);
            return output;
        }
    }
}
