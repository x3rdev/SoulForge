package com.github.x3rdev.soul_forge.client.screen.widget;

import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.item.AncientTablet;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class WordStoneButton extends AbstractWidget {

    private final ResearchTableScreen screen;
    private final int index;

    public WordStoneButton(ResearchTableScreen screen, int index) {
        super(0, screen.getGuiTop()+57, 0, 12, Component.literal("word stone button"));
        this.screen = screen;
        this.index = index;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(isActive()) {
            this.setX(screen.getGuiLeft()+7+screen.getStoneX(index));
            guiGraphics.blit(ResearchTableScreen.INSPECT_SCREEN_LOCATION, getX(), getY(), 176, 20, 2, 16);
            int offset = 0;
            String word = screen.getMenu().getStoneWord(index);
            int wordWidth = screen.getFont().width(word)+1;
            this.setWidth(wordWidth);
            while (wordWidth > 0) {
                int sub = Math.min(wordWidth, 22);
                guiGraphics.blit(ResearchTableScreen.INSPECT_SCREEN_LOCATION, getX()+2+offset, getY(), 178, 20, sub, 16);
                wordWidth -= sub;
                offset += sub;
            }
            guiGraphics.blit(ResearchTableScreen.INSPECT_SCREEN_LOCATION, getX()+offset+2, getY(), 200, 20, 2, 16);
            guiGraphics.drawString(screen.getFont(), Component.literal(word), getX()+3, getY()+4, isHovered() ? 0x98caff : 0xbababa, false);
        }
    }

    @Override
    public int getWidth() {
        if(this.isActive()) {
            return screen.getFont().width(screen.getMenu().getStoneWord(index))+1+2+2;
        }
        return 0;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(isActive()) {
            super.onClick(mouseX, mouseY);
            screen.getMenu().pickWord(this.index);
        }
    }

    @Override
    public void playDownSound(SoundManager handler) {
        if(isActive()) {
            super.playDownSound(handler);
        }
    }

    @Override
    public boolean isActive() {
        return super.isActive() &&
                screen.inspectScreenActive() &&
                !Research.playerHasResearchUnlocked(Minecraft.getInstance().player, screen.getActiveResearch()) &&
                !screen.getMenu().getTabletStack().isEmpty() &&
                index < ((AncientTablet) screen.getMenu().getTabletStack().getItem()).getWordCount(screen.getMenu().getWordStoneSeed());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
