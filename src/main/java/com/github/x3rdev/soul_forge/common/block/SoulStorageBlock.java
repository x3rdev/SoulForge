package com.github.x3rdev.soul_forge.common.block;

import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SoulStorageBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(3, 2, 3, 13, 8, 13),
            Block.box(2, 8, 2, 14, 10, 14),
            Block.box(5, 10, 7, 7, 12, 9),
            Block.box(9, 10, 7, 11, 12, 9),
            Block.box(5, 12, 7, 11, 14, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElseThrow();;

    public SoulStorageBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulStorageBlockEntity(pos, state);
    }
}
