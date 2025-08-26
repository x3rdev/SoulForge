package com.github.x3rdev.soul_forge.client.screen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.widget.SoulAnvilStartButton;
import com.github.x3rdev.soul_forge.common.menu.SoulAnvilMenu;
import com.github.x3rdev.soul_forge.common.packet.StartSoulAnvilPayload;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class SoulAnvilScreen extends AbstractContainerScreen<SoulAnvilMenu> implements MenuAccess<SoulAnvilMenu> {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/soul_anvil.png");

    public SoulAnvilScreen(SoulAnvilMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 247;
        this.inventoryLabelY = this.imageHeight - 95;

    }

    @Override
    protected void init() {
        super.init();
        this.addWidget(new SoulAnvilStartButton(this, leftPos+69, topPos+97, 38, 14, Component.literal("test"), onHammerPress()));
    }

    private Button.OnPress onHammerPress() {
        return button -> {
            PacketDistributor.sendToServer(new StartSoulAnvilPayload(menu.containerId));
            this.onClose();
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().pushPose();
        Optional<RecipeHolder<SoulAnvilRecipe>> recipeInContainer = menu.getRecipeInContainer();
        if(menu.getRecipeProgress() > 0) {
            guiGraphics.blit(LOCATION, leftPos+68, topPos+96, 176, 0, 40, (int) (menu.getRecipeProgress()*16));
        } else if(recipeInContainer.isPresent()) {
            guiGraphics.blit(LOCATION, leftPos+68, topPos+96, 176, 0, 40, 16);
//            if(getMenu().getItems().get(13).isEmpty()) {
//                ItemStack result = recipeInContainer.get().value().result();
//                guiGraphics.renderFakeItem(result, leftPos + 80, topPos + 125);
//                guiGraphics.fill(RenderType.guiGhostRecipeOverlay(), leftPos + 80, topPos + 125, leftPos + 80 + 16, topPos + 125 + 16, 0x66FFFFFF);
//            }
        }
        guiGraphics.pose().popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xd4edff, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xd4edff, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
