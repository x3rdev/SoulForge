package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class SoulCandelabraBlock extends SoulCandleBlock {

    private static VoxelShape SHAPE_NORTH;
    private static VoxelShape SHAPE_EAST;
    private static VoxelShape SHAPE_SOUTH;
    private static VoxelShape SHAPE_WEST;

    public SoulCandelabraBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(SHAPE_NORTH == null) {
            SHAPE_NORTH = Stream.of(
                    Block.box(6.5, 4, 10, 9.5, 6, 16),
                    Block.box(6, 8, 9.5, 10, 10, 13.5),
                    Block.box(7, 6, 10.5, 9, 7, 12.5),
                    Block.box(7, 0, 10.5, 9, 4, 12.5),
                    Block.box(8, 0, 12.5, 8, 4, 16),
                    Block.box(6.5, 7, 10, 9.5, 8, 13),
                    Block.box(5.5, 10, 11.5, 10.5, 20, 11.5),
                    Block.box(6, 10, 9.5, 10, 13, 13.5),
                    Block.box(5.5, 10, 11.5, 10.5, 20, 11.5),
                    Block.box(0, 10, 9.5, 4, 12, 13.5),
                    Block.box(1, 2, 10.5, 3, 10, 12.5),
                    Block.box(-0.5, 12, 11.5, 4.5, 22, 11.5),
                    Block.box(-0.5, 12, 11.5, 4.5, 22, 11.5),
                    Block.box(0, 12, 9.5, 4, 15, 13.5),
                    Block.box(3, 1, 12, 7, 9, 12),
                    Block.box(3, 7, 10.5, 6.5, 8, 12.5),
                    Block.box(12, 10, 9.5, 16, 12, 13.5),
                    Block.box(13, 2, 10.5, 15, 10, 12.5),
                    Block.box(11.5, 12, 11.5, 16.5, 22, 11.5),
                    Block.box(11.5, 12, 11.5, 16.5, 22, 11.5),
                    Block.box(12, 12, 9.5, 16, 15, 13.5),
                    Block.box(9, 1, 12, 13, 9, 12),
                    Block.box(9.5, 7, 10.5, 13, 8, 12.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        }
        switch (pState.getValue(FACING)) {
            case EAST -> {
                if(SHAPE_EAST == null) {
                    SHAPE_EAST = BlockUtil.rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_NORTH);
                }
                return SHAPE_EAST;
            }
            case SOUTH -> {
                if(SHAPE_SOUTH == null) {
                    SHAPE_SOUTH = BlockUtil.rotateShape(Direction.NORTH, Direction.WEST, SHAPE_NORTH);
                }
                return SHAPE_SOUTH;
            }
            case WEST -> {
                if(SHAPE_WEST == null) {
                    SHAPE_WEST = BlockUtil.rotateShape(Direction.NORTH, Direction.EAST, SHAPE_NORTH);
                }
                return SHAPE_WEST;
            }
            default -> {
                return SHAPE_NORTH;
            }
        }
    }
}
