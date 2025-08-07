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

    private VoxelShape shapeNorth;
    private VoxelShape shapeEast;
    private VoxelShape shapeSouth;
    private VoxelShape shapeWest;

    public SoulCandelabraBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (shapeNorth == null) {
            shapeNorth = Stream.of(
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
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElseThrow();
        }
        switch (pState.getValue(FACING)) {
            case NORTH -> {
                return shapeNorth;
            }
            case EAST -> {
                if (shapeEast == null) {
                    shapeEast = BlockUtil.rotateShape(Direction.NORTH, Direction.SOUTH, shapeNorth);
                }
                return shapeEast;
            }
            case SOUTH -> {
                if (shapeSouth == null) {
                    shapeSouth = BlockUtil.rotateShape(Direction.NORTH, Direction.WEST, shapeNorth);
                }
                return shapeSouth;
            }
            case WEST -> {
                if (shapeWest == null) {
                    shapeWest = BlockUtil.rotateShape(Direction.NORTH, Direction.EAST, shapeNorth);
                }
                return shapeWest;
            }
            default -> throw new IllegalArgumentException();
        }
    }
}
