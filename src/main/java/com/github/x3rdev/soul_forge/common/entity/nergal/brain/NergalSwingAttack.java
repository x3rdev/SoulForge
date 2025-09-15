package com.github.x3rdev.soul_forge.common.entity.nergal.brain;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.brain.MovingHitboxAttack;
import com.github.x3rdev.soul_forge.common.entity.brain.MovingHitboxAttackPath;
import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class NergalSwingAttack extends MovingHitboxAttack<Nergal> {

    public static final ResourceKey<MovingHitboxAttackPath> NERGAL_SWING_PATH = ResourceKey.create(DatapackRegistry.MOVING_HITBOX_ATTACK_PATH_KEY,
            ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "nergal_swing"));

    public static final Predicate<Nergal> IS_VALID_PREDICATE = (nergal) -> {
        LivingEntity target = nergal.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
        return target.isAlive() && nergal.canAttack(target) && target.distanceToSqr(nergal) < 5*5;
    };

    public NergalSwingAttack() {
        super(NERGAL_SWING_PATH);
        this.startCondition(IS_VALID_PREDICATE);
        this.runFor(nergal -> 3*20 + 20);
    }

    @Override
    protected void start(ServerLevel level, Nergal entity, long gameTime) {
        super.start(level, entity, gameTime);
        entity.triggerAnim("c", "nergal_swing");
        entity.resetTicksAttacking();
    }

    @Override
    protected void tick(ServerLevel level, Nergal entity, long gameTime) {
        super.tick(level, entity, gameTime);
        entity.incrementTicksAttacking();
        AABB hurtBox = hurtBox(entity, Math.min(getSwingPath(entity).points().length-1, entity.getTicksAttacking()+4));
        entity.getEntityData().set(Nergal.DEBUG_ATTACK_BOX, hurtBox);
        if(entity.getTicksAttacking() > 19 && entity.getTicksAttacking() < 32) {
            level.getEntities(entity, hurtBox.move(entity.position()), EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(entity.attackablePredicate()))
                    .forEach(entity::doHurtTarget);
        }
    }

    @Override
    protected boolean shouldKeepRunning(Nergal entity) {
        return true;
    }

    @Override
    protected void stop(ServerLevel level, Nergal entity, long gameTime) {
        super.stop(level, entity, gameTime);
        entity.stopTriggeredAnim("c", "nergal_swing");
        entity.resetTicksAttacking();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected AABB hurtBox(Nergal entity, long tick) {
        Vec3 center = getSwingPath(entity).getPointForTick((int) tick)
                .yRot(Mth.DEG_TO_RAD*(180-entity.yBodyRot));
        return AABB.ofSize(center, 3.5, 5, 3.5);
    }
}
