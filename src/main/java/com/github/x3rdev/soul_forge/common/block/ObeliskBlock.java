package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ObeliskBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty NETHER_STAR = BooleanProperty.create("nether_star");

    private static VoxelShape SHAPE_X_AXIS_TOP;
    private static VoxelShape SHAPE_Z_AXIS_TOP;
    private static VoxelShape SHAPE_X_AXIS_BOT;
    private static VoxelShape SHAPE_Z_AXIS_BOT;

    public ObeliskBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(NETHER_STAR, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        if(SHAPE_X_AXIS == null) {
            SHAPE_X_AXIS =
        }
        switch (direction.getAxis()) {
            case X -> {

            }
            case Z -> {

            }
            default -> {
                return
            }
        }
    }
}
