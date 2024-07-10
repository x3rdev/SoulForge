package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockItemRegistry {
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulForge.MOD_ID);

    public static final RegistryObject<Item> SOUL_BRICKS = BLOCK_ITEMS.register("soul_bricks",
            () -> new BlockItem(BlockRegistry.SOUL_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRACKED_SOUL_BRICKS = BLOCK_ITEMS.register("cracked_soul_bricks",
            () -> new BlockItem(BlockRegistry.CRACKED_SOUL_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOUL_BRICK_STAIRS = BLOCK_ITEMS.register("soul_brick_stairs",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOUL_BRICK_SLAB = BLOCK_ITEMS.register("soul_brick_slab",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> SOUL_BRICK_WALL = BLOCK_ITEMS.register("soul_brick_wall",
            () -> new BlockItem(BlockRegistry.SOUL_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHISELED_SOUL_BRICKS = BLOCK_ITEMS.register("chiseled_soul_bricks",
            () -> new BlockItem(BlockRegistry.CHISELED_SOUL_BRICKS.get(), new Item.Properties()));
}
