package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class ResearchTableBackButton extends AbstractWidget {

    private final ResearchTableScreen screen;

    public ResearchTableBackButton(int x, int y, ResearchTableScreen screen) {
        super(x, y, 20, 10, Component.literal("back"));
        this.screen = screen;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(isActive()) {
            guiGraphics.blit(ResearchTableScreen.INSPECT_SCREEN_LOCATION, getX(), getY(), 214, isHovered() ? 10 : 0, getWidth(), getHeight());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if(isActive()) {
            super.onClick(mouseX, mouseY, button);
            screen.setActiveResearch(Research.getEmptyResearch(Minecraft.getInstance().level.registryAccess()));
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        if(isActive()) {
            super.playDownSound(handler);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean isActive() {
        return super.isActive() && screen.inspectScreenActive();
    }
}
