package com.heretere.hac.core.util.math.vector;

import org.jetbrains.annotations.NotNull;

/**
 * This vector is completely mutable and represents a point in 2d space.
 */
public final class MutableVector2F {
    /**
     * The x location in 2d space.
     */
    private float x;
    /**
     * The y location in 2d space.
     */
    private float y;

    /**
     * Instantiates a new Mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     */
    public MutableVector2F(
        final double x,
        final double y
    ) {
        this((float) x, (float) y);
    }

    /**
     * Instantiates a new Mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     */
    public MutableVector2F(
        final float x,
        final float y
    ) {
        this.x = x;
        this.y = y;
    }

    /**
     * Add mutable vector 2 f.
     *
     * @param other the other
     * @return the mutable vector 2 f
     */
    public MutableVector2F add(@NotNull final MutableVector2F other) {
        return this.add(other.x, other.y);
    }

    /**
     * Add mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the mutable vector 2 f
     */
    public MutableVector2F add(
        final double x,
        final double y
    ) {
        return this.add((float) x, (float) y);
    }

    /**
     * Add mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the mutable vector 2 f
     */
    public MutableVector2F add(
        final float x,
        final float y
    ) {
        this.x += x;
        this.y += y;

        return this;
    }

    /**
     * Subtract mutable vector 2 f.
     *
     * @param other the other
     * @return the mutable vector 2 f
     */
    public MutableVector2F subtract(@NotNull final MutableVector2F other) {
        return this.subtract(other.x, other.y);
    }

    /**
     * Subtract mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the mutable vector 2 f
     */
    public MutableVector2F subtract(
        final double x,
        final double y
    ) {
        return this.subtract((float) x, (float) y);
    }

    /**
     * Subtract mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the mutable vector 2 f
     */
    public MutableVector2F subtract(
        final float x,
        final float y
    ) {
        this.x -= x;
        this.y -= y;

        return this;
    }

    /**
     * Set mutable vector 2 f.
     *
     * @param other the other
     * @return the mutable vector 2 f
     */
    public MutableVector2F set(@NotNull final MutableVector2F other) {
        return this.set(other.x, other.y);
    }

    /**
     * Set mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the mutable vector 2 f
     */
    public MutableVector2F set(
        final double x,
        final double y
    ) {
        return this.set((float) x, (float) y);
    }

    /**
     * Set mutable vector 2 f.
     *
     * @param x the x
     * @param y the y
     * @return the mutable vector 2 f
     */
    public MutableVector2F set(
        final float x,
        final float y
    ) {
        this.x = x;
        this.y = y;

        return this;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public float getX() {
        return this.x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     * @return the x
     */
    public MutableVector2F setX(final float x) {
        this.x = x;
        return this;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public float getY() {
        return this.y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     * @return the y
     */
    public MutableVector2F setY(final float y) {
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return "MutableVector2F{" + "x=" + this.x + ", y=" + this.y + '}';
    }
}
