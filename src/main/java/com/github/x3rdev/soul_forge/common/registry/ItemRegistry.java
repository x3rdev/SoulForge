package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SoulForge.MOD_ID);

    public static final DeferredHolder<Item, SoulScytheItem> SOUL_SCYTHE = ITEMS.register("soul_scythe",
            SoulScytheItem::new);
    public static final DeferredHolder<Item, SoulBottleItem> SMALL_SOUL_BOTTLE = ITEMS.register("small_soul_bottle",
            () -> new SoulBottleItem(10));
    public static final DeferredHolder<Item, SoulBottleItem> LARGE_SOUL_BOTTLE = ITEMS.register("large_soul_bottle",
            () -> new SoulBottleItem(20));
    public static final DeferredHolder<Item, SoulSteelArmorItem> SOUL_STEEL_HELMET = ITEMS.register("soul_steel_helmet",
            () -> new SoulSteelArmorItem(ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, SoulSteelArmorItem> SOUL_STEEL_CHESTPLATE = ITEMS.register("soul_steel_chestplate",
            () -> new SoulSteelArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final DeferredHolder<Item, SoulSteelArmorItem> SOUL_STEEL_LEGGINGS = ITEMS.register("soul_steel_leggings",
            () -> new SoulSteelArmorItem(ArmorItem.Type.LEGGINGS));
    public static final DeferredHolder<Item, SoulSteelArmorItem> SOUL_STEEL_BOOTS = ITEMS.register("soul_steel_boots",
            () -> new SoulSteelArmorItem(ArmorItem.Type.BOOTS));
    public static final DeferredHolder<Item, WispAmuletItem> WISP_AMULET = ITEMS.register("wisp_amulet",
            WispAmuletItem::new);
    public static final DeferredHolder<Item, Item> SOUL_GEM = ITEMS.register("soul_gem",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg",
            () -> new DeferredSpawnEggItem(EntityRegistry.GHOST, 0x5063c0, 0x77a2fb, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> NERGAL_SPAWN_EGG = ITEMS.register("nergal_spawn_egg",
            () -> new DeferredSpawnEggItem(EntityRegistry.NERGAL, 0x6388d6, 0xd4edff, new Item.Properties()));
    public static final DeferredHolder<Item, Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ECTOPLASM = ITEMS.register("ectoplasm",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MURKY_LENS = ITEMS.register("murky_lens",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, ResearcherGlassesItem> RESEARCHER_GLASSES = ITEMS.register("researcher_glasses",
            () -> new ResearcherGlassesItem(ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, CursedSwordItem> CURSED_SWORD = ITEMS.register("cursed_sword",
            CursedSwordItem::new);
    public static final DeferredHolder<Item, NecronomiconItem> NECRONOMICON = ITEMS.register("necronomicon",
            NecronomiconItem::new);
    public static final DeferredHolder<Item, RuneItem> RED_SINGLE_RUNE = ITEMS.register("red_single_rune",
            () -> new RuneItem(new Item.Properties(), RuneItem.RuneColor.RED, new String[]{"x--", "---", "---"}));
    public static final DeferredHolder<Item, RuneItem> RED_SQUARE_RUNE = ITEMS.register("red_square_rune",
            () -> new RuneItem(new Item.Properties(), RuneItem.RuneColor.RED, new String[]{"xx-", "xx-", "---"}));
    public static final DeferredHolder<Item, RuneItem> RED_LARGE_SQUARE_RUNE = ITEMS.register("red_large_square_rune",
            () -> new RuneItem(new Item.Properties(), RuneItem.RuneColor.RED, new String[]{"xxx", "xxx", "xxx"}));


    public static class ModItemTab {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SoulForge.MOD_ID);

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SOUL_FORGE_ITEM_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .icon(Items.NAME_TAG::getDefaultInstance)
                .title(Component.translatable("itemGroup." + SoulForge.MOD_ID))
                .displayItems((displayParameters, output) -> {
                    ItemRegistry.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                    BlockItemRegistry.BLOCK_ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                })
                .build());
    }
}

