package com.github.x3rdev.soul_forge.common.datagen;

import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;

public class SoulForgeRecipeProvider extends RecipeProvider {

    public SoulForgeRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        ShapedRecipeBuilder.shaped(
                RecipeCategory.MISC,
                new ItemStack(BlockItemRegistry.RESEARCH_TABLE)
        )
                .pattern("aba")
                .pattern("a a")
                .define('a', BlockItemRegistry.SOULWOOD_PLANKS.get())
                .define('b', ItemRegistry.SOUL_CRYSTAL.get())
                .unlockedBy("has_soul_gem", has(ItemRegistry.SOUL_CRYSTAL.get()))
                .save(recipeOutput);
    }
}
