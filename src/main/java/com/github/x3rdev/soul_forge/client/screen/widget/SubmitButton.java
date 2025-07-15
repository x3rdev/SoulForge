package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.packet.SubmitResearchPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class SubmitButton extends AbstractWidget {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_unlock.png");

    private final ResearchTableScreen screen;

    public SubmitButton(int x, int y, ResearchTableScreen screen) {
        super(x, y, 32, 14, Component.literal("Submit"));
        this.screen = screen;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && screen.unlockScreenActive();
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return isActive() && playerHasItemsForSubmit();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(isActive()) {
            guiGraphics.pose().pushPose();
            guiGraphics.blit(LOCATION, getX(), getY(), 176, isHovered() && playerHasItemsForSubmit() ? 16 : 0, 34, 16);
            guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("Submit"), getX()+2, getY()+4, playerHasItemsForSubmit() ? 0xbababa : 0x181d24);
            if(isHovered() && !playerHasItemsForSubmit()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal("Insufficient items"), mouseX, mouseY);
            }
            guiGraphics.pose().popPose();
        }
    }

    private boolean playerHasItemsForSubmit() {
        ItemStack unlockStack = screen.getActiveResearch().orElseThrow().value().unlockItemStack();
        return screen.getMenu().player.getInventory().countItem(unlockStack.getItem()) >= unlockStack.getCount();
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        if(playerHasItemsForSubmit()) {
            PacketDistributor.sendToServer(new SubmitResearchPayload(screen.getMenu().containerId));
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
