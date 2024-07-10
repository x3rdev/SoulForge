package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.SoulBottleItem;
import com.github.x3rdev.soul_forge.common.item.SoulScytheItem;
import com.github.x3rdev.soul_forge.common.item.SoulSteelArmorItem;
import com.github.x3rdev.soul_forge.common.item.WispAmuletItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulForge.MOD_ID);

    public static final RegistryObject<Item> SOUL_SCYTHE = ITEMS.register("soul_scythe",
            SoulScytheItem::new);
    public static final RegistryObject<Item> SMALL_SOUL_BOTTLE = ITEMS.register("small_soul_bottle",
            () -> new SoulBottleItem(10));
    public static final RegistryObject<Item> LARGE_SOUL_BOTTLE = ITEMS.register("large_soul_bottle",
            () -> new SoulBottleItem(20));
    public static final RegistryObject<Item> SOUL_STEEL_HELMET = ITEMS.register("soul_steel_helmet",
            () -> new SoulSteelArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> SOUL_STEEL_CHESTPLATE = ITEMS.register("soul_steel_chestplate",
            () -> new SoulSteelArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> SOUL_STEEL_LEGGINGS = ITEMS.register("soul_steel_leggings",
            () -> new SoulSteelArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> SOUL_STEEL_BOOTS = ITEMS.register("soul_steel_boots",
            () -> new SoulSteelArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> WISP_AMULET = ITEMS.register("wisp_amulet",
            WispAmuletItem::new);
    public static class ModItemTab {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SoulForge.MOD_ID);

        public static final RegistryObject<CreativeModeTab> SOLARIS_ITEM_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .icon(Items.NAME_TAG::getDefaultInstance)
                .title(Component.translatable("itemGroup." + SoulForge.MOD_ID))
                .displayItems((displayParameters, output) -> {
                    ItemRegistry.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                    BlockItemRegistry.BLOCK_ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                })
                .build());
    }
}

