package com.github.x3rdev.soul_forge.common.recipe;

import com.github.x3rdev.soul_forge.common.registry.RecipeSerializerRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RitualRecipe implements Recipe<RitualInput> {

    private final Ingredient inputPrimary;
    private final Ingredient inputCatalystNorth;
    private final Ingredient inputCatalystNorthEast;
    private final Ingredient inputCatalystEast;
    private final Ingredient inputCatalystSouthEast;
    private final Ingredient inputCatalystSouth;
    private final Ingredient inputCatalystSouthWest;
    private final Ingredient inputCatalystWest;

    private final Ingredient inputCatalystNorthWest;

    private final ItemStack result;

    public RitualRecipe(Ingredient inputPrimary,
                        Ingredient inputCatalystNorth, Ingredient inputCatalystNorthEast,
                        Ingredient inputCatalystEast, Ingredient inputCatalystSouthEast,
                        Ingredient inputCatalystSouth, Ingredient inputCatalystSouthWest,
                        Ingredient inputCatalystWest, Ingredient inputCatalystNorthWest,
                        ItemStack result) {
        this.inputPrimary = inputPrimary;
        this.inputCatalystNorth = inputCatalystNorth;
        this.inputCatalystNorthEast = inputCatalystNorthEast;
        this.inputCatalystEast = inputCatalystEast;
        this.inputCatalystSouthEast = inputCatalystSouthEast;
        this.inputCatalystSouth = inputCatalystSouth;
        this.inputCatalystSouthWest = inputCatalystSouthWest;
        this.inputCatalystWest = inputCatalystWest;
        this.inputCatalystNorthWest = inputCatalystNorthWest;
        this.result = result;
    }

    @Override
    public boolean matches(RitualInput input, Level level) {

        return inputPrimary.test(input.primary()) &&
                inputCatalystNorth.test(input.catalystNorth()) &&
                inputCatalystNorthEast.test(input.catalystNorthEast()) &&
                inputCatalystEast.test(input.catalystEast()) &&
                inputCatalystSouthEast.test(input.catalystSouthEast()) &&
                inputCatalystSouth.test(input.catalystSouth()) &&
                inputCatalystSouthWest.test(input.catalystSouthWest()) &&
                inputCatalystWest.test(input.catalystWest()) &&
                inputCatalystNorthWest.test(input.catalystNorthWest());
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

    public Ingredient getInputPrimary() {
        return inputPrimary;
    }

    public Ingredient getInputCatalystNorth() {
        return inputCatalystNorth;
    }

    public Ingredient getInputCatalystNorthEast() {
        return inputCatalystNorthEast;
    }

    public Ingredient getInputCatalystEast() {
        return inputCatalystEast;
    }

    public Ingredient getInputCatalystSouthEast() {
        return inputCatalystSouthEast;
    }

    public Ingredient getInputCatalystSouth() {
        return inputCatalystSouth;
    }

    public Ingredient getInputCatalystSouthWest() {
        return inputCatalystSouthWest;
    }

    public Ingredient getInputCatalystWest() {
        return inputCatalystWest;
    }

    public Ingredient getInputCatalystNorthWest() {
        return inputCatalystNorthWest;
    }

    public ItemStack getResult() {
        return result;
    }
}
