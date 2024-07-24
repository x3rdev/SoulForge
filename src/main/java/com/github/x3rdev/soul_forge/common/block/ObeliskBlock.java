package com.github.x3rdev.soul_forge.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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

    private static VoxelShape SHAPE_NORTH_TOP;
    private static VoxelShape SHAPE_EAST_TOP;
    private static VoxelShape SHAPE_SOUTH_TOP;
    private static VoxelShape SHAPE_WEST_TOP;
    private static VoxelShape SHAPE_NORTH_BOT;
    private static VoxelShape SHAPE_EAST_BOT;
    private static VoxelShape SHAPE_SOUTH_BOT;
    private static VoxelShape SHAPE_WEST_BOT;

    public ObeliskBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(NETHER_STAR, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        boolean hasNetherStar = pState.getValue(NETHER_STAR);
        DoubleBlockHalf half = pState.getValue(HALF);
        BlockPos otherBlockPos = half.equals(DoubleBlockHalf.LOWER) ? pPos.above() : pPos.below();
        BlockState otherBlockState = pLevel.getBlockState(otherBlockPos);
        if(stack.is(Items.NETHER_STAR) && !hasNetherStar) {
            pLevel.setBlock(pPos, pState.setValue(NETHER_STAR, true), 3 | 32);
            pLevel.setBlock(otherBlockPos, otherBlockState.setValue(NETHER_STAR, true), 3 | 32);
            if(!pPlayer.isCreative()) stack.shrink(1);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else if (hasNetherStar) {
            pLevel.setBlock(pPos, pState.setValue(NETHER_STAR, false), 3 | 32);
            pLevel.setBlock(otherBlockPos, otherBlockState.setValue(NETHER_STAR, false), 3 | 32);
            pPlayer.addItem(new ItemStack(Items.NETHER_STAR, 1));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            if(pPlayer.isCreative()) {
                DoublePlantBlock.preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
            }
            if(pState.getValue(HALF).equals(DoubleBlockHalf.LOWER) && pState.getValue(NETHER_STAR)) {
                Vec3 pos = pPos.relative(pState.getValue(FACING).getOpposite()).getCenter();
                ItemEntity item = new ItemEntity(pLevel, pos.x, pos.y, pos.z, new ItemStack(Items.NETHER_STAR, 1));
                pLevel.addFreshEntity(item);
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(NETHER_STAR, false).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pState.getValue(HALF).equals(DoubleBlockHalf.UPPER) ? pLevel.getBlockState(pPos.below()).equals(pState.setValue(HALF, DoubleBlockHalf.LOWER)) : super.canSurvive(pState, pLevel, pPos);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        return !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
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
        DoubleBlockHalf half = state.getValue(HALF);
        switch (direction) {
            case NORTH -> {
                if(half.equals(DoubleBlockHalf.UPPER)) {
                    if(SHAPE_NORTH_TOP == null) {
                        SHAPE_NORTH_TOP = Stream.of(
                                Block.box(0.5, 0, 4, 15.5, 16, 13),
                                Block.box(0.5, 5, 3, 15.5, 16, 4),
                                Block.box(8.5, 4, 3, 15.5, 5, 4),
                                Block.box(9.5, 3, 3, 15.5, 4, 4),
                                Block.box(10.5, 1, 3, 15.5, 3, 4),
                                Block.box(12.5, 0, 3, 15.5, 1, 4),
                                Block.box(0.5, 1, 3, 5.5, 3, 4),
                                Block.box(0.5, 0, 3, 3.5, 1, 4),
                                Block.box(0.5, 3, 3, 6.5, 4, 4),
                                Block.box(0.5, 4, 3, 7.5, 5, 4)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_NORTH_TOP;
                } else {
                    if(SHAPE_NORTH_BOT == null) {
                        SHAPE_NORTH_BOT = Stream.of(
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
                    return SHAPE_NORTH_BOT;
                }
            }
            case EAST -> {
                if(half.equals(DoubleBlockHalf.UPPER)) {
                    if(SHAPE_EAST_TOP == null) {
                        SHAPE_EAST_TOP = Stream.of(
                                Block.box(3, 0, 0.5, 12, 16, 15.5),
                                Block.box(12, 5, 0.5, 13, 16, 15.5),
                                Block.box(12, 4, 8.5, 13, 5, 15.5),
                                Block.box(12, 3, 9.5, 13, 4, 15.5),
                                Block.box(12, 1, 10.5, 13, 3, 15.5),
                                Block.box(12, 0, 12.5, 13, 1, 15.5),
                                Block.box(12, 1, 0.5, 13, 3, 5.5),
                                Block.box(12, 0, 0.5, 13, 1, 3.5),
                                Block.box(12, 3, 0.5, 13, 4, 6.5),
                                Block.box(12, 4, 0.5, 13, 5, 7.5)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_EAST_TOP;
                } else {
                    if(SHAPE_EAST_BOT == null) {
                        SHAPE_EAST_BOT = Stream.of(
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
                    return SHAPE_EAST_BOT;
                }
            }
            case SOUTH -> {
                if(half.equals(DoubleBlockHalf.UPPER)) {
                    if(SHAPE_SOUTH_TOP == null) {
                        SHAPE_SOUTH_TOP = Stream.of(
                                Block.box(0.5, 0, 3, 15.5, 16, 12),
                                Block.box(0.5, 5, 12, 15.5, 16, 13),
                                Block.box(0.5, 4, 12, 7.5, 5, 13),
                                Block.box(0.5, 3, 12, 6.5, 4, 13),
                                Block.box(0.5, 1, 12, 5.5, 3, 13),
                                Block.box(0.5, 0, 12, 3.5, 1, 13),
                                Block.box(10.5, 1, 12, 15.5, 3, 13),
                                Block.box(12.5, 0, 12, 15.5, 1, 13),
                                Block.box(9.5, 3, 12, 15.5, 4, 13),
                                Block.box(8.5, 4, 12, 15.5, 5, 13)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_SOUTH_TOP;
                } else {
                    if(SHAPE_SOUTH_BOT == null) {
                        SHAPE_SOUTH_BOT = Stream.of(
                                Block.box(0, 0, 0, 16, 4, 16),
                                Block.box(2.5, 4, 4, 13.5, 7, 12),
                                Block.box(0.5, 7, 3, 15.5, 16, 12),
                                Block.box(0.5, 15, 12, 2.5, 16, 13),
                                Block.box(0.5, 14, 12, 1.5, 15, 13),
                                Block.box(13.5, 15, 12, 15.5, 16, 13),
                                Block.box(13.5, 13, 12, 15.5, 14, 13),
                                Block.box(10.5, 10, 12, 15.5, 12, 13),
                                Block.box(12.5, 12, 12, 15.5, 13, 13),
                                Block.box(9.5, 9, 12, 15.5, 10, 13),
                                Block.box(8.5, 8, 12, 15.5, 9, 13),
                                Block.box(0.5, 8, 12, 7.5, 9, 13),
                                Block.box(0.5, 9, 12, 6.5, 10, 13),
                                Block.box(0.5, 10, 12, 5.5, 12, 13),
                                Block.box(0.5, 12, 12, 3.5, 13, 13),
                                Block.box(0.5, 13, 12, 2.5, 14, 13),
                                Block.box(14.5, 14, 12, 15.5, 15, 13),
                                Block.box(0.5, 7, 12, 15.5, 8, 13)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_SOUTH_BOT;
                }
            }
            case WEST -> {
                if(half.equals(DoubleBlockHalf.UPPER)) {
                    if(SHAPE_WEST_TOP == null) {
                        SHAPE_WEST_TOP = Stream.of(
                                Block.box(4, 0, 0.5, 13, 16, 15.5),
                                Block.box(3, 5, 0.5, 4, 16, 15.5),
                                Block.box(3, 4, 0.5, 4, 5, 7.5),
                                Block.box(3, 3, 0.5, 4, 4, 6.5),
                                Block.box(3, 1, 0.5, 4, 3, 5.5),
                                Block.box(3, 0, 0.5, 4, 1, 3.5),
                                Block.box(3, 1, 10.5, 4, 3, 15.5),
                                Block.box(3, 0, 12.5, 4, 1, 15.5),
                                Block.box(3, 3, 9.5, 4, 4, 15.5),
                                Block.box(3, 4, 8.5, 4, 5, 15.5)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_WEST_TOP;
                } else {
                    if(SHAPE_WEST_BOT == null) {
                        SHAPE_WEST_BOT = Stream.of(
                                Block.box(0, 0, 0, 16, 4, 16),
                                Block.box(4, 4, 2.5, 12, 7, 13.5),
                                Block.box(4, 7, 0.5, 13, 16, 15.5),
                                Block.box(3, 15, 0.5, 4, 16, 2.5),
                                Block.box(3, 14, 0.5, 4, 15, 1.5),
                                Block.box(3, 15, 13.5, 4, 16, 15.5),
                                Block.box(3, 13, 13.5, 4, 14, 15.5),
                                Block.box(3, 10, 10.5, 4, 12, 15.5),
                                Block.box(3, 12, 12.5, 4, 13, 15.5),
                                Block.box(3, 9, 9.5, 4, 10, 15.5),
                                Block.box(3, 8, 8.5, 4, 9, 15.5),
                                Block.box(3, 8, 0.5, 4, 9, 7.5),
                                Block.box(3, 9, 0.5, 4, 10, 6.5),
                                Block.box(3, 10, 0.5, 4, 12, 5.5),
                                Block.box(3, 12, 0.5, 4, 13, 3.5),
                                Block.box(3, 13, 0.5, 4, 14, 2.5),
                                Block.box(3, 14, 14.5, 4, 15, 15.5),
                                Block.box(3, 7, 0.5, 4, 8, 15.5)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
                    }
                    return SHAPE_WEST_BOT;
                }
            }
        }
        return Shapes.block();
    }
}
