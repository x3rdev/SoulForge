package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeTypeRegistry {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SoulForge.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<RitualRecipe>> RITUAL_RECIPE_TYPE = RECIPE_TYPES.register("ritual",
            resourceLocation -> new RecipeType<>() {
                @Override
                public String toString() {
                    return resourceLocation.toString();
                }
            });
}
