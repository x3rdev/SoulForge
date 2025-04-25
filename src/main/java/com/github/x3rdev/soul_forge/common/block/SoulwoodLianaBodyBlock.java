package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.phys.shapes.Shapes;

public class SoulwoodLianaBodyBlock extends GrowingPlantBodyBlock {

    public static final MapCodec<SoulwoodLianaBodyBlock> CODEC = simpleCodec(SoulwoodLianaBodyBlock::new);

    public SoulwoodLianaBodyBlock(Properties properties) {
        super(properties, Direction.DOWN, Shapes.block(), false);
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
