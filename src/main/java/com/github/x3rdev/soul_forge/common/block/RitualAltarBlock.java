package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.block_entity.RitualAltarBlockEntity;
import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class RitualAltarBlock extends Block implements EntityBlock {

    private static VoxelShape shape;

    public RitualAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        RitualAltarBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityRegistry.RITUAL_ALTAR.get()).orElseThrow();
        if(!blockEntity.getTheItem().isEmpty()) {
            this.takeItemFromPedestal(blockEntity, level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        RitualAltarBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityRegistry.RITUAL_ALTAR.get()).orElseThrow();
        if(stack.getItem() instanceof Necronomicon) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if(!blockEntity.getTheItem().isEmpty()) {
            this.takeItemFromPedestal(blockEntity, level, pos, player);
        } else {
            blockEntity.setTheItem(stack.copyWithCount(1));
            if (!player.hasInfiniteMaterials()) {
                stack.consume(1, player);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private void takeItemFromPedestal(RitualAltarBlockEntity blockEntity, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide()) {
            ItemStack itemstack = blockEntity.getTheItem();
            if (!itemstack.isEmpty()) {
                Vec3 vec3 = Vec3.atCenterOf(pos).add(0, 1F, 0);
                ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), itemstack.copy());
                itementity.setDeltaMovement(vec3.vectorTo(player.position().add(0,1,0)).normalize().scale(0.4F));
                itementity.setNoPickUpDelay();
                level.addFreshEntity(itementity);
            }
        }
        blockEntity.removeTheItem();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(shape == null) {
            makeShape();
        }
        return shape;
    }

    private static void makeShape() {
        shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.875, 1), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.5625, 0.9375), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.0, 0.5625, 0.0, 1.0, 0.875, 1.0), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.25, 0.9375), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), BooleanOp.OR);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            PedestalBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityRegistry.PEDESTAL.get()).orElseThrow();
            if (!level.isClientSide()) {
                ItemStack itemstack = blockEntity.getTheItem();
                if (!itemstack.isEmpty()) {
                    Vec3 vec3 = pos.getCenter();
                    ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), itemstack.copy());
                    itementity.setDefaultPickUpDelay();
                    level.addFreshEntity(itementity);
                }
            }
            blockEntity.setRemoved();
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RitualAltarBlockEntity(pos, state);
    }
}
