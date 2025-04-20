package com.github.x3rdev.soul_forge.common.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public abstract class MovingHitboxAttack<E extends LivingEntity> extends ExtendedBehaviour<E> {

    protected abstract AABB[] hurtBox(E entity);

}
