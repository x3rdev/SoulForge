package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.worldgen.BiomeModifierBootstrap;
import com.github.x3rdev.soul_forge.common.worldgen.ConfiguredFeatureBootstrap;
import com.github.x3rdev.soul_forge.common.worldgen.PlacedFeatureBootstrap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SoulForgeDatapackProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierBootstrap::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureBootstrap::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureBootstrap::bootstrap);

    public SoulForgeDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(SoulForge.MOD_ID));
    }
}
