package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.internal.NeoForgeItemTagsProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SoulForgeItemTagsProvider extends ItemTagsProvider {

    public static final TagKey<Item> SCYTHES = TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("scythes"));

    public SoulForgeItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SoulForge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ItemTags.SWORDS).add(
                ItemRegistry.SOUL_STEEL_SWORD.get(),
                ItemRegistry.AWAKENED_SOUL_STEEL_SWORD.get()
        );
        tag(ItemTags.HEAD_ARMOR).add(
                ItemRegistry.SOUL_STEEL_HELMET.get(),
                ItemRegistry.AWAKENED_SOUL_STEEL_HELMET.get()
        );
        tag(ItemTags.CHEST_ARMOR).add(
                ItemRegistry.SOUL_STEEL_CHESTPLATE.get(),
                ItemRegistry.AWAKENED_SOUL_STEEL_CHESTPLATE.get()
        );
        tag(ItemTags.LEG_ARMOR).add(
                ItemRegistry.SOUL_STEEL_LEGGINGS.get(),
                ItemRegistry.AWAKENED_SOUL_STEEL_LEGGINGS.get()
        );
        tag(ItemTags.FOOT_ARMOR).add(
                ItemRegistry.SOUL_STEEL_BOOTS.get(),
                ItemRegistry.AWAKENED_SOUL_STEEL_BOOTS.get()
        );
        tag(SCYTHES).add(
                ItemRegistry.WOODEN_SCYTHE.get(),
                ItemRegistry.STONE_SCYTHE.get(),
                ItemRegistry.GOLDEN_SCYTHE.get(),
                ItemRegistry.IRON_SCYTHE.get(),
                ItemRegistry.DIAMOND_SCYTHE.get(),
                ItemRegistry.NETHERITE_SCYTHE.get(),
                ItemRegistry.SOUL_SCYTHE.get()
        );
        tag(ItemTags.WEAPON_ENCHANTABLE).addTag(SCYTHES);
        tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(SCYTHES);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).addTag(SCYTHES);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).addTag(SCYTHES);
        tag(Tags.Items.TOOLS).addTag(SCYTHES);
        tag(ItemTags.BREAKS_DECORATED_POTS).addTag(SCYTHES);
        tag(ItemTags.VANISHING_ENCHANTABLE).addTag(SCYTHES);


    }
}
