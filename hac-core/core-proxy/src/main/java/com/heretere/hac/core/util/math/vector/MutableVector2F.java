package com.heretere.hac.core.util.math.vector;

public class MutableVector2F {
    private float x;
    private float y;

    public MutableVector2F(double x, double y) {
        this((float) x, (float) y);
    }

    public MutableVector2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public MutableVector2F add(MutableVector2F other) {
        return this.add(other.x, other.y);
    }

    public MutableVector2F add(double x, double y) {
        return this.add((float) x, (float) y);
    }

    public MutableVector2F add(float x, float y) {
        this.x += x;
        this.y += y;

        return this;
    }

    public MutableVector2F subtract(MutableVector2F other) {
        return this.subtract(other.x, other.y);
    }

    public MutableVector2F subtract(double x, double y) {
        return this.subtract((float) x, (float) y);
    }

    public MutableVector2F subtract(float x, float y) {
        this.x -= x;
        this.y -= y;

        return this;
    }

    public MutableVector2F set(MutableVector2F other) {
        return this.set(other.x, other.y);
    }

    public MutableVector2F set(double x, double y) {
        return this.set((float) x, (float) y);
    }

    public MutableVector2F set(float x, float y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public MutableVector2F setX(float x) {
        this.x = x;
        return this;
    }

    public MutableVector2F setY(float y) {
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return "MutableVector2F{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
