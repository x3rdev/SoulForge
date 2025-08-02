package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class SoulForgeRecipeProvider extends RecipeProvider {

    public SoulForgeRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        ShapelessRecipeBuilder.shapeless(
                RecipeCategory.MISC,
                new ItemStack(ItemRegistry.NECRONOMICON)
        )
                .requires(Items.BOOK)
                .requires(ItemRegistry.ECTOPLASM.get())
                .unlockedBy("has_book", has(Items.BOOK))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                new ItemStack(BlockItemRegistry.RESEARCH_TABLE)
        )
                .pattern("bcd")
                .pattern("aaa")
                .pattern("a a")
                .define('a', BlockItemRegistry.SOULWOOD_PLANKS.get())
                .define('b', Items.INK_SAC)
                .define('c', Items.PAPER)
                .define('d', Items.FEATHER)
                .unlockedBy("has_soulwood", has(BlockItemRegistry.SOULWOOD_LOG.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(
                        RecipeCategory.COMBAT,
                        new ItemStack(ItemRegistry.WOODEN_SCYTHE)
                )
                .pattern("aa ")
                .pattern("b a")
                .pattern("b  ")
                .define('a', BlockItemRegistry.SOULWOOD_PLANKS.get())
                .define('b', ItemRegistry.SOULWOOD_STICK.get())
                .unlockedBy("has_soulwood_planks", has(BlockItemRegistry.SOULWOOD_PLANKS.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(
                        RecipeCategory.COMBAT,
                        new ItemStack(ItemRegistry.STONE_SCYTHE)
                )
                .pattern("aa ")
                .pattern("b a")
                .pattern("b  ")
                .define('a', Items.STONE)
                .define('b', ItemRegistry.SOULWOOD_STICK.get())
                .unlockedBy("has_stone", has(Items.STONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(
                        RecipeCategory.COMBAT,
                        new ItemStack(ItemRegistry.IRON_SCYTHE)
                )
                .pattern("aa ")
                .pattern("b a")
                .pattern("b  ")
                .define('a', Items.IRON_INGOT)
                .define('b', ItemRegistry.SOULWOOD_STICK.get())
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(
                        RecipeCategory.COMBAT,
                        new ItemStack(ItemRegistry.GOLDEN_SCYTHE)
                )
                .pattern("aa ")
                .pattern("b a")
                .pattern("b  ")
                .define('a', Items.GOLD_INGOT)
                .define('b', ItemRegistry.SOULWOOD_STICK.get())
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(
                        RecipeCategory.COMBAT,
                        new ItemStack(ItemRegistry.DIAMOND_SCYTHE)
                )
                .pattern("aa ")
                .pattern("b a")
                .pattern("b  ")
                .define('a', Items.DIAMOND)
                .define('b', ItemRegistry.SOULWOOD_STICK.get())
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(recipeOutput);
        netheriteSmithing(recipeOutput, ItemRegistry.DIAMOND_SCYTHE.get(), RecipeCategory.COMBAT, ItemRegistry.NETHERITE_SCYTHE.get());
    }
}
