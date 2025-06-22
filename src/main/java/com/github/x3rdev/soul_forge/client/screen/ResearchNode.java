package com.github.x3rdev.soul_forge.client.screen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResearchNode extends MoveableWidget {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_icons.png");

    private final Research research;
    private final ItemStack stack;
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    public ResearchNode(int x, int y, Research research, int minX, int minY, int maxX, int maxY) {
        super(x-13, y-13, 26, 26, Component.literal("test"));
        this.research = research;
        this.stack = research == null ? ItemRegistry.NECRONOMICON.get().getDefaultInstance() : research.icon();
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(minX, minY, maxX, maxY);
        guiGraphics.pose().translate(getX(), getY(), 2);
        guiGraphics.renderItem(stack, 5, 5);
        guiGraphics.blit(LOCATION, 0, 0, 0, 0, 26, 26);
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
        if(isHovered) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, getMessage(), mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


}
