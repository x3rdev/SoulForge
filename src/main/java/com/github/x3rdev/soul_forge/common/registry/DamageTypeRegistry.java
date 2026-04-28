package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulScytheProjectile;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class DamageTypeRegistry {

    private static final ResourceKey<DamageType> SCYTHE = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "scythe"));


    public static DamageSource scythe(RegistryAccess registryAccess, SoulScytheProjectile scythe,
                                                     Entity owner) {
        return new DamageSource(
                registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SCYTHE),
                scythe,
                owner
        );
    }
}
