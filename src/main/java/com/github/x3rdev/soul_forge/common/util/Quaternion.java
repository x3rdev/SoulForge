package com.github.x3rdev.soul_forge.common.util;

// utility class for handling rotations and orientations
public class Quaternion {
    public static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);
    float x, y, z, r;

    public Quaternion(float x, float y, float z, float r) {
        this.x = x; // imaginary unit i
        this.y = y; // imaginary unit j
        this.z = z; // imaginary unit k
        this.r = r; // real component
    }

    public Quaternion(double x, double y, double z, double r) {
        this((float) x, (float) y, (float) z, (float) r);
    }

    public Quaternion(VecF vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.r = 0;
    }

    public static Quaternion attitude(float theta, VecF axis) {
        float a = (float) (theta * 0.5);
        return new Quaternion(axis.x * Math.sin(a), axis.y * Math.sin(a), axis.z * Math.sin(a), Math.cos(a));
    }

    public Quaternion inverse() {
        return new Quaternion(-x, -y, -z, r);
    }

    public Quaternion multiply(Quaternion q) {
        return new Quaternion(
                r * q.x + x * q.r + y * q.z - z * q.y,
                r * q.y - x * q.z + y * q.r + z * q.x,
                r * q.z + x * q.y - y * q.x + z * q.r,
                r * q.r - x * q.x - y * q.y - z * q.z
        );
    }

    public Quaternion scale(float scale) {
        return new Quaternion(x * scale, y * scale, z * scale, r * scale);
    }

    public Quaternion normalize() {
        if (this == ZERO) return new Quaternion(0, 0, 0, 1);
        return this.scale((float) (1 / Math.sqrt(this.multiply(this.inverse()).r)));
    }

    public VecF toVecF() {
        return new VecF(x, y, z);
    }

    @Override
    public Quaternion clone() {
        return new Quaternion(x, y, z, r);
    }
}
