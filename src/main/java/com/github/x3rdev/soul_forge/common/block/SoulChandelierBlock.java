package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulChandelierBlock extends Block {
    private static VoxelShape SHAPE;

    public SoulChandelierBlock(Properties pProperties) {
        super(pProperties.lightLevel(value -> 15));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(SHAPE == null) {
            SHAPE = Shapes.join(Block.box(4, 1, 4, 12, 3, 12), Block.box(6, 3, 6, 10, 16, 10), BooleanOp.OR);
        }
        return SHAPE;
    }
}
