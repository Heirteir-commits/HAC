package com.heretere.hac.core.util.math.vector;

import com.flowpowered.math.GenericMath;

public class MutableVector3F {
    private float x;
    private float y;
    private float z;

    public MutableVector3F(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    public MutableVector3F(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MutableVector3F add(MutableVector3F other) {
        return this.add(other.x, other.y, other.z);
    }

    public MutableVector3F add(double x, double y, double z) {
        return this.add((float) x, (float) y, (float) z);
    }

    public MutableVector3F add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public MutableVector3F subtract(MutableVector3F other) {
        return this.subtract(other.x, other.y, other.z);
    }

    public MutableVector3F subtract(double x, double y, double z) {
        return this.subtract((float) x, (float) y, (float) z);
    }

    public MutableVector3F subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    public MutableVector3F mul(MutableVector3F other) {
        return this.mul(other.x, other.y, other.z);
    }

    public MutableVector3F mul(double x, double y, double z) {
        return this.mul((float) x, (float) y, (float) z);
    }

    public MutableVector3F mul(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;

        return this;
    }

    public MutableVector3F set(MutableVector3F other) {
        return this.set(other.x, other.y, other.z);
    }

    public MutableVector3F set(double x, double y, double z) {
        return this.set((float) x, (float) y, (float) z);
    }

    public MutableVector3F set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public MutableVector3F setX(float x) {
        this.x = x;
        return this;
    }

    public MutableVector3F setY(float y) {
        this.y = y;
        return this;
    }

    public MutableVector3F setZ(float z) {
        this.z = z;
        return this;
    }

    public float length() {
        return (float) GenericMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public MutableVector3F copy() {
        return new MutableVector3F(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "MutableVector3F{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }


}
