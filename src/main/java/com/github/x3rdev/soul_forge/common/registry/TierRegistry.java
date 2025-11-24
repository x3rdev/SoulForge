package com.github.x3rdev.soul_forge.common.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.function.Supplier;

public class TierRegistry {

    public static final Tier SOUL_STEEL = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2510,
            9.0F,
            4.0F,
            16,
            () -> Ingredient.of(ItemRegistry.SOUL_STEEL_INGOT.get())
    );

    public static final Tier AWAKENED_SOUL_STEEL = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2670,
            9.0F,
            4.5F,
            16,
            () -> Ingredient.of(ItemRegistry.SOUL_STEEL_INGOT.get())
    );
}
