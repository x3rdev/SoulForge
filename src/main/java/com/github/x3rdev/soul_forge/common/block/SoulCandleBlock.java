package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class SoulCandleBlock extends WallTorchBlock {

    private static VoxelShape SHAPE_NORTH;
    private static VoxelShape SHAPE_EAST;
    private static VoxelShape SHAPE_SOUTH;
    private static VoxelShape SHAPE_WEST;

    public SoulCandleBlock(Properties pProperties) {
        super(pProperties, ParticleTypes.SOUL_FIRE_FLAME);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(SHAPE_NORTH == null) {
            SHAPE_NORTH = Stream.of(
                    Block.box(8, 7, 7.5, 8, 13, 9.5),
                    Block.box(6.5, 4, 10, 9.5, 6, 16),
                    Block.box(6, 8, 9.5, 10, 10, 13.5),
                    Block.box(7, 6, 10.5, 9, 7, 12.5),
                    Block.box(7, 0, 10.5, 9, 4, 12.5),
                    Block.box(8, 0, 12.5, 8, 4, 16),
                    Block.box(6.5, 7, 10, 9.5, 8, 13),
                    Block.box(10, 7, 11.5, 12, 13, 11.5),
                    Block.box(4, 7, 11.5, 6, 13, 11.5),
                    Block.box(8, 7, 13.5, 8, 13, 15.5)
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

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

    }

    @Override
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(this));
        }

        return this.descriptionId;
    }
}
