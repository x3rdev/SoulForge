package com.github.x3rdev.soul_forge.common.compat.jei;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class SoulAnvilCategory implements IRecipeCategory<RecipeHolder<SoulAnvilRecipe>> {

    public static final ResourceLocation SPRITES = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/jei/soul_anvil.png");

    private final IDrawable icon;

    public SoulAnvilCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockItemRegistry.SOUL_ANVIL));
    }

    @Override
    public RecipeType<RecipeHolder<SoulAnvilRecipe>> getRecipeType() {
        return SoulForgePlugin.SOUL_ANVIL_RECIPE_TYPE.get();
    }

    @Override
    public Component getTitle() {
        return Component.literal("soul anvil category");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 99;
    }

    @Override
    public int getHeight() {
        return 102;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SoulAnvilRecipe> soulAnvilRecipeRecipeHolder, IFocusGroup focuses) {
        SoulAnvilRecipe recipe = soulAnvilRecipeRecipeHolder.value();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                builder.addInputSlot(23+i*18, 8+j*18).addIngredients(recipe.gridInput().ingredients().get(i + 3 * j));
            }
        }
        builder.addInputSlot(1, 1).addIngredients(recipe.outerInputs().get(0));
        builder.addInputSlot(82, 1).addIngredients(recipe.outerInputs().get(1));
        builder.addInputSlot(1, 51).addIngredients(recipe.outerInputs().get(2));
        builder.addInputSlot(82, 51).addIngredients(recipe.outerInputs().get(3));

        builder.addOutputSlot(41, 85).addItemStack(recipe.result());
    }

    @Override
    public void draw(RecipeHolder<SoulAnvilRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(SPRITES, 0, 0, 0, 0, 99,102);
    }
}
