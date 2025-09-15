package com.github.x3rdev.soul_forge.common.entity.nergal.brain;

import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;

public class NergalMoveToWalkTarget extends MoveToWalkTarget<Nergal> {

    @Override
    protected boolean hasReachedTarget(Nergal entity, WalkTarget target) {
        return target.getTarget().currentPosition().distanceToSqr(entity.position()) <= 5*5;
    }
}
