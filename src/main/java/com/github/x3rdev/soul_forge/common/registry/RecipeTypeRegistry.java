package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeTypeRegistry {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<RitualRecipe>> RITUAL = RECIPE_TYPES.register("ritual",
            RecipeType::simple);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SoulAnvilRecipe>> SOUL_ANVIL = RECIPE_TYPES.register("soul_anvil",
            RecipeType::simple);
}
