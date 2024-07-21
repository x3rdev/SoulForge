package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ObeliskBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty NETHER_STAR = BooleanProperty.create("nether_star");

    private static VoxelShape SHAPE_X_AXIS_TOP;
    private static VoxelShape SHAPE_Z_AXIS_TOP;
    private static VoxelShape SHAPE_X_AXIS_BOT;
    private static VoxelShape SHAPE_Z_AXIS_BOT;

    public ObeliskBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(NETHER_STAR, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isCreative()) {
            DoublePlantBlock.preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(NETHER_STAR, false).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, FACING, NETHER_STAR);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = state.getValue(FACING);
        switch (direction.getAxis()) {
            case X -> {
                if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    if(SHAPE_X_AXIS_TOP == null) {
                        SHAPE_X_AXIS_TOP = Stream.of(
                                Block.box(0.5, 16, 4, 15.5, 32, 13),
                                Block.box(0.5, 21, 3, 15.5, 32, 4),
                                Block.box(8.5, 20, 3, 15.5, 21, 4),
                                Block.box(9.5, 19, 3, 15.5, 20, 4),
                                Block.box(10.5, 17, 3, 15.5, 19, 4),
                                Block.box(12.5, 16, 3, 15.5, 17, 4),
                                Block.box(0.5, 17, 3, 5.5, 19, 4),
                                Block.box(0.5, 16, 3, 3.5, 17, 4),
                                Block.box(0.5, 19, 3, 6.5, 20, 4),
                                Block.box(0.5, 20, 3, 7.5, 21, 4)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_X_AXIS_TOP;
                } else {
                    if(SHAPE_X_AXIS_BOT == null) {
                        SHAPE_X_AXIS_BOT = Stream.of(
                                Block.box(0, 0, 0, 16, 4, 16),
                                Block.box(2.5, 4, 4, 13.5, 7, 12),
                                Block.box(0.5, 7, 4, 15.5, 16, 13),
                                Block.box(13.5, 15, 3, 15.5, 16, 4),
                                Block.box(14.5, 14, 3, 15.5, 15, 4),
                                Block.box(0.5, 15, 3, 2.5, 16, 4),
                                Block.box(0.5, 13, 3, 2.5, 14, 4),
                                Block.box(0.5, 10, 3, 5.5, 12, 4),
                                Block.box(0.5, 12, 3, 3.5, 13, 4),
                                Block.box(0.5, 9, 3, 6.5, 10, 4),
                                Block.box(0.5, 8, 3, 7.5, 9, 4),
                                Block.box(8.5, 8, 3, 15.5, 9, 4),
                                Block.box(9.5, 9, 3, 15.5, 10, 4),
                                Block.box(10.5, 10, 3, 15.5, 12, 4),
                                Block.box(12.5, 12, 3, 15.5, 13, 4),
                                Block.box(13.5, 13, 3, 15.5, 14, 4),
                                Block.box(0.5, 14, 3, 1.5, 15, 4),
                                Block.box(0.5, 7, 3, 15.5, 8, 4)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_X_AXIS_BOT;
                }
            }
            case Z -> {
                if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    if(SHAPE_Z_AXIS_TOP == null) {
                        SHAPE_Z_AXIS_TOP = Stream.of(
                                Block.box(4, 16, 0.5, 13, 32, 15.5),
                                Block.box(3, 21, 0.5, 4, 32, 15.5),
                                Block.box(3, 20, 0.5, 4, 21, 7.5),
                                Block.box(3, 19, 0.5, 4, 20, 6.5),
                                Block.box(3, 17, 0.5, 4, 19, 5.5),
                                Block.box(3, 16, 0.5, 4, 17, 3.5),
                                Block.box(3, 17, 10.5, 4, 19, 15.5),
                                Block.box(3, 16, 12.5, 4, 17, 15.5),
                                Block.box(3, 19, 9.5, 4, 20, 15.5),
                                Block.box(3, 20, 8.5, 4, 21, 15.5)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_Z_AXIS_TOP;
                } else {
                    if(SHAPE_Z_AXIS_BOT == null) {
                        SHAPE_Z_AXIS_BOT = Stream.of(
                                Block.box(0, 0, 0, 16, 4, 16),
                                Block.box(4, 4, 2.5, 12, 7, 13.5),
                                Block.box(3, 7, 0.5, 12, 16, 15.5),
                                Block.box(12, 15, 13.5, 13, 16, 15.5),
                                Block.box(12, 14, 14.5, 13, 15, 15.5),
                                Block.box(12, 15, 0.5, 13, 16, 2.5),
                                Block.box(12, 13, 0.5, 13, 14, 2.5),
                                Block.box(12, 10, 0.5, 13, 12, 5.5),
                                Block.box(12, 12, 0.5, 13, 13, 3.5),
                                Block.box(12, 9, 0.5, 13, 10, 6.5),
                                Block.box(12, 8, 0.5, 13, 9, 7.5),
                                Block.box(12, 8, 8.5, 13, 9, 15.5),
                                Block.box(12, 9, 9.5, 13, 10, 15.5),
                                Block.box(12, 10, 10.5, 13, 12, 15.5),
                                Block.box(12, 12, 12.5, 13, 13, 15.5),
                                Block.box(12, 13, 13.5, 13, 14, 15.5),
                                Block.box(12, 14, 0.5, 13, 15, 1.5),
                                Block.box(12, 7, 0.5, 13, 8, 15.5)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_Z_AXIS_BOT;
                }
            }
        }
        return Shapes.block();
    }
}
