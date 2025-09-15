package com.github.x3rdev.soul_forge.common.entity.nergal.brain;

import com.github.x3rdev.soul_forge.common.entity.SoulScytheProjectile;
import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.function.Predicate;

public class NergalScytheThrowAttack extends ExtendedBehaviour<Nergal> {

    public static final Predicate<Nergal> IS_VALID_PREDICATE = (nergal) -> {
        LivingEntity target = nergal.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
        return target.isAlive() && nergal.canAttack(target) && target.distanceToSqr(nergal) > 10*10;
    };

    public NergalScytheThrowAttack() {
        startCondition(IS_VALID_PREDICATE);
        this.runFor((nergal) -> 2 * 20);
    }

    @Override
    protected void start(ServerLevel level, Nergal entity, long gameTime) {
        super.start(level, entity, gameTime);
        entity.resetTicksAttacking();
    }

    @Override
    protected void tick(ServerLevel level, Nergal nergal, long gameTime) {
        super.tick(level, nergal, gameTime);
        nergal.incrementTicksAttacking();
        if(nergal.getTicksAttacking() == 10) {
            SoulScytheProjectile projectile = new SoulScytheProjectile(level, nergal);
            projectile.setPos(nergal.getEyePosition().add(0, 1F, 0).add(nergal.getLookAngle().normalize()));
            projectile.shootFromRotation(nergal, nergal.getXRot(), nergal.getYRot(), 0.0F, 1.0F, 0.05F);
            level.addFreshEntity(projectile);
        }
    }

    @Override
    protected boolean shouldKeepRunning(Nergal entity) {
        return true;
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
