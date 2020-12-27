package com.heretere.hac.movement.player.data.simulator;

import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import com.heretere.hac.movement.proxy.MovementVersionProxy;
import com.heretere.hac.movement.proxy.simulation.HumanAccessor;
import com.heretere.hac.movement.proxy.simulation.SimulationData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Simulator {
    private final @NotNull MovementVersionProxy proxy;
    private final @NotNull HACPlayer player;

    private @Nullable HumanAccessor accessor;
    private @Nullable SimulationData current;

    boolean first;

    public Simulator(
        MovementVersionProxy proxy,
        HACPlayer player
    ) {
        this.proxy = proxy;
        this.player = player;

        this.player.getBukkitPlayer().ifPresent(bukkitPlayer -> {
            this.accessor = this.proxy.createHumanAccessor(bukkitPlayer.getWorld());
            this.current = new SimulationData();
        });
    }

    public void update() {
        if (this.accessor == null || this.current == null) {
            return;
        }
        if (!first) {
            this.player.getBukkitPlayer().ifPresent(this::updateSimulatorData);
            first = true;
        }

        SimulationData output = this.accessor.simulationTick(this.current);
        this.current.apply(output);
    }

    private void updateSimulatorData(Player bukkitPlayer) {
        if (this.current == null) {
            return;
        }

        this.player.getDataManager().getData(PlayerData.class)
                   .ifPresent(playerData -> {
                       this.current.setWorld(bukkitPlayer.getWorld());
                       MutableVector3F location = playerData.getCurrent().getLocation();
                       MutableVector2F direction = playerData.getCurrent().getDirection();

                       this.current.setLocation(location.getX(), location.getY(), location.getZ());
                       this.current.setDirection(direction.getX(), direction.getY());

                       this.current.setSneaking(playerData.getCurrent().isSneaking());
                       this.current.setSprinting(playerData.getCurrent().isSprinting());
                       this.current.setElytraFlying(playerData.getCurrent().isElytraFlying());
                       this.current.setFlying(playerData.getCurrent().isFlying());
                   });
    }

}
