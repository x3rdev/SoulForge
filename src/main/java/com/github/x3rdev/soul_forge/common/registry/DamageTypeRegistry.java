package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulScytheProjectileEntity;
import com.github.x3rdev.soul_forge.common.item.SoulScytheItem;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DamageTypeRegistry {

    private final Registry<DamageType> damageTypes;

    private final ResourceKey<DamageType> scythe = ResourceKey.create(Registries.DAMAGE_TYPE,
            new ResourceLocation(SoulForge.MOD_ID, "scythe"));

    public DamageTypeRegistry(RegistryAccess registryAccess) {
        this.damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    public DamageSource scythe(SoulScytheProjectileEntity scytheEntity, Entity owner) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(scythe), scytheEntity, owner);
    }
}
