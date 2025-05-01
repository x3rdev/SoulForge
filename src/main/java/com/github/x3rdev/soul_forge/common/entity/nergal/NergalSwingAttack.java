package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.ai.MovingHitboxAttack;
import com.github.x3rdev.soul_forge.common.entity.ai.MovingHitboxAttackPath;
import com.github.x3rdev.soul_forge.common.entity.ai.MovingHitboxAttackRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class NergalSwingAttack extends MovingHitboxAttack<NergalEntity> {

    public static final ResourceKey<MovingHitboxAttackPath> NERGAL_SWING_PATH = ResourceKey.create(MovingHitboxAttackRegistry.MOVING_HITBOX_ATTACK_PATH_KEY,
            ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "nergal_swing"));

    public static final Predicate<NergalEntity> IS_VALID_PREDICATE = (nergal) -> {
        LivingEntity target = nergal.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
        return target.isAlive() && nergal.canAttack(target) && target.distanceTo(nergal) < 6*6;
    };

    public NergalSwingAttack() {
        super(NERGAL_SWING_PATH);
        this.startCondition(IS_VALID_PREDICATE);
        this.runFor((nergal) -> 3*20 + 20);
    }

    @Override
    protected void start(ServerLevel level, NergalEntity entity, long gameTime) {
        super.start(level, entity, gameTime);
        entity.triggerAnim("c", "attack1");
        entity.resetTicksAttacking();
    }

    @Override
    protected void tick(ServerLevel level, NergalEntity nergal, long gameTime) {
        super.tick(level, nergal, gameTime);
        AABB hurtBox = hurtBox(nergal, Math.min(getSwingPath(nergal).points().length - 1, nergal.getTicksAttacking() + 6));
        nergal.getEntityData().set(NergalEntity.DEBUG_ATTACK_BOX, hurtBox);
        nergal.incrementTicksAttacking();
        level.getEntities(nergal, hurtBox).forEach(entity -> {
            entity.hurt(nergal.damageSources().mobAttack(nergal), 6);
        });
    }

    @Override
    protected boolean shouldKeepRunning(NergalEntity entity) {
        return true;
    }

    @Override
    protected void stop(ServerLevel level, NergalEntity entity, long gameTime) {
        super.stop(level, entity, gameTime);
        entity.stopTriggeredAnim("c", "attack1");
        entity.resetTicksAttacking();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected AABB hurtBox(NergalEntity entity, long tick) {
        Vec3 center = getSwingPath(entity).getPointForTick((int) tick)
                .yRot(Mth.DEG_TO_RAD*(180-entity.yBodyRot));
        return AABB.ofSize(center, 2.5, 2.5, 2.5);
    }
}
