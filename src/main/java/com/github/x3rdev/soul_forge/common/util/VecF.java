package com.github.x3rdev.soul_forge.common.util;

import net.minecraft.core.Direction;

// utility class for future rendering stuff
public class VecF {
    public static final VecF ZERO = new VecF(0, 0, 0);
    public float x;
    public float y;
    public float z;

    public VecF(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VecF add(VecF v) {
        return new VecF(x + v.x, y + v.y, z + v.z);
    }

    public VecF negate() {
        return new VecF(-x, -y, -z);
    }

    public VecF scale(float scale) {
        return new VecF(x * scale, y * scale, z * scale);
    }

    public float dot(VecF v1, VecF v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public VecF cross(VecF v1, VecF v2) {
        return new VecF(v1.y * v2.z - v2.y * v1.z, v1.z * v2.x - v2.z * v1.x, v1.x * v2.y - v2.x * v1.y);
    }

    public VecF rotate(VecF axis, float theta) {
        Quaternion attitude = Quaternion.attitude(theta, axis).normalize();
        return attitude.multiply(new Quaternion(this)).multiply(attitude.inverse()).toVecF();
    }

    public VecF rotateCounterClockwise(Direction axis) {
        return switch (axis) {
            case UP -> new VecF(z, y, -x);
            case DOWN -> new VecF(-z, y, x);
            case EAST -> new VecF(x, -z, y);
            case WEST -> new VecF(x, z, -y);
            case SOUTH -> new VecF(-y, x, z);
            case NORTH -> new VecF(y, -x, z);
        };
    }

    public VecF rotateClockwise(Direction axis) {
        return switch (axis) {
            case UP -> new VecF(-z, y, x);
            case DOWN -> new VecF(z, y, -x);
            case EAST -> new VecF(x, z, -y);
            case WEST -> new VecF(x, -z, y);
            case SOUTH -> new VecF(y, -x, z);
            case NORTH -> new VecF(-y, x, z);
        };
    }

    @Override
    public VecF clone() {
        return new VecF(x, y, z);
    }
}
