package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.block_entity.SoulAnvilBlockEntity;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.menu.SoulAnvilMenu;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SoulAnvilBlock extends Block implements EntityBlock {

    private static VoxelShape shape;
    
    public SoulAnvilBlock(Properties properties) {
        super(properties.noOcclusion());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : new BlockEntityTicker<T>() {
            @Override
            public void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
                SoulAnvilBlockEntity.serverTick(level, pos, state, (SoulAnvilBlockEntity) blockEntity);
            }
        };
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(shape == null) {
            shape = Shapes.empty();
            shape = Shapes.join(shape, Shapes.box(0, 0, 0.0625, 1, 0.1875, 0.9375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.25, 0.8125, 0.625, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0.5, 0.25, 1, 0.875, 0.75), BooleanOp.OR);
        }
        return shape;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            player.openMenu(
                    getMenuProvider(state, level, pos),
                    byteBuf -> byteBuf.writeBlockPos(pos)
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new SoulAnvilMenu(containerId, playerInventory, level.getBlockEntity(pos, BlockEntityRegistry.SOUL_ANVIL.get()).orElseThrow()),
                getName()
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulAnvilBlockEntity(pos, state);
    }
}
