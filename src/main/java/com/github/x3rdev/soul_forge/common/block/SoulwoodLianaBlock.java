package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulwoodLianaBlock extends GrowingPlantHeadBlock {

    public static final MapCodec<SoulwoodLianaBlock> CODEC = simpleCodec(SoulwoodLianaBlock::new);
    protected static final VoxelShape SHAPE = Block.box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public SoulwoodLianaBlock(Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false, 0.1F);
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
