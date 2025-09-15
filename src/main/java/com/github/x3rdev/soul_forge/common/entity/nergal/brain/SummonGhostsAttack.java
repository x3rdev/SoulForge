package com.github.x3rdev.soul_forge.common.entity.nergal.brain;

import com.github.x3rdev.soul_forge.common.entity.Ghost;
import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class SummonGhostsAttack extends ExtendedBehaviour<Nergal> {

    public SummonGhostsAttack() {
        startCondition(nergalEntity -> {
           List<Entity> entities = nergalEntity.level().getEntities(nergalEntity, nergalEntity.getBoundingBox().inflate(10));
           return entities.stream().noneMatch(entity -> entity.getType().equals(EntityRegistry.GHOST.get()));
        });
        runFor(nergalEntity -> 100);
    }

    @Override
    protected boolean shouldKeepRunning(Nergal entity) {
        int ticksSincePlayerHit = entity.tickCount - entity.lastHurtByPlayerTime;
        return ticksSincePlayerHit != 0;
    }

    @Override
    protected void start(Nergal entity) {
        entity.triggerAnim("c", "nergal_summon");
        entity.resetTicksAttacking();
    }

    @Override
    protected boolean doStartCheck(ServerLevel level, Nergal entity, long gameTime) {
        return super.doStartCheck(level, entity, gameTime);
    }

    @Override
    protected void tick(Nergal entity) {
        entity.incrementTicksAttacking();
        entity.setDeltaMovement(0,0,0);

        if(entity.getTicksAttacking() == 13) {
            summonGhosts(entity, entity.level(), 8);
        }
    }

    private void summonGhosts(Nergal entity, Level level, int count) {
        float angle = Mth.TWO_PI/count;
        for (int i = 0; i < count; i++) {
            Vec3 spawnPos = entity.position().add(new Vec3(8,0,0).yRot(i*angle));
            Ghost ghostEntity = new Ghost(level);
            ghostEntity.setPos(spawnPos);
            level.addFreshEntity(ghostEntity);
        }
    }

    @Override
    protected void stop(ServerLevel level, Nergal entity, long gameTime) {
        super.stop(level, entity, gameTime);
        entity.resetTicksAttacking();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }
}
