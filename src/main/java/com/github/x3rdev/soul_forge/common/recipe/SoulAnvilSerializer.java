package com.github.x3rdev.soul_forge.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class SoulAnvilSerializer implements RecipeSerializer<SoulAnvilRecipe> {

    public static final MapCodec<SoulAnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.list(Ingredient.CODEC, 9, 9).fieldOf("grid_inputs").orElse(List.of()).forGetter(SoulAnvilRecipe::gridInputs),
                    new FilledIngredientListCodec(4).fieldOf("outer_inputs").orElse(List.of()).forGetter(SoulAnvilRecipe::outerInputs),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SoulAnvilRecipe::result)
            ).apply(instance, SoulAnvilRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SoulAnvilRecipe> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, SoulAnvilRecipe value) {
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(9)).encode(buffer, value.gridInputs());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)).encode(buffer, value.outerInputs());
            ItemStack.STREAM_CODEC.encode(buffer, value.result());
        }

        @Override
        public SoulAnvilRecipe decode(RegistryFriendlyByteBuf buffer) {
            List<Ingredient> gridInputs = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(9)).decode(buffer);
            List<Ingredient> outerInputs = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)).decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new SoulAnvilRecipe(
                    gridInputs,
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
