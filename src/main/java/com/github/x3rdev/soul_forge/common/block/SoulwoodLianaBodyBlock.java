package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulwoodLianaBodyBlock extends GrowingPlantBodyBlock {

    public static final MapCodec<SoulwoodLianaBodyBlock> CODEC = simpleCodec(SoulwoodLianaBodyBlock::new);
    protected static final VoxelShape SHAPE = Block.box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public SoulwoodLianaBodyBlock(Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false);
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
