package com.github.x3rdev.soul_forge.common.compat.jei;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.recipe.RitualRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RitualCategory implements IRecipeCategory<RecipeHolder<RitualRecipe>> {

    public static final ResourceLocation SPRITES = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/ritual.png");

    private final IDrawable icon;

    public RitualCategory(IGuiHelper helper) {
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
        return 135;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RitualRecipe> recipe, IFocusGroup focuses) {
        int slotSize = 18;
        int yOffset = 81;
        builder.addInputSlot(slotSize*2+1, yOffset+1)
                .addIngredients(recipe.value().centerInput());
//
        List<Ingredient> cardinalInputs = new ArrayList<>(recipe.value().cardinalInputs());
        for (int i = 0; i < 4 - recipe.value().cardinalInputs().size(); i++) {
            cardinalInputs.add(Ingredient.EMPTY);
        }
        builder.addInputSlot(slotSize*2+1, yOffset-slotSize*2+1)
                .addIngredients(cardinalInputs.get(0));
        builder.addInputSlot(slotSize*4+1, yOffset+1)
                .addIngredients(cardinalInputs.get(1));
        builder.addInputSlot(slotSize*2+1, yOffset+slotSize*2+1)
                .addIngredients(cardinalInputs.get(2));
        builder.addInputSlot(1, yOffset+1)
                .addIngredients(cardinalInputs.get(3));
//
        List<Ingredient> diagonalInputs = new ArrayList<>(recipe.value().diagonalInputs());
        for (int i = 0; i < 4 - recipe.value().diagonalInputs().size(); i++) {
            diagonalInputs.add(Ingredient.EMPTY);
        }
        builder.addInputSlot(slotSize-9+1, yOffset-slotSize-9+1)
                .addIngredients(diagonalInputs.get(0));
        builder.addInputSlot(slotSize*4-9+1, yOffset-slotSize-9+1)
                .addIngredients(diagonalInputs.get(1));
        builder.addInputSlot(slotSize*4-9+1, yOffset+slotSize*2-9+1)
                .addIngredients(diagonalInputs.get(2));
        builder.addInputSlot(slotSize-9+1, yOffset+slotSize*2-9+1)
                .addIngredients(diagonalInputs.get(3));

        builder.addOutputSlot(181+1, yOffset+1)
                .addItemStack(recipe.value().result());

        builder.addSlot(RecipeIngredientRole.CATALYST, 121+1, yOffset+1)
                .addItemStack(ItemRegistry.NECRONOMICON.get().getDefaultInstance());
    }

    @Override
    public void draw(RecipeHolder<RitualRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.pose().pushPose();
        renderPedestals(guiGraphics, recipe);
        renderToolTips(guiGraphics, mouseX, mouseY);
        guiGraphics.pose().popPose();
    }

    private void renderPedestals(GuiGraphics guiGraphics, RecipeHolder<RitualRecipe> recipe) {
        guiGraphics.pose().pushPose();
        int slotSize = 18;
        int yOffset = 81;

        //Center slot
        guiGraphics.blit(SPRITES, slotSize*2, yOffset, 0, 0, 18, 18);
        //Cardinal slots
        guiGraphics.blit(SPRITES, slotSize*2, yOffset-slotSize*2, 0, 0, 18, 18);
        guiGraphics.blit(SPRITES, slotSize*4, yOffset, 0, 0, 18, 18);
        guiGraphics.blit(SPRITES, slotSize*2, yOffset+slotSize*2, 0, 0, 18, 18);
        guiGraphics.blit(SPRITES, 0, yOffset, 0, 0, 18, 18);
        //Diagonal slots
        guiGraphics.blit(SPRITES, slotSize-9, yOffset-slotSize-9, 0, 0, 18, 18);
        guiGraphics.blit(SPRITES, slotSize*4-9, yOffset-slotSize-9, 0, 0, 18, 18);
        guiGraphics.blit(SPRITES, slotSize*4-9, yOffset+slotSize*2-9, 0, 0, 18, 18);
        guiGraphics.blit(SPRITES, slotSize-9, yOffset+slotSize*2-9, 0, 0, 18, 18);

        guiGraphics.blit(SPRITES, 181, yOffset, 0, 0, 18, 18);

        guiGraphics.blit(SPRITES, 100, yOffset+2, 18, 0, 13, 13); //PLUS
        guiGraphics.blit(SPRITES, 125, yOffset+22, 53, 0, 9, 13); //MOUSE
        guiGraphics.blit(SPRITES, 148, yOffset+1, 31, 0, 22, 15); //ARROW


        renderStorage(guiGraphics, recipe);
        guiGraphics.pose().popPose();
    }

    private void renderStorage(GuiGraphics guiGraphics, RecipeHolder<RitualRecipe> recipe) {
        guiGraphics.pose().pushPose();
        renderSoul(guiGraphics, recipe, 40, 30, EntityRegistry.SOUL.get(), SoulType.SOUL);
        renderSoul(guiGraphics, recipe, 40+28, 30, EntityRegistry.UNDEAD_SOUL.get(), SoulType.UNDEAD_SOUL);
        renderSoul(guiGraphics, recipe, 40+28*2, 30, EntityRegistry.NETHER_SOUL.get(), SoulType.NETHER_SOUL);
        renderSoul(guiGraphics, recipe, 40+28*3, 30, EntityRegistry.ENDER_SOUL.get(), SoulType.ENDER_SOUL);
        renderSoul(guiGraphics, recipe, 40+28*4, 30, EntityRegistry.DRAGON_SOUL.get(), SoulType.DRAGON_SOUL);
        guiGraphics.pose().popPose();

        SoulStorageBlockEntity blockEntity = new SoulStorageBlockEntity(BlockPos.ZERO, BlockRegistry.SOUL_STORAGE.get().defaultBlockState());
        blockEntity.setLevel(Minecraft.getInstance().level);
        BlockEntityRenderDispatcher blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        BlockEntityRenderer<SoulStorageBlockEntity> storageRenderer = blockEntityRenderDispatcher.getRenderer(blockEntity);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(20, 39, 10);
        guiGraphics.pose().scale(10, -10, 10);
        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(30));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(180+45));
        storageRenderer.render(blockEntity, 0, guiGraphics.pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        guiGraphics.pose().popPose();
    }

    private void renderSoul(GuiGraphics guiGraphics, RecipeHolder<RitualRecipe> recipe, int x, int y, EntityType<SoulEntity> entityType, SoulType soulType) {
        SoulEntity soulEntity = new SoulEntity(entityType, Minecraft.getInstance().level, soulType, true);
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super SoulEntity> soulEntityRenderer = entityRenderDispatcher.getRenderer(soulEntity);

        guiGraphics.pose().pushPose();
        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(String.valueOf(recipe.value().inputSouls().getOrDefault(soulType, 0))), x+8, y-10, 0xFFFFFFFF);
        guiGraphics.pose().translate(x, y, 10);
        guiGraphics.pose().scale(15, -15, 15);
        soulEntityRenderer.render(soulEntity, 0, 0, guiGraphics.pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
        guiGraphics.pose().popPose();
    }

    private void renderToolTips(GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if(mouseX >= 3 && mouseY >= 0 && mouseX < 23 && mouseY < 45) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.soul_storage"), (int) mouseX, (int) mouseY);
        }
        if(mouseX >= 35 && mouseY >= 13 && mouseX < 45 && mouseY < 32) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.soul"), (int) mouseX, (int) mouseY);
        }
        if(mouseX >= 63 && mouseY >= 13 && mouseX < 74 && mouseY < 32) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.undead_soul"), (int) mouseX, (int) mouseY);
        }
        if(mouseX >= 91 && mouseY >= 13 && mouseX < 101 && mouseY < 32) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.nether_soul"), (int) mouseX, (int) mouseY);
        }
        if(mouseX >= 119 && mouseY >= 13 && mouseX < 129 && mouseY < 32) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.ender_soul"), (int) mouseX, (int) mouseY);
        }
        if(mouseX >= 147 && mouseY >= 13 && mouseX < 157 && mouseY < 32) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.dragon_soul"), (int) mouseX, (int) mouseY);
        }
        if(mouseX >= 124 && mouseY >= 102 && mouseX < 135 && mouseY < 117) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.soul_forge.jei.right_click_hint"), (int) mouseX, (int) mouseY);
        }
//        guiGraphics.fill(124, 102, 135, 117, 1000, 0x77FF0000);
    }
}
