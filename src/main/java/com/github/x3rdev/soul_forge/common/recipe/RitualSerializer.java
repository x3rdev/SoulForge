package com.github.x3rdev.soul_forge.common.recipe;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RitualSerializer implements RecipeSerializer<RitualRecipe> {

    public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("center_input").orElse(Ingredient.EMPTY).forGetter(RitualRecipe::centerInput),
                    new FilledIngredientListCodec(4).fieldOf("cardinal_inputs").orElse(List.of()).forGetter(RitualRecipe::cardinalInputs),
                    new FilledIngredientListCodec(4).fieldOf("diagonal_inputs").orElse(List.of()).forGetter(RitualRecipe::diagonalInputs),
                    Codec.unboundedMap(SoulType.CODEC, Codec.INT).fieldOf("soul_inputs").orElse(Map.of()).forGetter(RitualRecipe::inputSouls),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RitualRecipe::result)
            ).apply(instance, RitualRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, RitualRecipe value) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value.centerInput());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)).encode(buffer, value.cardinalInputs());
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)).encode(buffer, value.diagonalInputs());
            ByteBufCodecs.map(
                    HashMap::new,
                    SoulType.STREAM_CODEC,
                    ByteBufCodecs.INT,
                    SoulType.values().length
            ).encode(buffer, new HashMap<>(value.inputSouls()));
            ItemStack.STREAM_CODEC.encode(buffer, value.result());
        }

        @Override
        public RitualRecipe decode(RegistryFriendlyByteBuf buffer) {
            Ingredient centerInput = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            List<Ingredient> cardinalInputs = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)).decode(buffer);
            List<Ingredient> diagonalInputs = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list(4)).decode(buffer);
            HashMap<SoulType, Integer> soulInputs = ByteBufCodecs.map(
                    HashMap::new,
                    SoulType.STREAM_CODEC,
                    ByteBufCodecs.INT,
                    SoulType.values().length
            ).decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new RitualRecipe(
                    centerInput,
                    cardinalInputs,
                    diagonalInputs,
                    soulInputs,
                    result
            );
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
