package com.github.x3rdev.soul_forge.common.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "soul_forge");

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_PARTICLE = PARTICLE_TYPES.register(
    "soul_particle",
            () -> new SimpleParticleType(false)
    );
}