package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.recipe.RitualSerializer;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SoulForge.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RitualRecipe>> RITUAL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("ritual",
            RitualSerializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SoulAnvilRecipe>> SOUL_ANVIL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("soul_anvil",
            SoulAnvilSerializer::new);
}
