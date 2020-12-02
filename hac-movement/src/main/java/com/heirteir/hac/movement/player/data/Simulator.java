package com.heirteir.hac.movement.player.data;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.collect.ImmutableSet;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.player.HACPlayer;
import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.core.player.data.location.PlayerData;
import com.heirteir.hac.core.util.reflections.helper.PlayerHelper;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.entity.human.EntityHumanAccessor;
import com.mojang.authlib.GameProfile;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

public final class Simulator {
    private final PlayerData playerData;
    @Getter
    private final SimulatorData closestMatch;
    private HACHumanWrapper noInputEmulator;
    private ImmutableSet<HACHumanWrapper> emulators;

    public Simulator(Movement movement, HACPlayer player, WrappedConstructor hacHumanConstructor) {
        this.playerData = player.getDataManager().getData(PlayerData.class);
        this.closestMatch = new SimulatorData();
        this.resetClosestMatch();

        Object[] arguments;
        Object world = API.INSTANCE.getReflections().getHelpers().getHelper(PlayerHelper.class).getWorld(player.getBukkitPlayer());
        GameProfile profile = new GameProfile(null, "HAC-Simulator");

        if (API.INSTANCE.getReflections().getVersion().greaterThanOrEqual(ServerVersion.SIXTEEN_R1)) {
            arguments = new Object[]{world, API.INSTANCE.getReflections().getHelpers().getHelper(PlayerHelper.class).getBlockPosition(player.getBukkitPlayer()), 0f, profile};
        } else {
            arguments = new Object[]{world, profile};
        }

        try {
            this.noInputEmulator = new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, 0, 0);

            this.emulators = ImmutableSet.of(
                    this.noInputEmulator,
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, 1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, 1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, -1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 1, 0, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 1, 1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 1, -1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), -1, 0, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), -1, 1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), -1, -1, 0),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, 0, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, 1, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 0, -1, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 1, 0, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 1, 1, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), 1, -1, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), -1, 0, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), -1, 1, 1),
                    new HACHumanWrapper(player, hacHumanConstructor.newInstance(EntityHumanAccessor.class, arguments), -1, -1, 1)
            );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            movement.getLog().reportFatalError(e);
        }
    }

    public void update() {
        if (!this.playerData.getCurrent().isHasPos()) {
            this.noInputEmulator.update(this.closestMatch);
            if (!this.noInputEmulator.getSimulatorData().isOnGround() && this.noInputEmulator.getSimulatorData().getVelocity().getY() == 0) {
                this.playerData.getCurrent().setVelocity(Vector3f.ZERO);
                this.playerData.getCurrent().setHasPos(true);
                this.playerData.getCurrent().setHasLook(true);
            }
        }

        this.emulators.forEach(emulator -> emulator.update(this.closestMatch));

        this.closestMatch.apply(this.emulators.stream()
                .map(HACHumanWrapper::getSimulatorData)
                .min(Comparator.comparingDouble(data -> this.playerData.getCurrent().getVelocity().sub(data.getVelocity()).length()))
                .orElse(this.closestMatch));


        if (this.playerData.getCurrent().getVelocity().length() > this.closestMatch.getVelocity().length() && this.playerData.getCurrent().getVelocity().sub(this.closestMatch.getVelocity()).length() > 0.1) {
            System.out.println(this.playerData.getCurrent().getVelocity().sub(this.closestMatch.getVelocity()) + " " + this.playerData.getCurrent().getVelocity() + " " + this.closestMatch.getVelocity());
        }
    }

    private void resetClosestMatch() {
        this.closestMatch.setVelocity(Vector3f.ZERO);
        this.closestMatch.setLocation(this.playerData.getCurrent().getLocation());
        this.closestMatch.setMotion(new Vector3f(0, -0.0784000015258789, 0));
        this.closestMatch.setVelocity(Vector3f.ZERO);
        this.closestMatch.setFallDistance(0);
        this.closestMatch.setInWeb(false);
        this.closestMatch.setOnGround(this.playerData.getCurrent().isOnGround());
        this.closestMatch.setMotOffset(null);
    }
}
