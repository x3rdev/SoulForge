package com.github.x3rdev.soul_forge.common.entity.nergal;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;

public class NergalMoveToWalkTarget extends MoveToWalkTarget<NergalEntity> {

    @Override
    protected boolean hasReachedTarget(NergalEntity entity, WalkTarget target) {
        return target.getTarget().currentPosition().distanceToSqr(entity.position()) <= 5*5;
    }
}
