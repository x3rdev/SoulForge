package com.github.x3rdev.soul_forge.common.recipe;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;
import java.util.Map;

public record RitualInput(
        ItemStack centerInput,
        List<ItemStack> cardinalInputs,
        List<ItemStack> diagonalInputs,
        Map<SoulType, Integer> soulInputs)
        implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        switch (index) {
            case 0 -> {
                return centerInput;
            }
            case 1 -> {
                return cardinalInputs.get(0);
            }
            case 2 -> {
                return cardinalInputs.get(1);
            }
            case 3 -> {
                return cardinalInputs.get(2);
            }
            case 4 -> {
                return cardinalInputs.get(3);
            }
            case 5 -> {
                return diagonalInputs.get(0);
            }
            case 6 -> {
                return diagonalInputs.get(1);
            }
            case 7 -> {
                return diagonalInputs.get(2);
            }
            case 8 -> {
                return diagonalInputs.get(3);
            }
            default -> throw new IllegalArgumentException("No item for index: " + index);
        }
    }

    @Override
    public int size() {
        return 9;
    }
}
