package com.github.x3rdev.soul_forge.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RitualSerializer implements RecipeSerializer<RitualRecipe> {

    public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("primary").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystNorth),
                    Ingredient.CODEC.fieldOf("catalyst_north").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystNorth),
                    Ingredient.CODEC.fieldOf("catalyst_north_east").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystNorthEast),
                    Ingredient.CODEC.fieldOf("catalyst_east").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystEast),
                    Ingredient.CODEC.fieldOf("catalyst_south_east").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystSouthEast),
                    Ingredient.CODEC.fieldOf("catalyst_south").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystSouth),
                    Ingredient.CODEC.fieldOf("catalyst_south_west").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystSouthWest),
                    Ingredient.CODEC.fieldOf("catalyst_west").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystWest),
                    Ingredient.CODEC.fieldOf("catalyst_north_west").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::getInputCatalystNorthWest),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RitualRecipe::getResult)
            ).apply(instance, RitualRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public RitualRecipe decode(RegistryFriendlyByteBuf buffer) {
            Ingredient inputPrimary = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystNorth = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystNorthEast = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystEast = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystSouthEast = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystSouth = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystSouthWest = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystWest = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient inputCatalystNorthWest = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new RitualRecipe(
                    inputPrimary,
                    inputCatalystNorth,
                    inputCatalystNorthEast,
                    inputCatalystEast,
                    inputCatalystSouthEast,
                    inputCatalystSouth,
                    inputCatalystSouthWest,
                    inputCatalystWest,
                    inputCatalystNorthWest,
                    result
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, RitualRecipe value) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputPrimary());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystNorth());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystNorthEast());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystEast());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystSouthEast());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystSouth());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystSouthWest());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystWest());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.getInputCatalystNorthWest());
            ItemStack.STREAM_CODEC.encode(buffer, value.getResult());
        }
    };

    @Override
    public MapCodec<RitualRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
