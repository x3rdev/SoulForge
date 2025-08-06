package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulwoodLianaBodyBlock extends GrowingPlantBodyBlock {

    public static final MapCodec<SoulwoodLianaBodyBlock> CODEC = simpleCodec(SoulwoodLianaBodyBlock::new);
    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public SoulwoodLianaBodyBlock(Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) || level.getBlockState(pos.above()).is(BlockRegistry.SOULWOOD_LEAVES);
    }

    @Override
    protected MapCodec<? extends GrowingPlantBodyBlock> codec() {
        return CODEC;
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return BlockRegistry.SOULWOOD_LIANA.get();
    }
}
