package com.github.x3rdev.soul_forge.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record SoulAnvilInput(
        int width,
        int height,
        List<ItemStack> gridInputs,
        List<ItemStack> outerInputs) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        switch (index) {
            case 0 -> {
                return gridInputs.get(0);
            }
            case 1 -> {
                return gridInputs.get(1);
            }
            case 2 -> {
                return gridInputs.get(2);
            }
            case 3 -> {
                return gridInputs.get(3);
            }
            case 4 -> {
                return gridInputs.get(4);
            }
            case 5 -> {
                return gridInputs.get(5);
            }
            case 6 -> {
                return gridInputs.get(6);
            }
            case 7 -> {
                return gridInputs.get(7);
            }
            case 8 -> {
                return gridInputs.get(8);
            }
            case 9 -> {
                return outerInputs.get(0);
            }
            case 10 -> {
                return outerInputs.get(1);
            }
            case 11 -> {
                return outerInputs.get(2);
            }
            case 12 -> {
                return outerInputs.get(3);
            }
            default -> throw new IllegalArgumentException("No item for index: " + index);
        }
    }

    @Override
    public int size() {
        return 9+4;
    }
}
