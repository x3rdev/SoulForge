package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.ai.moving_hitbox_attack.MovingHitboxAttack;
import com.github.x3rdev.soul_forge.common.entity.ai.moving_hitbox_attack.MovingHitboxAttackPath;
import com.github.x3rdev.soul_forge.common.entity.ai.moving_hitbox_attack.MovingHitboxAttackRegistry;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NergalSwingAttack extends MovingHitboxAttack<NergalEntity> {

    public static final ResourceKey<MovingHitboxAttackPath> NERGAL_SWING_PATH = ResourceKey.create(MovingHitboxAttackRegistry.MOVING_HITBOX_ATTACK_PATH_KEY,
            ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "nergal_swing"));

    public NergalSwingAttack() {
        super(NERGAL_SWING_PATH);
        this.startCondition((nergal) -> nergal.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow().distanceToSqr(nergal) < 6*6);
    }

    @Override
    protected void start(ServerLevel level, NergalEntity entity, long gameTime) {
        super.start(level, entity, gameTime);
        entity.triggerAnim("c", "attack1");
    }

    @Override
    protected void tick(ServerLevel level, NergalEntity entity, long gameTime) {
        super.tick(level, entity, gameTime);
        entity.getEntityData().set(NergalEntity.DEBUG_ATTACK_BOX, hurtBox(entity));
        Vec3 pos = hurtBox(entity).getCenter().add(entity.position());
        level.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected AABB hurtBox(NergalEntity entity) {
        return AABB.ofSize(getSwingPath(entity).getPointForTick(0).add(entity.position()), 1, 1, 1);
    }
}
