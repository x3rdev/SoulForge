package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PedestalBlock extends Block implements EntityBlock {

    private static VoxelShape SHAPE;

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        PedestalBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityRegistry.PEDESTAL.get()).orElseThrow();
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        PedestalBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityRegistry.PEDESTAL.get()).orElseThrow();
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(SHAPE == null) {
            SHAPE = Shapes.empty();
            SHAPE = Shapes.join(SHAPE, Shapes.box(0.125, 0, 0.125, 0.875, 0.125, 0.875), BooleanOp.OR);
            SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.125, 0.25, 0.75, 0.25, 0.75), BooleanOp.OR);
            SHAPE = Shapes.join(SHAPE, Shapes.box(0.3125, 0.25, 0.3125, 0.6875, 0.75, 0.6875), BooleanOp.OR);
            SHAPE = Shapes.join(SHAPE, Shapes.box(0.25, 0.75, 0.25, 0.75, 0.8125, 0.75), BooleanOp.OR);
            SHAPE = Shapes.join(SHAPE, Shapes.box(0.125, 0.8125, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
        }
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }
}
