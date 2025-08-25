package com.github.x3rdev.soul_forge.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record SoulAnvilInput(
        CraftingInput gridInput,
        List<ItemStack> outerInputs) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        switch (index) {
            case 0 -> {
                if(gridInput.size() > 0) {
                    return gridInput.getItem(0);
                }
                return ItemStack.EMPTY;
            }
            case 1 -> {
                if(gridInput.size() > 1) {
                    return gridInput.getItem(1);
                }
                return ItemStack.EMPTY;
            }
            case 2 -> {
                if(gridInput.size() > 2) {
                    return gridInput.getItem(2);
                }
                return ItemStack.EMPTY;
            }
            case 3 -> {
                if(gridInput.size() > 3) {
                    return gridInput.getItem(3);
                }
                return ItemStack.EMPTY;
            }
            case 4 -> {
                if(gridInput.size() > 4) {
                    return gridInput.getItem(4);
                }
                return ItemStack.EMPTY;
            }
            case 5 -> {
                if(gridInput.size() > 5) {
                    return gridInput.getItem(5);
                }
                return ItemStack.EMPTY;
            }
            case 6 -> {
                if(gridInput.size() > 6) {
                    return gridInput.getItem(6);
                }
                return ItemStack.EMPTY;
            }
            case 7 -> {
                if(gridInput.size() > 7) {
                    return gridInput.getItem(7);
                }
                return ItemStack.EMPTY;
            }
            case 8 -> {
                if(gridInput.size() > 8) {
                    return gridInput.getItem(8);
                }
                return ItemStack.EMPTY;
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
