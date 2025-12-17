package com.github.x3rdev.soul_forge.common.enchantment;

import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ReapingEnchantmentEffect() implements EnchantmentEntityEffect {

    public static final MapCodec<ReapingEnchantmentEffect> CODEC = MapCodec.unit(ReapingEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if(!entity.isAlive()) {
            level.sendParticles(ParticleRegistry.SOUL_PARTICLE.get(), entity.getX(), entity.getY()+1, entity.getZ(), 10, 0, 0, 0, 0.5);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
