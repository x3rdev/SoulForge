package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block.SoulwoodSaplingBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FoliagePlacerTypeRegistry {

    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<?>> SOULWOOD = FOLIAGE_PLACER_TYPES.register("soulwood",
            () -> new FoliagePlacerType<>(SoulwoodSaplingBlock.SoulwoodFoliagePlacer.CODEC));
}
