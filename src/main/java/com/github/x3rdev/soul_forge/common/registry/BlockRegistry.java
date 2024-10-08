package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block.*;
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
    public static final RegistryObject<Block> SOUL_STEEL_BARS = BLOCKS.register("soul_steel_bars",
            () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
    public static final RegistryObject<Block> SOUL_CANDLESTICK = BLOCKS.register("soul_candlestick",
            () -> new SoulCandleBlock(BlockBehaviour.Properties.copy(Blocks.LANTERN)));
    public static final RegistryObject<Block> SOUL_CANDELABRA = BLOCKS.register("soul_candelabra",
            () -> new SoulCandelabraBlock(BlockBehaviour.Properties.copy(Blocks.LANTERN)));
    public static final RegistryObject<Block> OBELISK = BLOCKS.register("obelisk",
            () -> new ObeliskBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)));
    public static final RegistryObject<Block> DARK_TOMB = BLOCKS.register("dark_tomb",
            () -> new DarkTombBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)));
    public static final RegistryObject<Block> STATUE = BLOCKS.register("statue",
            () -> new StatueBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SOUL_CHANDELIER = BLOCKS.register("soul_chandelier",
            () -> new SoulChandelierBlock(BlockBehaviour.Properties.copy(Blocks.LANTERN)));
}
