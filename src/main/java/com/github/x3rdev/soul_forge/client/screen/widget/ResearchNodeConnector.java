package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

public class ResearchNodeConnector extends MoveableWidget {

    private final ResearchTableScreen screen;
    private final int dirX;
    private final int dirY;
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    public ResearchNodeConnector(int x, int y, ResearchTableScreen screen, int dirX, int dirY, int minX, int minY, int maxX, int maxY) {
        super(x, y, 0, 0, Component.literal("test"));
        this.screen = screen;
        this.dirX = dirX;
        this.dirY = dirY;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && screen.researchTreeActive();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(screen.researchTreeActive()) {
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(minX, minY, maxX, maxY);
        guiGraphics.pose().translate(getX(), getY(), 0);
        if (dirY != 0) {
            guiGraphics.fill(-1, -1, (dirX / 2) + 2, 2, 0xFF000000);
            guiGraphics.fill(dirX / 2 - 1, dirY - 1, dirX + 2, dirY + 2, 0xFF000000);
            guiGraphics.fill(dirX / 2 - 1, -1, dirX / 2 + 2, dirY + 2, 0xFF000000);
        } else {
            guiGraphics.fill(-1, -1, dirX + 2, 2, 0xFF000000);
        }
        if (dirY != 0) {
            guiGraphics.fill(RenderType.gui(), 0, 0, (dirX / 2) + 1, 1, 1, 0xFFbababa);
            guiGraphics.fill(RenderType.gui(), dirX / 2, dirY, dirX + 1, dirY + 1, 1, 0xFFbababa);
            guiGraphics.fill(RenderType.gui(), dirX / 2, 0, dirX / 2 + 1, dirY + 1, 1, 0xFFbababa);
        } else {
            guiGraphics.fill(RenderType.gui(), 0, 0, dirX + 1, 1, 1, 0xFFbababa);
        }
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
