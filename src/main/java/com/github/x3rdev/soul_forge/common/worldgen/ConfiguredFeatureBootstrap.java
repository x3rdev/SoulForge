package com.github.x3rdev.soul_forge.common.worldgen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block.SoulwoodSaplingBlock;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ConfiguredFeatureBootstrap {

    public static final ResourceKey<ConfiguredFeature<?, ?>> SOUL_CRYSTAL_ORE = registerKey("soul_crystal_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SOULWOOD_TREE = registerKey("soulwood_tree");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldSoulCrystalOres = List.of(
                OreConfiguration.target(stoneReplaceables, BlockRegistry.SOUL_CRYSTAL_ORE.get().defaultBlockState())
        );

        context.register(SOUL_CRYSTAL_ORE, new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(overworldSoulCrystalOres, 12)));

        context.register(SOULWOOD_TREE, new ConfiguredFeature<>(Feature.TREE, SoulwoodSaplingBlock.createSoulwood().build()));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, name));
    }
}
