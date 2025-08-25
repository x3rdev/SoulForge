package com.github.x3rdev.soul_forge.common.recipe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record FilledIngredientListStreamCodec(int size) implements StreamCodec<RegistryFriendlyByteBuf, List<Ingredient>> {

    @Override
    public List<Ingredient> decode(RegistryFriendlyByteBuf buffer) {
        return Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buffer, List<Ingredient> value) {
        List<Ingredient> copy = new ArrayList<>(value);
        for (int i = 0; i < size - value.size(); i++) {
            copy.add(Ingredient.EMPTY);
        }
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, copy);
    }
}
