package com.github.x3rdev.soul_forge.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RecipeUtil {

    public static boolean itemStacksMatchIngredients(List<ItemStack> itemStacks, List<Ingredient> ingredients) {
        List<ItemStack> copy = new ArrayList<>(itemStacks);
        for (Ingredient ingredient : ingredients) {
            Optional<ItemStack> optionalItemStack = copy.stream().filter(ingredient).findAny();
            if(optionalItemStack.isPresent()) {
                copy.remove(optionalItemStack.get());
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean itemStacksStrictlyMatchIngredients(List<ItemStack> itemStacks, List<Ingredient> ingredients) {
        if(itemStacks.size() != ingredients.size()) {
            return false;
        }
        for (int i = 0; i < itemStacks.size(); i++) {
            if(!ingredients.get(i).test(itemStacks.get(i))) {
                return false;
            }
        }
        return true;
    }

}
