package com.github.x3rdev.soul_forge.common.compat.jei;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RitualCategory implements IRecipeCategory<RecipeHolder<RitualRecipe>> {

    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/ritual.png");

    private final IDrawableStatic background;
    private final IDrawable icon;

    public RitualCategory(IGuiHelper helper) {

        this.background = helper.createDrawable(BACKGROUND, 7, 15, 162, 72);
//        this.inventory = helper.createDrawable(BACKGROUND, 7, 101, 162, 36);
//        this.dyeSlot = helper.createDrawable(BACKGROUND, 7, 101, 18, 18);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockItemRegistry.PEDESTAL));
    }


    @Override
    public RecipeType<RecipeHolder<RitualRecipe>> getRecipeType() {
        return SoulForgePlugin.RITUAL_RECIPE_TYPE.get();
    }

    @Override
    public Component getTitle() {
        return Component.literal("ritual category");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RitualRecipe> recipe, IFocusGroup focuses) {

    }

    @Override
    public void draw(RecipeHolder<RitualRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.pose().pushPose();
        renderPedestals(guiGraphics);
        guiGraphics.pose().popPose();
    }

    private void renderPedestals(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(100-8, 56, 0);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 0, -16*3);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 16*2, -16*2);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 16*3, 0);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 16*2, 16*2);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 0, 16*3);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), -16*2, 16*2);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), -16*3, 0);
        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), -16*2, -16*2);


//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100-8+56, 56-8);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100-8, 112-8);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100-8-56, 56-8);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100-8-56, 56-8);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100+56-32-8, 56-32);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100-25-8, 25-8);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100+25-8, 75-8);
//        guiGraphics.renderFakeItem(BlockItemRegistry.PEDESTAL.get().getDefaultInstance(), 100-25-8, 75-8);
        guiGraphics.pose().popPose();
    }

    private void renderStorage(GuiGraphics guiGraphics) {
        SoulStorageBlockEntity blockEntity = new SoulStorageBlockEntity(BlockPos.ZERO, BlockRegistry.SOUL_STORAGE.get().defaultBlockState());
        blockEntity.setLevel(Minecraft.getInstance().level);
        BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        BlockEntityRenderer<SoulStorageBlockEntity> renderer = dispatcher.getRenderer(blockEntity);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(50, 50, 10);
        guiGraphics.pose().scale(10, -10, 10);

        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(30));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(180+45));
        renderer.render(blockEntity, 0, guiGraphics.pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        guiGraphics.pose().popPose();
    }
}
