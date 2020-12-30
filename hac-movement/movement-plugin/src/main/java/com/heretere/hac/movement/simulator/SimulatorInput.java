package com.heretere.hac.movement.simulator;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.TrigMath;
import com.flowpowered.math.vector.Vector2f;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * This class represents all possible player input permutations.
 */
public enum SimulatorInput {
    /**
     * When the player is just pressing the forward key (w).
     */
    FORWARD(0, new Vector2f(0, 1)),
    /**
     * When the player is pressing forward and left (w, a).
     */
    FORWARD_LEFT(TrigMath.QUARTER_PI, new Vector2f(1, 1)),
    /**
     * When the player is pressing forward and right (w, d).
     */
    FORWARD_RIGHT(-TrigMath.QUARTER_PI, new Vector2f(-1, 1)),
    /**
     * When the player is pressing the backward key (s).
     */
    BACK(TrigMath.PI, new Vector2f(0, -1)),
    /**
     * When the player is pressing the backward and left key (s, a).
     */
    BACK_LEFT(TrigMath.HALF_PI + TrigMath.QUARTER_PI, new Vector2f(1, -1)),
    /**
     * When the player is pressing the backward and right key (s, d).
     */
    BACK_RIGHT(-TrigMath.HALF_PI - TrigMath.QUARTER_PI, new Vector2f(-1, -1)),
    /**
     * When the player is pressing the right key (d).
     */
    RIGHT(-TrigMath.HALF_PI, new Vector2f(-1, 0)),
    /**
     * When the player is pressing the left key (a).
     */
    LEFT(TrigMath.HALF_PI, new Vector2f(1, 0));

    /**
     * The signed angle this input represents on the unit circle in radians.
     */
    private final float angleRad;
    /**
     * A 2d vector containing values between [-1,1], [-1,1].
     * Represents the inputs as floats.
     */
    private final @NotNull Vector2f input;

    SimulatorInput(
        final double angleRad,
        final @NotNull Vector2f input
    ) {
        this.angleRad = (float) angleRad;
        this.input = input;
    }

    /**
     * Get the signed input angle in radians on the unit circle.
     *
     * @return the angle rad
     */
    public float getAngleRad() {
        return this.angleRad;
    }

    /**
     * A 2d vector containing values between [-1,1], [-1,1].
     *
     * @return the input vector
     */
    public @NotNull Vector2f getInput() {
        return this.input;
    }

    /**
     * Gets the closest input based on the input radians.
     *
     * @param radians the signed angle in radians
     * @return The simulator input that represents this angle.
     */
    public static @NotNull SimulatorInput getInputFromAngleRadians(final float radians) {
        return Arrays.stream(values())
                     .min(Comparator.comparingDouble(inputRad -> GenericMath.getRadianDifference(
                         radians,
                         inputRad.angleRad
                     )))
                     .orElseThrow(NoSuchElementException::new);
    }
}
