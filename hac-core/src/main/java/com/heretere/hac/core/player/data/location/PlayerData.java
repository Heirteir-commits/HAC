package com.heretere.hac.core.player.data.location;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3f;
import com.heretere.hac.api.API;
import com.heretere.hac.api.events.types.packets.wrapper.in.FlyingPacket;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.util.reflections.helper.EntityHelper;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PlayerData {
    @Getter(AccessLevel.NONE)
    private final HACPlayer player;

    private final Data current;
    private final Data previous;

    public PlayerData(@NotNull HACPlayer player) {
        this.player = player;
        this.current = new Data(this.player.getBukkitPlayer());
        this.previous = new Data(this.player.getBukkitPlayer());
    }

    public void update(@NotNull FlyingPacket flying) {
        this.previous.apply(this.current);

        this.current.onGround = flying.isOnGround();
        this.current.hasLook = flying.isHasLook();
        this.current.hasPos = flying.isHasPos();

        if (this.current.hasLook) {
            this.current.direction = new Vector2f(flying.getYaw(), flying.getPitch());
        }

        if (this.current.hasPos) {
            this.current.location = new Vector3f(flying.getX(), flying.getY(), flying.getZ());
            this.current.velocity = this.current.location.sub(this.previous.location);
            this.current.moving = this.current.velocity.getX() != 0 || this.current.velocity.getY() != 0 || this.current.velocity.getZ() != 0;
        } else {
            this.current.velocity = Vector3f.ZERO;
            this.current.moving = false;
        }

        this.setElytraFlying(API.INSTANCE.getReflections().getHelpers().getHelper(EntityHelper.class).isElytraFlying(this.player.getBukkitPlayer()));
    }

    public void setSneaking(boolean sneaking) {
        this.current.sneaking = sneaking;
    }

    public void setSprinting(boolean sprinting) {
        this.current.sprinting = sprinting;
    }

    public void setElytraFlying(boolean elytraFlying) {
        this.current.elytraFlying = elytraFlying;
    }

    public void setFlying(boolean flying) {
        this.current.flying = flying;
    }

    @lombok.Data
    public static final class Data {
        private Vector3f location;
        private Vector3f velocity;
        private Vector2f direction;

        private boolean onGround;
        private boolean moving;
        private boolean sneaking;
        private boolean sprinting;
        private boolean elytraFlying;
        private boolean flying;
        private boolean hasPos;
        private boolean hasLook;

        private Data(@NotNull Player player) {
            this.location = new Vector3f(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
            this.velocity = Vector3f.ZERO;
            this.direction = new Vector2f(player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch());

            this.onGround = true;
            this.moving = false;
            this.sneaking = player.isSneaking();
            this.sprinting = player.isSprinting();
            this.elytraFlying = false;
            this.flying = player.isFlying();
            this.hasPos = false;
            this.hasLook = false;
        }

        private void apply(@NotNull Data data) {
            this.location = data.location;
            this.velocity = data.velocity;
            this.direction = data.direction;

            this.onGround = data.onGround;
            this.moving = data.moving;
            this.sneaking = data.sneaking;
            this.sprinting = data.sprinting;
            this.elytraFlying = data.elytraFlying;
            this.flying = data.flying;
            this.hasPos = data.hasPos;
            this.hasLook = data.hasLook;
        }
    }

}
