package com.github.x3rdev.soul_forge.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import software.bernie.geckolib.util.Color;

public class ResearchNodeConnector extends MoveableWidget {

    private final int dirX;
    private final int dirY;
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    public ResearchNodeConnector(int x, int y, int dirX, int dirY, int minX, int minY, int maxX, int maxY) {
        super(x, y, 0, 0, Component.literal("test"));
        this.dirX = dirX;
        this.dirY = dirY;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(minX, minY, maxX, maxY);
        guiGraphics.pose().translate(getX(), getY(), 0);
        if(dirY != 0) {
            guiGraphics.fill(-1, -1, (dirX/2)+2, 2, 0xFF000000);
            guiGraphics.fill(dirX/2-1, dirY-1, dirX+2, dirY+2, 0xFF000000);
            guiGraphics.fill(dirX/2-1, -1, dirX/2+2, dirY+2, 0xFF000000);
        } else {
            guiGraphics.fill(-1, -1, dirX+2, 2, 0xFF000000);
        }
        if(dirY != 0) {
            guiGraphics.fill(RenderType.gui(),0, 0, (dirX/2)+1, 1, 1, 0xFFbababa);
            guiGraphics.fill(RenderType.gui(),dirX/2, dirY, dirX+1, dirY+1, 1, 0xFFbababa);
            guiGraphics.fill(RenderType.gui(),dirX/2, 0, dirX/2+1, dirY+1, 1, 0xFFbababa);
        } else {
            guiGraphics.fill(RenderType.gui(),0, 0, dirX+1, 1, 1, 0xFFbababa);
        }
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }



    private void drawPixel(GuiGraphics guiGraphics, int x, int y, float alpha) {
        guiGraphics.fill(RenderType.gui(), x, y, x+1, y+1, Color.ofARGB(alpha, 0, 1, 1).getColor());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
