package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.GeckoBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockItemRegistry {
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(Registries.ITEM, SoulForge.MOD_ID);

    public static final DeferredHolder<Item, BlockItem> SOUL_BRICKS = BLOCK_ITEMS.register("soul_bricks",
            () -> new BlockItem(BlockRegistry.SOUL_BRICKS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> CRACKED_SOUL_BRICKS = BLOCK_ITEMS.register("cracked_soul_bricks",
            () -> new BlockItem(BlockRegistry.CRACKED_SOUL_BRICKS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_BRICK_STAIRS = BLOCK_ITEMS.register("soul_brick_stairs",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_STAIRS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_BRICK_SLAB = BLOCK_ITEMS.register("soul_brick_slab",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_SLAB.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_BRICK_WALL = BLOCK_ITEMS.register("soul_brick_wall",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_WALL.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> CHISELED_SOUL_BRICKS = BLOCK_ITEMS.register("chiseled_soul_bricks",
            () -> new BlockItem(BlockRegistry.CHISELED_SOUL_BRICKS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_BRICK_COLUMN = BLOCK_ITEMS.register("soul_brick_column",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_COLUMN.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_BRICK_DOOR = BLOCK_ITEMS.register("soul_brick_door",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_DOOR.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_STEEL_BARS = BLOCK_ITEMS.register("soul_steel_bars",
            () -> new BlockItem(BlockRegistry.SOUL_STEEL_BARS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_CANDLESTICK = BLOCK_ITEMS.register("soul_candlestick",
            () -> new BlockItem(BlockRegistry.SOUL_CANDLESTICK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_CANDELABRA = BLOCK_ITEMS.register("soul_candelabra",
            () -> new BlockItem(BlockRegistry.SOUL_CANDELABRA.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> DARK_TOMB = BLOCK_ITEMS.register("dark_tomb",
            () -> new BlockItem(BlockRegistry.DARK_TOMB.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> STATUE = BLOCK_ITEMS.register("statue",
            () -> new BlockItem(BlockRegistry.STATUE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_CHANDELIER = BLOCK_ITEMS.register("soul_chandelier",
            () -> new BlockItem(BlockRegistry.SOUL_CHANDELIER.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_CRYSTAL_ORE = BLOCK_ITEMS.register("soul_crystal_ore",
            () -> new BlockItem(BlockRegistry.SOUL_CRYSTAL_ORE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_CRYSTAL_BLOCK = BLOCK_ITEMS.register("soul_crystal_block",
            () -> new BlockItem(BlockRegistry.SOUL_CRYSTAL_BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOULWOOD_LEAVES = BLOCK_ITEMS.register("soulwood_leaves",
            () -> new BlockItem(BlockRegistry.SOULWOOD_LEAVES.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOULWOOD_SAPLING = BLOCK_ITEMS.register("soulwood_sapling",
            () -> new BlockItem(BlockRegistry.SOULWOOD_SAPLING.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOULWOOD_LIANA = BLOCK_ITEMS.register("soulwood_liana",
            () -> new BlockItem(BlockRegistry.SOULWOOD_LIANA.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOULWOOD_LOG = BLOCK_ITEMS.register("soulwood_log",
            () -> new BlockItem(BlockRegistry.SOULWOOD_LOG.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOULWOOD_PLANKS = BLOCK_ITEMS.register("soulwood_planks",
            () -> new BlockItem(BlockRegistry.SOULWOOD_PLANKS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> PEDESTAL = BLOCK_ITEMS.register("pedestal",
            () -> new GeckoBlock(BlockRegistry.PEDESTAL.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_STORAGE = BLOCK_ITEMS.register("soul_storage",
            () -> new GeckoBlock(BlockRegistry.SOUL_STORAGE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> RESEARCH_TABLE = BLOCK_ITEMS.register("research_table",
            () -> new BlockItem(BlockRegistry.RESEARCH_TABLE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SOUL_ANVIL = BLOCK_ITEMS.register("soul_anvil",
            () -> new GeckoBlock(BlockRegistry.SOUL_ANVIL.get(), new Item.Properties()));
}