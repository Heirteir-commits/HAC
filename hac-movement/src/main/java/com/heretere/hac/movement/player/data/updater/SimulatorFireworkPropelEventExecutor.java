package com.heretere.hac.movement.player.data.updater;

import com.flowpowered.math.TrigMath;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3f;
import com.heretere.hac.api.events.types.AbstractEventExecutor;
import com.heretere.hac.api.events.types.FireworkPropelEvent;
import com.heretere.hac.api.events.types.Priority;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.player.data.location.PlayerData;
import com.heretere.hac.movement.player.data.Simulator;
import com.heretere.hac.movement.player.data.SimulatorData;

public class SimulatorFireworkPropelEventExecutor extends AbstractEventExecutor<FireworkPropelEvent> {
    public SimulatorFireworkPropelEventExecutor() {
        super(FireworkPropelEvent.class, Priority.PROCESS_1);
    }

    private static float applyCalculation(float mot, float value) {
        return mot + (value * 0.1F + (value * 1.5F - mot) * 0.5F);
    }

    @Override
    protected void run(HACPlayer player, FireworkPropelEvent event) {
        Vector2f direction = player.getDataManager().getData(PlayerData.class).getCurrent().getDirection();

        float yaw = direction.getX();
        float f = TrigMath.cos(-yaw * TrigMath.DEG_TO_RAD - TrigMath.PI);
        float f2 = TrigMath.sin(-yaw * TrigMath.DEG_TO_RAD - TrigMath.PI);
        float f3 = TrigMath.cos(-direction.getY() * TrigMath.DEG_TO_RAD);
        float f4 = TrigMath.sin(-direction.getY() * TrigMath.DEG_TO_RAD);

        Vector3f directionToVector = new Vector3f(f2 * f3, f4, f * f3);

        SimulatorData simulatorData = player.getDataManager().getData(Simulator.class).getClosestMatch();

        simulatorData.setMotion(new Vector3f(
                applyCalculation(simulatorData.getMotion().getX(), directionToVector.getX()),
                applyCalculation(simulatorData.getMotion().getY(), directionToVector.getY()),
                applyCalculation(simulatorData.getMotion().getZ(), directionToVector.getZ())
        ));
    }
}
