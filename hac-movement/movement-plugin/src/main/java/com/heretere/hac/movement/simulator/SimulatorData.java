package com.heretere.hac.movement.simulator;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.TrigMath;
import com.flowpowered.math.vector.Vector2f;
import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.core.proxy.player.PlayerData;
import com.heretere.hac.core.util.math.vector.MutableVector2F;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import com.heretere.hac.movement.MovementVersionProxy;
import org.jetbrains.annotations.NotNull;

public final class SimulatorData {
    /**
     * All angles in Minecraft are offset by 90 degrees.
     */
    private static final float NOTCH_OFFSET = 90F;
    /**
     * the minimum difference allowed between the player and the simulator.
     */
    private static final float MIN_DIFF = 0.01F;

    /**
     * The player data instance.
     */
    private final @NotNull PlayerData playerData;

    /**
     * The proxied nms simulator instance.
     */
    private final Simulator simulator;
    /**
     * Values that represent where the simulator is at, at the latest point in time.
     */
    private final SimulationPoint current;

    /**
     * @param proxy  The version proxy
     * @param player The parent HACPlayer instance
     */
    public SimulatorData(
        final @NotNull MovementVersionProxy proxy,
        final @NotNull HACPlayer player
    ) {
        this.playerData = player.getDataManager()
                                .getData(PlayerData.class)
                                .orElseThrow(IllegalArgumentException::new);

        this.simulator = proxy.getSimulatorFactory().createSimulator(player);
        this.current = new SimulationPoint();
    }

    /**
     * Updates the simulator's position with regard to the player's current location information.
     */
    public void update() {
        this.current.getLocation().set(this.playerData.getCurrent().getLocation());
        this.current.getDirection().set(this.playerData.getCurrent().getDirection());

        this.current.setStrafe(0);
        this.current.setForward(0);
        this.current.setJumping(false);

        SimulationPoint noMove = this.simulator.processTick(this.current);

        MutableVector3F difference = this.playerData.getCurrent().getVelocity().copy().subtract(noMove.getVelocity());

        boolean moving = this.playerData.getCurrent().getVelocity().length() > GenericMath.FLT_EPSILON;

        if (!moving || difference.length() < MIN_DIFF) {
            this.current.apply(noMove);
        } else {
            MutableVector2F normalized = new MutableVector2F(difference.getX(), difference.getZ()).normalize();

            float targetAngleRadians = (float) ((this.playerData.getCurrent()
                                                                .getDirection()
                                                                .getX() + NOTCH_OFFSET) * TrigMath.DEG_TO_RAD);

            targetAngleRadians = (float) GenericMath.wrapAngleRad(targetAngleRadians);

            float sourceAngleRadians = (float) TrigMath.atan2(normalized.getY(), normalized.getX());

            float angleDifferenceRadians = (float) TrigMath.atan2(
                TrigMath.sin(targetAngleRadians - sourceAngleRadians),
                TrigMath.cos(targetAngleRadians - sourceAngleRadians)
            );

            Vector2f input = SimulatorInput.getInputFromAngleRadians(angleDifferenceRadians).getInput();

            this.current.setStrafe(input.getX());
            this.current.setForward(input.getY());

            this.current.apply(this.simulator.processTick(this.current));
        }

        if (!moving
            || this.playerData.getCurrent()
                              .getVelocity()
                              .copy()
                              .subtract(this.current.getVelocity())
                              .length() < MIN_DIFF
            || this.playerData.getCurrent().getVelocity().length() < this.current.getVelocity().length()
        ) {
            this.current.getMotion()
                        .add(this.playerData.getCurrent()
                                            .getVelocity()
                                            .copy()
                                            .subtract(this.current.getVelocity()));
        }
    }


}
