package com.github.x3rdev.soul_forge.common.compat.jei;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import com.mojang.serialization.DataResult;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@JeiPlugin
public class SoulForgePlugin implements IModPlugin {

    public static final Supplier<RecipeType<RecipeHolder<RitualRecipe>>> RITUAL_RECIPE_TYPE = RecipeType.createFromDeferredVanilla(RecipeTypeRegistry.RITUAL);

    public static final Supplier<RecipeType<RecipeHolder<SoulAnvilRecipe>>> SOUL_ANVIL_RECIPE_TYPE = RecipeType.createFromDeferredVanilla(RecipeTypeRegistry.SOUL_ANVIL);

    private static IJeiRuntime jeiRuntime;
    private static RitualCategory ritualCategory;
    private static SoulAnvilCategory soulAnvilCategory;

    public static void showRecipes(List<ResourceLocation> locations) {
        if(jeiRuntime == null) throw new IllegalStateException("jeiRuntime is null");
        List<RecipeHolder<RitualRecipe>> recipes = locations.stream()
                .map(resourceLocation -> jeiRuntime.getRecipeManager()
                        .createRecipeLookup(RITUAL_RECIPE_TYPE.get())
                .get()
                .filter(r -> Objects.equals(r.id(), resourceLocation))
                .findFirst()
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "No recipe found for registry name: " + resourceLocation)))
                .map(DataResult::getOrThrow).toList();
        jeiRuntime.getRecipesGui().showRecipes(ritualCategory, recipes, List.of());
//        jeiRuntime.getRecipesGui().show(jeiRuntime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, outputStack));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        SoulForgePlugin.jeiRuntime = jeiRuntime;
    }

    @Override
    public void onRuntimeUnavailable() {
        SoulForgePlugin.jeiRuntime = null;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, SoulForge.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        ritualCategory = new RitualCategory(helper);
        soulAnvilCategory = new SoulAnvilCategory(helper);
        registration.addRecipeCategories(ritualCategory, soulAnvilCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientPacketListener level = Minecraft.getInstance().getConnection();
        RecipeManager recipeManager = Objects.requireNonNull(level).getRecipeManager();
        registration.addRecipes(RITUAL_RECIPE_TYPE.get(), recipeManager.getAllRecipesFor(RecipeTypeRegistry.RITUAL.get()));
        registration.addRecipes(SOUL_ANVIL_RECIPE_TYPE.get(), recipeManager.getAllRecipesFor(RecipeTypeRegistry.SOUL_ANVIL.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(
                RITUAL_RECIPE_TYPE.get(),
                BlockItemRegistry.RITUAL_ALTAR.get(),
                BlockItemRegistry.PEDESTAL.get(),
                BlockItemRegistry.SOUL_CAULDRON.get()
        );
        registration.addRecipeCatalysts(
                SOUL_ANVIL_RECIPE_TYPE.get(),
                BlockItemRegistry.SOUL_ANVIL.get()
        );
    }


}
