package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.github.x3rdev.soul_forge.common.entity.GhostEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class SummonGhostsAttack extends ExtendedBehaviour<NergalEntity> {

    int tickCount;

    public SummonGhostsAttack() {
        runFor(nergalEntity -> 100);
    }

    @Override
    protected boolean shouldKeepRunning(NergalEntity entity) {
        int ticksSincePlayerHit = entity.tickCount - entity.lastHurtByPlayerTime;
        if (ticksSincePlayerHit == 0) {
            return false;
        }
        return super.shouldKeepRunning(entity);
    }

    @Override
    protected void start(NergalEntity entity) {
        tickCount = 0;
    }

    @Override
    protected void tick(NergalEntity entity) {
        if(tickCount == 50) {
            summonGhosts(entity, entity.level(), 1);
        }
        tickCount++;
    }

    private void summonGhosts(NergalEntity entity, Level level, int count) {
        for (int i = 0; i < count; i++) {
            Vec3 spawnPos = entity.position();
            GhostEntity ghostEntity = new GhostEntity(level);
            for (int j = -2; j <= 6; j++) { // Search height
                ghostEntity.setPos(spawnPos.add(0,j,0));
                if(ghostEntity.checkSpawnObstruction(level)) {
                    level.addFreshEntity(ghostEntity);
                    break;
                }
            }
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }
}
