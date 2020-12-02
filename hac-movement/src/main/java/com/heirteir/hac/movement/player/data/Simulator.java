package com.heirteir.hac.movement.player.data;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.core.player.data.location.PlayerData;
import com.heirteir.hac.core.util.reflections.helper.EntityHelper;
import com.heirteir.hac.core.util.reflections.helper.WorldHelper;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.human.EntityHumanAccessor;
import com.mojang.authlib.GameProfile;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public final class Simulator {
    private static final float GRAVITY = 0.08F;
    private static final float GRAVITY_DRAG = Simulator.GRAVITY * 0.98F;

    private final Movement movement;
    private final PlayerData playerData;
    @Getter
    private final SimulatorData closestMatch;
    private HACHumanWrapper noInputSimulator;
    private ImmutableSet<HACHumanWrapper> simulators;

    public Simulator(Movement movement, HACPlayer player, WrappedConstructor hacHumanConstructor, WrappedConstructor foodMetaDataConstructor) {
        this.movement = movement;
        this.playerData = player.getDataManager().getData(PlayerData.class);
        this.closestMatch = new SimulatorData();
        this.resetClosestMatch();

        Object[] arguments;
        Object world = API.INSTANCE.getReflections().getHelpers().getHelper(WorldHelper.class).getNMSWorld(player.getBukkitPlayer().getWorld());
        GameProfile profile = new GameProfile(null, "HAC-Simulator");

        if (API.INSTANCE.getReflections().getVersion().greaterThanOrEqual(ServerVersion.SIXTEEN_R1)) {
            arguments = new Object[]{world, API.INSTANCE.getReflections().getHelpers().getHelper(EntityHelper.class).getBlockPosition(player.getBukkitPlayer()), 0f, profile};
        } else {
            arguments = new Object[]{world, profile};
        }

        try {
            EntityHumanAccessor hacHuman = hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments);
            Object foodMeta = foodMetaDataConstructor.newInstance(Object.class, hacHuman);
            this.noInputSimulator = new HACHumanWrapper(player, hacHuman, foodMeta, 0, 0, 0);

            Set<HACHumanWrapper> wrappers = Sets.newHashSetWithExpectedSize(18);

            wrappers.add(this.noInputSimulator);
            /* Creates 17 simulators with the strafe: -1 -> 1, forward: -1 -> 1, and jump: 0 -> 1*/
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = 0; z <= 1; z++) {
                        if (!(x == 0 && y == 0 && z == 0)) {
                            hacHuman = hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments);
                            foodMeta = foodMetaDataConstructor.newInstance(Object.class, hacHuman);
                            wrappers.add(new HACHumanWrapper(player, hacHuman, foodMeta, x, y, z));
                        }
                    }
                }
            }

            this.simulators = ImmutableSet.copyOf(wrappers);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            movement.getLog().reportFatalError(e);
        }
    }

    public void update() {
        this.noInputSimulator.update(this.closestMatch);

        if (!this.playerData.getCurrent().isHasPos() &&
                !this.noInputSimulator.getSimulatorData().isOnGround() &&
                this.noInputSimulator.getSimulatorData().getVelocity().getY() == 0) {
            this.playerData.getCurrent().setVelocity(Vector3f.ZERO);
            this.playerData.getCurrent().setHasPos(true);
            this.playerData.getCurrent().setHasLook(true);
        }

        /* piston's changed 1.15 - better */

        CountDownLatch latch = new CountDownLatch(18);

        this.simulators.forEach(simulator -> API.INSTANCE.getThreadPool().getPool().execute(() -> {
            try {
                simulator.update(this.closestMatch);
            } catch (Exception e) {
                this.movement.getLog().severe(e);
            } finally {
                latch.countDown();
            }
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            this.movement.getLog().reportFatalError(e);
            Thread.currentThread().interrupt();
        }

        this.closestMatch.apply(this.simulators.stream()
                .map(HACHumanWrapper::getSimulatorData)
                .min(Comparator.comparingDouble(data -> this.playerData.getCurrent().getVelocity().sub(data.getVelocity()).length()))
                .orElse(this.closestMatch));

        if (/* this.playerData.getCurrent().getVelocity().length() > this.closestMatch.getVelocity().length() && */ this.playerData.getCurrent().getVelocity().sub(this.closestMatch.getVelocity()).length() > 0.1) {
            this.movement.getLog().info(this.playerData.getCurrent().getVelocity().sub(this.closestMatch.getVelocity()).length() + " " + this.playerData.getCurrent().getVelocity() + " " + this.closestMatch.getVelocity());
        } else {
            this.closestMatch.setMotion(this.closestMatch.getMotion().add(this.playerData.getCurrent().getVelocity().sub(this.closestMatch.getVelocity())));
            this.closestMatch.setLocation(this.playerData.getCurrent().getLocation());
        }
    }

    private void resetClosestMatch() {
        this.closestMatch.setVelocity(Vector3f.ZERO);
        this.closestMatch.setLocation(this.playerData.getCurrent().getLocation());
        this.closestMatch.setMotion(new Vector3f(0, -Simulator.GRAVITY_DRAG, 0));
        this.closestMatch.setVelocity(Vector3f.ZERO);
        this.closestMatch.setFallDistance(0);
        this.closestMatch.setInWeb(false);
        this.closestMatch.setOnGround(this.playerData.getCurrent().isOnGround());
        this.closestMatch.setMotOffset(null);
    }
}
