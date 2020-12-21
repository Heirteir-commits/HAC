package com.heretere.hac.movement.proxy.util.math.box;

import com.google.common.collect.Sets;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * All methods in this class are mutable. It is represented by two separate Mutable Vectors.
 */
public final class MutableBox3F {
    /**
     * How many vertices (or corners) are located on bounding box.
     */
    private static final int BOX_VERTICES = 8;

    /**
     * The minimum point of this bounding box.
     */
    private final MutableVector3F min;
    /**
     * The maximum point of this bounding box.
     */
    private final MutableVector3F max;

    /**
     * Instantiates a new Mutable box 3.
     *
     * @param minX the min x
     * @param minY the min y
     * @param minZ the min z
     * @param maxX the max x
     * @param maxY the max y
     * @param maxZ the max z
     */
    public MutableBox3F(final double minX,
                        final double minY,
                        final double minZ,
                        final double maxX,
                        final double maxY,
                        final double maxZ) {
        this.min = new MutableVector3F(minX, minY, minZ);
        this.max = new MutableVector3F(maxX, maxY, maxZ);
    }

    /**
     * The minimum Vector3F point on the bounding box.
     *
     * @return the min
     */
    public MutableVector3F getMin() {
        return min;
    }

    /**
     * The maximum Vector3F point on the bounding box.
     *
     * @return the max
     */
    public MutableVector3F getMax() {
        return max;
    }

    /**
     * Gets vertices of a bounding box (or the corners).
     *
     * @return the vertices
     */
    public Set<MutableVector3F> getVertices() {
        Set<MutableVector3F> vertices = Sets.newHashSetWithExpectedSize(BOX_VERTICES);

        vertices.add(new MutableVector3F(this.min.getX(), this.max.getY(), this.min.getZ()));
        vertices.add(new MutableVector3F(this.min.getX(), this.max.getY(), this.max.getZ()));
        vertices.add(new MutableVector3F(this.min.getX(), this.min.getY(), this.max.getZ()));
        vertices.add(new MutableVector3F(this.min.getX(), this.min.getY(), this.min.getZ()));

        vertices.add(new MutableVector3F(this.max.getX(), this.min.getY(), this.min.getZ()));
        vertices.add(new MutableVector3F(this.max.getX(), this.max.getY(), this.min.getZ()));
        vertices.add(new MutableVector3F(this.max.getX(), this.min.getY(), this.max.getZ()));
        vertices.add(new MutableVector3F(this.max.getX(), this.max.getY(), this.max.getZ()));

        return vertices;
    }

    /**
     * Calculate x offset float.
     *
     * @param other          the other
     * @param currentOffsetX the current offset x
     * @param dx             the dx
     * @return the float
     */
    public float calculateXOffset(@NotNull final MutableBox3F other, final float currentOffsetX, final float dx) {
        float output = 0;

        if (this.intersectsY(other) && this.intersectsZ(other)) {
            if (dx > 0 && other.min.getX() <= this.max.getX()) {
                output = Math.min(this.max.getX() - other.min.getX(), currentOffsetX);
            } else if (dx < 0 && other.max.getX() >= this.min.getX()) {
                output = Math.max(other.max.getX() - this.min.getX(), currentOffsetX);
            }
        }

        return output;
    }

    /**
     * Calculate y offset float.
     *
     * @param other          the other
     * @param currentOffsetY the current offset y
     * @param dy             the dy
     * @return the float
     */
    public float calculateYOffset(@NotNull final MutableBox3F other, final float currentOffsetY, final float dy) {
        float output = 0;

        if (this.intersectsX(other) && this.intersectsZ(other)) {
            if (dy > 0 && other.min.getY() <= this.max.getY()) {
                output = Math.min(this.max.getY() - other.min.getY(), currentOffsetY);
            } else if (dy < 0 && other.max.getY() >= this.min.getY()) {
                output = Math.max(other.max.getY() - this.min.getY(), currentOffsetY);
            }
        }

        return output;
    }

    /**
     * Calculate z offset float.
     *
     * @param other          the other
     * @param currentOffsetZ the current offset z
     * @param dz             the dz
     * @return the float
     */
    public float calculateZOffset(@NotNull final MutableBox3F other, final float currentOffsetZ, final float dz) {
        float output = 0;

        if (this.intersectsX(other) && this.intersectsY(other)) {
            if (dz > 0 && other.min.getZ() <= this.max.getZ()) {
                output = Math.min(this.max.getZ() - other.min.getZ(), currentOffsetZ);
            } else if (dz < 0 && other.max.getZ() >= this.min.getZ()) {
                output = Math.max(other.max.getZ() - this.min.getZ(), currentOffsetZ);
            }
        }

        return output;
    }

    /**
     * Extends a bounding box in a coordinate direction. Usually used for calculating the offset of the bounding box.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the mutable box 3 f
     */
    public MutableBox3F addCoord(final float x, final float y, final float z) {
        this.min.add(Math.min(x, 0.0F), Math.min(y, 0.0F), Math.min(z, 0.0F));
        this.max.add(Math.max(x, 0.0F), Math.max(y, 0.0F), Math.max(z, 0.0F));

        return this;
    }

    private boolean intersectsX(@NotNull final MutableBox3F other) {
        return this.max.getX() >= other.min.getX() && other.max.getX() >= this.min.getX();
    }

    private boolean intersectsY(@NotNull final MutableBox3F other) {
        return this.max.getY() >= other.min.getY() && other.max.getY() >= this.min.getY();
    }

    private boolean intersectsZ(@NotNull final MutableBox3F other) {
        return this.max.getZ() >= other.min.getZ() && other.max.getZ() >= this.min.getZ();
    }

    /**
     * Checks if if a bounding box intersects with another.
     *
     * @param other the other
     * @return the boolean
     */
    public boolean intersects(@NotNull final MutableBox3F other) {
        return this.intersectsX(other) && this.intersectsY(other) && this.intersectsZ(other);
    }

    /**
     * Copies a bounding box to a new MutableBox3F object for safe manipulation.
     *
     * @return the mutable box 3 f
     */
    public MutableBox3F copy() {
        return new MutableBox3F(
                this.min.getX(),
                this.min.getY(),
                this.min.getZ(),
                this.max.getX(),
                this.max.getY(),
                this.max.getZ()
        );
    }

    /**
     * Appends the world coords onto the bounding box.
     *
     * @param position the position
     * @return the mutable box 3 f
     */
    public MutableBox3F toAbsolute(@NotNull final MutableVector3F position) {
        this.min.add(position);
        this.max.add(position);
        return this;
    }

    @Override
    public String toString() {
        return "MutableBox3F{"
                + "min=" + min
                + ", max=" + max
                + '}';
    }
}
