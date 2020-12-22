package com.heretere.hac.core.util.math.vector;

import com.flowpowered.math.GenericMath;
import org.jetbrains.annotations.NotNull;

/**
 * Represents in a point in 3d space.
 */
public final class MutableVector3F {
    /**
     * The x location in 3d space.
     */
    private float x;
    /**
     * The y location in 3d space.
     */
    private float y;
    /**
     * The z location in 3d space.
     */
    private float z;

    /**
     * Instantiates a new Mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public MutableVector3F(
        final double x,
        final double y,
        final double z
    ) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Instantiates a new Mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public MutableVector3F(
        final float x,
        final float y,
        final float z
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Add mutable vector 3 f.
     *
     * @param other the other
     * @return the mutable vector 3 f
     */
    public MutableVector3F add(@NotNull final MutableVector3F other) {
        return this.add(other.x, other.y, other.z);
    }

    /**
     * Add mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F add(
        final double x,
        final double y,
        final double z
    ) {
        return this.add((float) x, (float) y, (float) z);
    }

    /**
     * Add mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F add(
        final float x,
        final float y,
        final float z
    ) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    /**
     * Subtract mutable vector 3 f.
     *
     * @param other the other
     * @return the mutable vector 3 f
     */
    public MutableVector3F subtract(@NotNull final MutableVector3F other) {
        return this.subtract(other.x, other.y, other.z);
    }

    /**
     * Subtract mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F subtract(
        final double x,
        final double y,
        final double z
    ) {
        return this.subtract((float) x, (float) y, (float) z);
    }

    /**
     * Subtract mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F subtract(
        final float x,
        final float y,
        final float z
    ) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    /**
     * Mul mutable vector 3 f.
     *
     * @param other the other
     * @return the mutable vector 3 f
     */
    public MutableVector3F mul(@NotNull final MutableVector3F other) {
        return this.mul(other.x, other.y, other.z);
    }

    /**
     * Mul mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F mul(
        final double x,
        final double y,
        final double z
    ) {
        return this.mul((float) x, (float) y, (float) z);
    }

    /**
     * Mul mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F mul(
        final float x,
        final float y,
        final float z
    ) {
        this.x *= x;
        this.y *= y;
        this.z *= z;

        return this;
    }

    /**
     * Set mutable vector 3 f.
     *
     * @param other the other
     * @return the mutable vector 3 f
     */
    public MutableVector3F set(@NotNull final MutableVector3F other) {
        return this.set(other.x, other.y, other.z);
    }

    /**
     * Set mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F set(
        final double x,
        final double y,
        final double z
    ) {
        return this.set((float) x, (float) y, (float) z);
    }

    /**
     * Set mutable vector 3 f.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable vector 3 f
     */
    public MutableVector3F set(
        final float x,
        final float y,
        final float z
    ) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     * @return the x
     */
    public MutableVector3F setX(final float x) {
        this.x = x;
        return this;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     * @return the y
     */
    public MutableVector3F setY(final float y) {
        this.y = y;
        return this;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public float getZ() {
        return z;
    }

    /**
     * Sets z.
     *
     * @param z the z
     * @return the z
     */
    public MutableVector3F setZ(final float z) {
        this.z = z;
        return this;
    }

    /**
     * Length float.
     *
     * @return the float
     */
    public float length() {
        return (float) GenericMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Copy mutable vector 3 f.
     *
     * @return the mutable vector 3 f
     */
    public MutableVector3F copy() {
        return new MutableVector3F(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "MutableVector3F{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }


}
