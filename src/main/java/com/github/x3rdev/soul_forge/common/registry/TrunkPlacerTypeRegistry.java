package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block.SoulwoodSaplingBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TrunkPlacerTypeRegistry {

    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<?>> SOULWOOD = TRUNK_PLACER_TYPES.register("soulwood",
            () -> new TrunkPlacerType<>(SoulwoodSaplingBlock.SoulwoodTrunkPlacer.CODEC));
}
