package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResearchTableBlock extends Block {

    public static final VoxelShape SHAPE_NORTH_LEFT = Shapes.or(
            Shapes.box(0.75, 0, 0, 1, 1, 1),
            Shapes.box(0, 0.8125, 0, 0.75, 1, 1)
    );

    public static final VoxelShape SHAPE_NORTH_RIGHT = Shapes.or(
            Shapes.box(0, 0, 0, 0.75, 0.8125, 1),
            Shapes.box(0, 0.8125, 0, 1, 1, 1)
    );

    public static final VoxelShape SHAPE_EAST_LEFT = Shapes.or(
            Shapes.box(0, 0, 0.75, 1, 1, 1),
            Shapes.box(0, 0.8125, 0, 1, 1, 0.75)
    );

    public static final VoxelShape SHAPE_EAST_RIGHT = Shapes.or(
            Shapes.box(0, 0, 0, 1, 1, 0.75),
            Shapes.box(0, 0.8125, 0.75, 1, 1, 1)
    );

    public static final VoxelShape SHAPE_SOUTH_LEFT = Shapes.or(
            Shapes.box(0, 0, 0, 0.25, 1, 1),
            Shapes.box(0.25, 0.8125, 0, 1, 1, 1)
    );

    public static final VoxelShape SHAPE_SOUTH_RIGHT = Shapes.or(
            Shapes.box(0.25, 0, 0, 1, 1, 1),
            Shapes.box(0, 0.8125, 0, 0.25, 1, 1)
    );

    public static final VoxelShape SHAPE_WEST_LEFT = Shapes.or(
            Shapes.box(0, 0, 0, 1, 1, 0.25),
            Shapes.box(0, 0.8125, 0.25, 1, 1, 1)
    );

    public static final VoxelShape SHAPE_WEST_RIGHT = Shapes.or(
            Shapes.box(0, 0, 0.25, 1, 1, 1),
            Shapes.box(0, 0.8125, 0, 1, 1, 0.25)
    );

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<TablePart> PART = EnumProperty.create("table_part", TablePart.class);

    public ResearchTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, TablePart.LEFT));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.is(ItemRegistry.NECRONOMICON.get())) {
            if(!level.isClientSide()) {
                player.openMenu(
                        getMenuProvider(level, pos, hand),
                        byteBuf -> {
                            byteBuf.writeBlockPos(pos);
                            byteBuf.writeEnum(hand);
                        }
                );
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        if(level.isClientSide()) {
            player.displayClientMessage(Component.literal("Interact with a Necronomicon to use the research table"), true);
        }
        return ItemInteractionResult.FAIL;
    }

    private MenuProvider getMenuProvider(Level level, BlockPos pos, InteractionHand hand) {
        return new SimpleMenuProvider(
                (containerId, playerInventory, player1) -> new ResearchTableMenu(containerId, playerInventory, ContainerLevelAccess.create(level, pos), hand),
                getName()
        );
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if(state.getValue(PART) == TablePart.RIGHT) {
            return List.of();
        }
        return super.getDrops(state, params);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if(direction.equals(getNeighborDirection(state.getValue(PART), state.getValue(FACING))) && !neighborState.is(this)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private static Direction getNeighborDirection(TablePart part, Direction direction) {
        return part == TablePart.LEFT ? direction.getCounterClockWise() : direction.getClockWise();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        BlockPos posOther = pos.relative(direction.getCounterClockWise());
        Level level = context.getLevel();
        return level.getBlockState(posOther).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(posOther)
                ? this.defaultBlockState().setValue(FACING, direction)
                : null;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        TablePart part = state.getValue(PART);
        return switch (state.getValue(FACING)) {
            case NORTH -> part.equals(TablePart.LEFT) ? SHAPE_NORTH_LEFT : SHAPE_NORTH_RIGHT;
            case EAST -> part.equals(TablePart.LEFT) ? SHAPE_EAST_LEFT : SHAPE_EAST_RIGHT;
            case SOUTH -> part.equals(TablePart.LEFT) ? SHAPE_SOUTH_LEFT : SHAPE_SOUTH_RIGHT;
            case WEST -> part.equals(TablePart.LEFT) ? SHAPE_WEST_LEFT : SHAPE_WEST_RIGHT;
            default -> Shapes.empty();
        };
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockpos = pos.relative(state.getValue(FACING).getCounterClockWise());
            level.setBlock(blockpos, state.setValue(PART, TablePart.RIGHT).setValue(FACING, state.getValue(FACING)), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    public enum TablePart implements StringRepresentable {
        LEFT("left"),
        RIGHT("right");

        private final String name;

        TablePart(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getVisualShape(state, level, pos, context);
    }
}
