package com.github.x3rdev.soul_forge.common.recipe;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Map;

public record RitualInput(
        ItemStack primary,
        ItemStack catalystNorth, ItemStack catalystNorthEast,
        ItemStack catalystEast, ItemStack catalystSouthEast,
        ItemStack catalystSouth, ItemStack catalystSouthWest,
        ItemStack catalystWest, ItemStack catalystNorthWest,
        Map<SoulType, Integer> souls)
        implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        switch (index) {
            case 0 -> {
                return primary;
            }
            case 1 -> {
                return catalystNorth;
            }
            case 2 -> {
                return catalystNorthEast;
            }
            case 3 -> {
                return catalystEast;
            }
            case 4 -> {
                return catalystSouthEast;
            }
            case 5 -> {
                return catalystSouth;
            }
            case 6 -> {
                return catalystSouthWest;
            }
            case 7 -> {
                return catalystWest;
            }
            case 8 -> {
                return catalystNorthWest;
            }
        }
        throw new IllegalArgumentException("No item for index: " + index);
    }

    @Override
    public int size() {
        return 9;
    }
}
