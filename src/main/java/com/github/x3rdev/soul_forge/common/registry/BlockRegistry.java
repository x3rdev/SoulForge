package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SoulForge.MOD_ID);

    public static final RegistryObject<Block> SOUL_BRICKS = BLOCKS.register("soul_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_SOUL_BRICKS = BLOCKS.register("cracked_soul_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.CRACKED_STONE_BRICKS)));
    public static final RegistryObject<Block> SOUL_BRICK_STAIRS = BLOCKS.register("soul_brick_stairs",
            () -> new StairBlock(() -> SOUL_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)));
    public static final RegistryObject<Block> SOUL_BRICK_SLAB = BLOCKS.register("soul_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)));
    public static final RegistryObject<Block> SOUL_BRICK_WALL = BLOCKS.register("soul_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));
    public static final RegistryObject<Block> CHISELED_SOUL_BRICKS = BLOCKS.register("chiseled_soul_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.CHISELED_STONE_BRICKS)));
    public static final RegistryObject<Block> SOUL_BRICK_COLUMN = BLOCKS.register("soul_brick_column",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> SOUL_BRICK_DOOR = BLOCKS.register("soul_brick_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR), BlockSetType.POLISHED_BLACKSTONE));
}
