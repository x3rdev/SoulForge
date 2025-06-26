package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResearchNode extends MoveableWidget {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_node.png");

    private final ResearchTableScreen screen;
    private final Research research;
    private final ItemStack stack;
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    public ResearchNode(ResearchTableScreen screen, int x, int y, Research research, int minX, int minY, int maxX, int maxY) {
        super(x-13, y-13, 26, 26, Component.literal(research.title()));
        this.screen = screen;
        this.research = research;
        this.stack = research.icon();
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(getX(), getY(), 2);
        Font font = Minecraft.getInstance().font;
        int textLength = font.width(getMessage());
        int offset = Math.ceilDiv(textLength+2, 18);
        if(!isHovered()) {
            guiGraphics.enableScissor(minX, minY, maxX, maxY);
        }
        if(isHovered()) {
            guiGraphics.pose().translate(0, 0, 160);
            guiGraphics.drawString(font, getMessage(), 20+(offset*18-textLength)/2+1, 8, 0x181d24);
            for (int i = 0; i < offset; i++) {
                guiGraphics.blit(LOCATION, -2+20+18*(i), 1, 30, 0, 18, 24);
            }
        }
        guiGraphics.renderItem(stack, 5, 5);
        guiGraphics.blit(LOCATION, -2, 1, 0, 0, 20, 26); // render left part of scroll
        guiGraphics.blit(LOCATION, -2+20+(isHovered() ? 18*offset : 0), 1, 20, 0, 10, 26); // render right part of scroll
        if(!isHovered()) {
            guiGraphics.disableScissor();
        }
        guiGraphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


}
