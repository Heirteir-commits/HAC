package com.heretere.hac.core.util.math;

import com.flowpowered.math.vector.Vector3f;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Boxf {
    private final Vector3f min;
    private final Vector3f max;

    public Boxf expand(double maxXOffset, double maxYOffset, double maxZOffset) {
        return new Boxf(this.min, new Vector3f(this.max.add(maxXOffset, maxYOffset, maxZOffset)));
    }

    public Boxf offset(double x, double y, double z) {
        return new Boxf(this.min.add(x, y, z), this.max.add(x, y, z));
    }

    public boolean contains(Vector3f point) {
        return this.contains(point.getX(), point.getY(), point.getZ());
    }

    public boolean contains(double x, double y, double z) {
        return x <= this.max.getX() && x >= this.min.getX() &&
                y <= this.max.getY() && y >= this.min.getY() &&
                z <= this.max.getZ() && z >= this.min.getZ();
    }

    public boolean intersects(Boxf other) {
        return other.min.getX() <= this.max.getX() && other.max.getX() >= this.min.getX() &&
                other.min.getY() <= this.max.getY() && other.max.getY() >= this.min.getY() &&
                other.min.getZ() <= this.max.getZ() && other.max.getZ() >= this.min.getZ();
    }

    @Override
    public String toString() {
        return "box[" + min + " -> " + max + "]";
    }
}
