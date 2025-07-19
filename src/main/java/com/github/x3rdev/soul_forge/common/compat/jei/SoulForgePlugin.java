package com.github.x3rdev.soul_forge.common.compat.jei;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Objects;
import java.util.function.Supplier;

@JeiPlugin
public class SoulForgePlugin implements IModPlugin {

    public static final Supplier<RecipeType<RecipeHolder<RitualRecipe>>> RITUAL_RECIPE_TYPE = RecipeType.createFromDeferredVanilla(RecipeTypeRegistry.RITUAL);

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, SoulForge.MOD_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new RitualCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientPacketListener level = Minecraft.getInstance().getConnection();
        RecipeManager recipeManager = Objects.requireNonNull(level).getRecipeManager();
        registration.addRecipes(RITUAL_RECIPE_TYPE.get(), recipeManager.getAllRecipesFor(RecipeTypeRegistry.RITUAL.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(RITUAL_RECIPE_TYPE.get(),
                BlockItemRegistry.PEDESTAL.get(),
                BlockItemRegistry.SOUL_STORAGE.get()
        );
    }
}
