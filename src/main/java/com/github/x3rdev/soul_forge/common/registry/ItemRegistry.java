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

    public static final DeferredHolder<Item, SoulScythe> SOUL_SCYTHE = ITEMS.register("soul_scythe",
            SoulScythe::new);
    public static final DeferredHolder<Item, SoulBottle> SMALL_SOUL_BOTTLE = ITEMS.register("small_soul_bottle",
            () -> new SoulBottle(10));
    public static final DeferredHolder<Item, SoulBottle> LARGE_SOUL_BOTTLE = ITEMS.register("large_soul_bottle",
            () -> new SoulBottle(20));
    public static final DeferredHolder<Item, SoulSteelArmor> SOUL_STEEL_HELMET = ITEMS.register("soul_steel_helmet",
            () -> new SoulSteelArmor(ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredHolder<Item, SoulSteelArmor> SOUL_STEEL_CHESTPLATE = ITEMS.register("soul_steel_chestplate",
            () -> new SoulSteelArmor(ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredHolder<Item, SoulSteelArmor> SOUL_STEEL_LEGGINGS = ITEMS.register("soul_steel_leggings",
            () -> new SoulSteelArmor(ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredHolder<Item, SoulSteelArmor> SOUL_STEEL_BOOTS = ITEMS.register("soul_steel_boots",
            () -> new SoulSteelArmor(ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));
    public static final DeferredHolder<Item, WispAmulet> WISP_AMULET = ITEMS.register("wisp_amulet",
            WispAmulet::new);

    public static final DeferredHolder<Item, DeferredSpawnEggItem> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg",
            () -> new DeferredSpawnEggItem(EntityRegistry.GHOST, 0x5063c0, 0x77a2fb, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> NERGAL_SPAWN_EGG = ITEMS.register("nergal_spawn_egg",
            () -> new DeferredSpawnEggItem(EntityRegistry.NERGAL, 0x6388d6, 0xd4edff, new Item.Properties()));
    public static final DeferredHolder<Item, Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMPOWERED_SOUL_CRYSTAL = ITEMS.register("empowered_soul_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ECTOPLASM = ITEMS.register("ectoplasm",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MURKY_LENS = ITEMS.register("murky_lens",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, ResearcherGlasses> RESEARCHER_GLASSES = ITEMS.register("researcher_glasses",
            () -> new ResearcherGlasses(ArmorItem.Type.HELMET));
    public static final DeferredHolder<Item, Necronomicon> NECRONOMICON = ITEMS.register("necronomicon",
            Necronomicon::new);
    public static final DeferredHolder<Item, ScrollOfShadows> SCROLL_OF_SHADOWS = ITEMS.register("scroll_of_shadows",
            ScrollOfShadows::new);
    public static final DeferredHolder<Item, Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WOODEN_SCYTHE = ITEMS.register("wooden_scythe",
            () -> new Scythe(Tiers.WOOD, new Item.Properties()));
    public static final DeferredHolder<Item, Item> STONE_SCYTHE = ITEMS.register("stone_scythe",
            () -> new Scythe(Tiers.STONE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> IRON_SCYTHE = ITEMS.register("iron_scythe",
            () -> new Scythe(Tiers.IRON, new Item.Properties()));
    public static final DeferredHolder<Item, Item> GOLDEN_SCYTHE = ITEMS.register("golden_scythe",
            () -> new Scythe(Tiers.GOLD, new Item.Properties()));
    public static final DeferredHolder<Item, Item> DIAMOND_SCYTHE = ITEMS.register("diamond_scythe",
            () -> new Scythe(Tiers.DIAMOND, new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHERITE_SCYTHE = ITEMS.register("netherite_scythe",
            () -> new Scythe(Tiers.NETHERITE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> SOULWOOD_STICK = ITEMS.register("soulwood_stick",
            () -> new Item(new Item.Properties()));




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

