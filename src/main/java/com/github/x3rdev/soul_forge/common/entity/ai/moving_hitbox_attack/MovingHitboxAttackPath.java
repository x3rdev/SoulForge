package com.github.x3rdev.soul_forge.common.entity.ai.moving_hitbox_attack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.stream.Stream;

public record MovingHitboxAttackPath(Vec3[] points) {

    public static final float SCALAR = 16F;

    public static final Codec<MovingHitboxAttackPath> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.listOf().fieldOf("points").xmap(
                            floats -> {
                                Vec3[] points = new Vec3[floats.size() / 3];
                                for (int i = 0; i < floats.size(); i += 3) {
                                    Vec3 v = new Vec3(floats.get(i)/SCALAR, floats.get(i + 1)/SCALAR, floats.get(i + 2)/SCALAR);
                                    points[i / 3] = v;
                                }
                                return points;
                            },
                            points -> Arrays.stream(points).flatMap(vec3 -> Stream.of(
                                    (float) vec3.x*SCALAR,
                                    (float) vec3.y*SCALAR,
                                    (float) vec3.z*SCALAR)
                            ).toList()).forGetter(MovingHitboxAttackPath::points)
            ).apply(instance, MovingHitboxAttackPath::new)
    );

    public Vec3 getPointForTick(int tick) {
        //TODO implement
        return points[tick];
    }
}
