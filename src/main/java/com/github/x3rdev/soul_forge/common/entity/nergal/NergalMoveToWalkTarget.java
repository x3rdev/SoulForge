package com.github.x3rdev.soul_forge.common.entity.nergal;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;

public class NergalMoveToWalkTarget extends MoveToWalkTarget<NergalEntity> {

    @Override
    protected void tick(NergalEntity entity) {
        super.tick(entity);
    }

    @Override
    protected boolean hasReachedTarget(NergalEntity entity, WalkTarget target) {
        return target.getTarget().currentBlockPosition().distSqr(entity.blockPosition()) <= 5*5;
    }
}
