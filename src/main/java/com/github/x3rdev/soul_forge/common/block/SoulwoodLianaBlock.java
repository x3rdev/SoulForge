package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulwoodLianaBlock extends GrowingPlantHeadBlock {

    public static final MapCodec<SoulwoodLianaBlock> CODEC = simpleCodec(SoulwoodLianaBlock::new);
    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public SoulwoodLianaBlock(Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false, 0.1F);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) || level.getBlockState(pos.above()).is(BlockRegistry.SOULWOOD_LEAVES);
    }

    @Override
    protected MapCodec<? extends GrowingPlantHeadBlock> codec() {
        return CODEC;
    }

    @Override
    protected Block getBodyBlock() {
        return BlockRegistry.SOULWOOD_LIANA_BODY.get();
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return random.nextInt(1,4);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }
}
