package com.heretere.hac.movement.proxy.util.math.box;

import com.google.common.collect.Sets;
import com.heretere.hac.core.util.math.vector.MutableVector3F;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MutableBox3F {
    private final MutableVector3F min;
    private final MutableVector3F max;

    public MutableBox3F(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.min = new MutableVector3F(minX, minY, minZ);
        this.max = new MutableVector3F(maxX, maxY, maxZ);
    }

    public MutableVector3F getMin() {
        return min;
    }

    public MutableVector3F getMax() {
        return max;
    }

    public Set<MutableVector3F> getVertices() {
        Set<MutableVector3F> vertices = Sets.newHashSetWithExpectedSize(8);

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

    public float calculateXOffset(@NotNull MutableBox3F other, float currentOffsetX, float dx) {
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

    public float calculateYOffset(@NotNull MutableBox3F other, float currentOffsetY, float dy) {
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

    public float calculateZOffset(@NotNull MutableBox3F other, float currentOffsetZ, float dz) {
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

    public MutableBox3F addCoord(float x, float y, float z) {
        this.min.add(Math.min(x, 0.0F), Math.min(y, 0.0F), Math.min(z, 0.0F));
        this.max.add(Math.max(x, 0.0F), Math.max(y, 0.0F), Math.max(z, 0.0F));

        return this;
    }

    private boolean intersectsX(@NotNull MutableBox3F other) {
        return this.max.getX() >= other.min.getX() && other.max.getX() >= this.min.getX();
    }

    private boolean intersectsY(@NotNull MutableBox3F other) {
        return this.max.getY() >= other.min.getY() && other.max.getY() >= this.min.getY();
    }

    private boolean intersectsZ(@NotNull MutableBox3F other) {
        return this.max.getZ() >= other.min.getZ() && other.max.getZ() >= this.min.getZ();
    }

    public boolean intersects(@NotNull MutableBox3F other) {
        return this.intersectsX(other) && this.intersectsY(other) && this.intersectsZ(other);
    }

    public MutableBox3F copy() {
        return new MutableBox3F(this.min.getX(), this.min.getY(), this.min.getZ(), this.max.getX(), this.max.getY(), this.max.getZ());
    }

    public MutableBox3F toAbsolute(MutableVector3F position) {
        this.min.add(position);
        this.max.add(position);
        return this;
    }

    @Override
    public String toString() {
        return "MutableBox3F{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
