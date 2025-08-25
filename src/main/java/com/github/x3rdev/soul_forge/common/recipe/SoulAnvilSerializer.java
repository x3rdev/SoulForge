package com.github.x3rdev.soul_forge.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.List;

public class SoulAnvilSerializer implements RecipeSerializer<SoulAnvilRecipe> {

    public static final MapCodec<SoulAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ShapedRecipePattern.MAP_CODEC.fieldOf("grid_input").forGetter(SoulAnvilRecipe::gridInput),
                    new FilledIngredientListCodec(4).fieldOf("outer_inputs").orElse(NonNullList.withSize(4, Ingredient.EMPTY)).forGetter(SoulAnvilRecipe::outerInputs),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SoulAnvilRecipe::result)
            ).apply(instance, SoulAnvilRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SoulAnvilRecipe> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, SoulAnvilRecipe value) {
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, value.gridInput());
            new FilledIngredientListStreamCodec(4).encode(buffer, value.outerInputs());
            ItemStack.STREAM_CODEC.encode(buffer, value.result());
        }

        @Override
        public SoulAnvilRecipe decode(RegistryFriendlyByteBuf buffer) {
            ShapedRecipePattern gridInput = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            List<Ingredient> outerInputs = new FilledIngredientListStreamCodec(4).decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new SoulAnvilRecipe(
                    gridInput,
                    outerInputs,
                    result
            );
        }
    };

        @Override
    public MapCodec<SoulAnvilRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SoulAnvilRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
