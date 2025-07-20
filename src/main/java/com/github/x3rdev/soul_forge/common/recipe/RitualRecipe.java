package com.github.x3rdev.soul_forge.common.recipe;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.RecipeSerializerRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record RitualRecipe(
        Ingredient centerInput,
        List<Ingredient> cardinalInputs,
        List<Ingredient> diagonalInputs,
        Map<SoulType, Integer> inputSouls,
        ItemStack result) implements Recipe<RitualInput> {

    @Override
    public boolean matches(RitualInput input, Level level) {
        return
                centerInput.test(input.centerInput()) &&
                ingredientListMatches(cardinalInputs, input.cardinalInputs()) &&
                ingredientListMatches(diagonalInputs, input.diagonalInputs()) &&
                inputHasSufficientSouls(input);
    }

    private boolean ingredientListMatches(List<Ingredient> ingredients, List<ItemStack> inputs) {
        List<Ingredient> ingredientsCopy = new ArrayList<>(ingredients);
        List<ItemStack> inputsCopy = new ArrayList<>(inputs);
        for (int i = 0; i < 4-ingredients.size(); i++) {
            ingredientsCopy.add(Ingredient.EMPTY);
        }
        for (Ingredient ingredient : ingredientsCopy) {
            for (ItemStack stack : inputsCopy) {
                if (ingredient.test(stack)) {
                    inputsCopy.remove(stack);
                    break;
                }
            }
        }
        return inputsCopy.isEmpty();
    }

    private boolean inputHasSufficientSouls(RitualInput input) {
        for (Map.Entry<SoulType, Integer> entry : inputSouls.entrySet()) {
            int availableSoulCount = input.soulInputs().getOrDefault(entry.getKey(), 0);
            if (availableSoulCount < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(RitualInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public Ingredient centerInput() {
        return centerInput;
    }

    @Override
    public List<Ingredient> cardinalInputs() {
        return cardinalInputs;
    }

    @Override
    public List<Ingredient> diagonalInputs() {
        return diagonalInputs;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.RITUAL_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.RITUAL.get();
    }
}
