package com.github.x3rdev.soul_forge.common.entity.ai;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public abstract class MovingHitboxAttack<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private final ResourceKey<MovingHitboxAttackPath> swingPathKey;
    private MovingHitboxAttackPath path;

    public MovingHitboxAttack(ResourceKey<MovingHitboxAttackPath> swingPathKey) {
        this.swingPathKey = swingPathKey;
    }

    protected MovingHitboxAttackPath getSwingPath(E entity) {
        if(path == null) {
            path = entity.level().registryAccess().lookup(MovingHitboxAttackRegistry.MOVING_HITBOX_ATTACK_PATH_KEY).orElseThrow().get(swingPathKey).orElseThrow().value();
        }
        return path;
    }

    protected abstract AABB hurtBox(E entity, long tick);

}
