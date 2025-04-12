package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class SummonGhostsAttack extends ExtendedBehaviour<NergalEntity> {

    private int tickCount;

    public SummonGhostsAttack() {
        tickCount = 0;
    }

    @Override
    protected boolean shouldKeepRunning(NergalEntity entity) {
        int ticksSincePlayerHit = entity.tickCount-entity.lastHurtByPlayerTime;
        if(ticksSincePlayerHit == 0) {
            return false;
        }
        return super.shouldKeepRunning(entity);
    }

    @Override
    protected void tick(NergalEntity entity) {
        super.tick(entity);
        tickCount++;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }
}
