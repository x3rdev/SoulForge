package com.github.x3rdev.soul_forge.common.recipe;

import com.github.x3rdev.soul_forge.common.registry.RecipeSerializerRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record SoulAnvilRecipe(List<Ingredient> gridInputs, List<Ingredient> outerInputs, ItemStack result) implements Recipe<SoulAnvilInput> {


    @Override
    public boolean matches(SoulAnvilInput input, Level level) {
        return
                RecipeUtil.itemStacksStrictlyMatchIngredients(input.gridInputs(), gridInputs) &&
                RecipeUtil.itemStacksMatchIngredients(input.outerInputs(), outerInputs);
    }

    @Override
    public ItemStack assemble(SoulAnvilInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.RITUAL_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.SOUL_ANVIL.get();
    }
}