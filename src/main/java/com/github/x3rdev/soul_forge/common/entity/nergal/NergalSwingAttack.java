package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.github.x3rdev.soul_forge.common.entity.ai.MovingHitboxAttack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animation.state.BoneSnapshot;

import java.util.List;
import java.util.Optional;

public class NergalSwingAttack extends MovingHitboxAttack<NergalEntity> {

    @Override
    protected AABB[] hurtBox(NergalEntity entity) {
        return new AABB[0];
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
    }
}
