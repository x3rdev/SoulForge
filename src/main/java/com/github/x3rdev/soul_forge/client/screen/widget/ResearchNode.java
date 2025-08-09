package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResearchNode extends MoveableWidget {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_node.png");

    private final ResearchTableScreen screen;
    private final Holder.Reference<Research> research;
    private final ItemStack iconItemStack;
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    public ResearchNode(int x, int y, ResearchTableScreen screen, Holder.Reference<Research> research, int minX, int minY, int maxX, int maxY) {
        super(x-13, y-13, 26, 26, Component.literal(research.value().title()));
        this.screen = screen;
        this.research = research;
        this.iconItemStack = research.value().iconItemStack();
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return !this.research.value().inactive();
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        screen.setActiveResearch(this.research);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!this.isActive()) { // Minecraft checks active field but not isActive method
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isActive() {
        return super.isActive() && screen.treeScreenActive() && getNodeRenderState() != NodeRenderState.HIDDEN;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(screen.treeScreenActive() && getNodeRenderState() != NodeRenderState.HIDDEN) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(getX(), getY(), 2);
            Font font = Minecraft.getInstance().font;
            int textLength = font.width(getMessage());
            int offset = Math.ceilDiv(textLength + 2, 18);
            if (!isHovered()) {
                guiGraphics.enableScissor(minX, minY, maxX, maxY);
            }
//            float scrollAlpha = getNodeRenderState() == NodeRenderState.UNLOCKED ? 1.0F : 0.48F;
            float scrollAlpha = 1.0F;
            if (isHovered()) {
                guiGraphics.pose().translate(0, 0, 160);
                guiGraphics.drawString(font, getMessage(), 20 + (offset * 18 - textLength) / 2 + 1, 8, 0x181d24);
                for (int i = 0; i < offset; i++) {
                    guiGraphics.innerBlit(LOCATION, -2 + 20 + 18 * (i), -2 + 20 + 18 * (i) + 18, 1, 1+24, 0, (float) 30/256, (float) (30+18)/256, 0, (float) 24/256, 1.0F, 1.0F, 1.0F, scrollAlpha); // render left part of scroll
                }
            }
            guiGraphics.renderItem(iconItemStack, 5, 5);
            if(getNodeRenderState() == NodeRenderState.CAN_BE_UNLOCKED && !isHovered()) {
                guiGraphics.innerBlit(LOCATION, -2, -2+30, 1, 1+26, 160, (float) 48/256, (float) 78/256, 0, (float) 26/256, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            guiGraphics.innerBlit(LOCATION, -2, -2+20, 1, 1+26, 0, 0, (float) 20/256, 0, (float) 26/256, 1.0F, 1.0F, 1.0F, scrollAlpha); // render left part of scroll
            guiGraphics.innerBlit(LOCATION, -2+20+(isHovered() ? 18 * offset : 0), -2+20+(isHovered() ? 18 * offset : 0)+10, 1, 1+26, 0, (float) 20/256, (float) (20+10)/256, 0, (float) 26/256, 1.0F, 1.0F, 1.0F, scrollAlpha); // render left part of scroll
            if (!isHovered()) {
                guiGraphics.disableScissor();
            }
            guiGraphics.pose().popPose();
        }
    }

    private NodeRenderState getNodeRenderState() {
        if(Necronomicon.isResearchUnlocked(screen.getNecronomicon(), research) ) {
            return NodeRenderState.UNLOCKED;
        }
        Holder.Reference<Research> parent = research.value().getParent(Minecraft.getInstance().level.registryAccess());
        if(Necronomicon.isResearchUnlocked(screen.getNecronomicon(), parent)) {
            return NodeRenderState.CAN_BE_UNLOCKED;
        }
        return NodeRenderState.HIDDEN;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    enum NodeRenderState {
        UNLOCKED,
        CAN_BE_UNLOCKED,
        HIDDEN
    }
}
