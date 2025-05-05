package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.TrunkPlacerTypeRegistry;
import com.github.x3rdev.soul_forge.common.worldgen.ConfiguredFeatureBootstrap;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SoulwoodSaplingBlock extends SaplingBlock {

    public SoulwoodSaplingBlock(Properties properties) {
        super(soulwoodTreeGrower(), properties);
    }

    public static TreeGrower soulwoodTreeGrower() {
        return new TreeGrower("soulwood", Optional.empty(), Optional.of(ConfiguredFeatureBootstrap.SOULWOOD_TREE), Optional.empty());
    }

    public static TreeConfiguration.TreeConfigurationBuilder createSoulwood() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockRegistry.SOULWOOD_LOG.get()),
                new SoulwoodTrunkPlacer(6, 3, 4),
                BlockStateProvider.simple(BlockRegistry.SOULWOOD_LEAVES.get()),
                new BushFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), 1),
                Optional.empty(),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }

    public static class SoulwoodTrunkPlacer extends TrunkPlacer {

        public static final MapCodec<SoulwoodTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
                instance -> trunkPlacerParts(instance).apply(instance, SoulwoodTrunkPlacer::new)
        );

        public SoulwoodTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
            super(baseHeight, heightRandA, heightRandB);
        }

        @Override
        protected TrunkPlacerType<?> type() {
            return TrunkPlacerTypeRegistry.SOULWOOD.get();
        }

        @Override
        public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
            setDirtAt(level, blockSetter, random, pos.below(), config);
            List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();

            for (int i = 0; i < baseHeight; i++) {
                placeLog(level, blockSetter, random, pos.above(i), config);
            }
            Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
                int max = random.nextInt(1, 1+baseHeight/2);
                for (int i = 0; i < max; i++) {
                    placeLog(level, blockSetter, random, pos.relative(direction).above(i), config);
                }
            });

            int[] horizontalAngles = new int[random.nextInt(3, 5)];
            int initialDegree = random.nextInt(0, 360);
            for (int i = 0; i < horizontalAngles.length; i++) {
                horizontalAngles[i] = initialDegree;
                initialDegree += (360/horizontalAngles.length) - random.nextInt(-20, 20);
            }
            for (int horizontalAngle : horizontalAngles) {
                for (int j = 0; j < 18; j++) {
                    Vec3 precisePose = pos.above(baseHeight).getCenter().add(new Vec3(j/3F, branchHeightFunction(j/3F), 0).yRot(Mth.DEG_TO_RAD * horizontalAngle));
                    BlockPos branchPos = BlockPos.containing(precisePose).below(1);
                    placeLog(level, blockSetter, random, branchPos, config);
                    if(j > 8 && j < 17) {
                        list.add(new FoliagePlacer.FoliageAttachment(branchPos.above(), 1, false));
                    }
                }
            }

            return list;
        }

        private static float branchHeightFunction(float x) {
            return (float) (-(1F/5)*Math.pow(x, 1/1.2F)*(x-11.5));
        }

    }

    public static class SoulwoodFoliagePlacer extends FoliagePlacer {

        public static final MapCodec<SoulwoodFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
                instance -> foliagePlacerParts(instance).apply(instance, SoulwoodFoliagePlacer::new)
        );

        public SoulwoodFoliagePlacer(IntProvider radius, IntProvider offset) {
            super(radius, offset);
        }

        @Override
        protected FoliagePlacerType<?> type() {
            return null;
        }

        @Override
        protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {

        }

        @Override
        public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
            return 0;
        }

        @Override
        protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
            return false;
        }
    }
}
