package com.github.x3rdev.soul_forge.client.screen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.widget.*;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.github.x3rdev.soul_forge.common.research.ResearchTree;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {
    public static final ResourceLocation INSPECT_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_inspect.png");
    public static final ResourceLocation SCROLL_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_scrolls.png");
    public static final ResourceLocation TREE_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_tree.png");
    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/souls_background.png");
    public static final int DRAGGABLE_WINDOW_WIDTH = 160;
    public static final int DRAGGABLE_WINDOW_HEIGHT = 142;
    public static final int ICON_SIZE = 26;
    public static final int PADDING = ICON_SIZE/2;
    public static final int MAX_DESCRIPTION_LINES = 7;
    public static final int LINE_HEIGHT = 4;
    private double anchorX;
    private double anchorY;
    private int treeDepth;
    private int treeBreadth;
    private Holder.Reference<Research> activeResearch;
    private int topDescriptionLine;
    private List<WordStoneButton> stones;

    public ResearchTableScreen(ResearchTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelX = this.leftPos + 8;
        this.inventoryLabelY = this.topPos + this.imageHeight - 94;
        this.anchorX = PADDING+PADDING;
        this.anchorY = DRAGGABLE_WINDOW_HEIGHT/2F;
        ResearchTree researchTree = ResearchTree.getResearchTree();
        addResearchTreeWidgets(researchTree, 0,0,0, 0);
        this.treeDepth = researchTree.pixelDepth();
        this.treeBreadth = researchTree.pixelBreadth();
        this.activeResearch = Research.getEmptyResearch(menu.player.registryAccess());
        addRenderableWidget(new ClickableRitualWidget(leftPos+138, topPos+35, this));
        addRenderableWidget(new ResearchTableBackButton(leftPos+148, topPos+4, this));
        stones = new ArrayList<>();
        for(int i = 0; i < ResearchTableMenu.STONE_COUNT; i++) {
            WordStoneButton widget = new WordStoneButton(this, i);
            addRenderableWidget(widget);
            stones.add(widget);
        }

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if(inspectScreenActive()) {
            int descriptionLineCount = this.font.split(Component.literal(getActiveResearch().value().description()), 2*90).size();
            topDescriptionLine = Math.clamp(topDescriptionLine-(int)scrollY, 0, Math.max(0, descriptionLineCount-MAX_DESCRIPTION_LINES-1));
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void addResearchTreeWidgets(ResearchTree tree, int lastX, int lastY, int offsetX, int offsetY) {
        addRenderableWidget(new ResearchNode(offsetX, offsetY, this, tree.getHead(),
                leftPos+8, topPos+16, leftPos+8+DRAGGABLE_WINDOW_WIDTH, topPos+16+DRAGGABLE_WINDOW_HEIGHT));
        addRenderableWidget(new ResearchNodeConnector(lastX, lastY, this, tree.getHead(), offsetX-lastX, offsetY-lastY,
                leftPos+8, topPos+16, leftPos+8+DRAGGABLE_WINDOW_WIDTH, topPos+16+DRAGGABLE_WINDOW_HEIGHT));
        int treeYOffset = (-tree.pixelBreadth()/2);
        for (ResearchTree childTree : tree.getChildren()) {
            treeYOffset += childTree.pixelBreadth()/2;
            addResearchTreeWidgets(childTree, offsetX, offsetY, offsetX + ICON_SIZE + PADDING, offsetY + treeYOffset);
            treeYOffset += childTree.pixelBreadth()/2 + PADDING;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(treeScreenActive()) {
            if (mouseX - dragX > leftPos + 8 && mouseX - dragX < leftPos + 8 + DRAGGABLE_WINDOW_WIDTH && mouseY - dragY > topPos + 16 && mouseY - dragY < topPos + 16 + DRAGGABLE_WINDOW_HEIGHT) {
                anchorX += dragX;
                float xScroll = Math.min(ICON_SIZE / 2F + PADDING, DRAGGABLE_WINDOW_WIDTH - (treeDepth));
                anchorX = Math.clamp(anchorX, xScroll, ICON_SIZE / 2F + PADDING);
                anchorY += dragY;
                float yScroll = -Math.min(0, DRAGGABLE_WINDOW_HEIGHT - (treeBreadth + PADDING));
                anchorY = Math.clamp(anchorY, DRAGGABLE_WINDOW_HEIGHT / 2F - yScroll, DRAGGABLE_WINDOW_HEIGHT / 2F + yScroll);
            }
        }
        if(inspectScreenActive()) {
            int x = leftPos + 106;
            int y = topPos + 30;
            int barBackgroundLength = MAX_DESCRIPTION_LINES*LINE_HEIGHT;
            int descriptionLineCount = font.split(Component.literal(activeResearch.value().description()), 2*90).size();
            int barLength = Mth.floor((float)barBackgroundLength*(float)MAX_DESCRIPTION_LINES/descriptionLineCount);
            if(mouseX > x-5 && mouseX < x+2 && mouseY > y+topDescriptionLine && mouseY < y+topDescriptionLine+barLength) {
                topDescriptionLine = (int) Math.clamp(topDescriptionLine+4*dragY, 0, Math.max(0, descriptionLineCount-MAX_DESCRIPTION_LINES-1));
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().pushPose();
        if(treeScreenActive()) {
            renderTreeScreen(guiGraphics);
        }
        if(inspectScreenActive()) {
            renderInspectScreen(guiGraphics);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        guiGraphics.pose().popPose();
    }

    private void renderTreeScreen(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5F, 0.5F, 1);
        boolean shouldRenderBlur = false;
        for (Renderable renderable : renderables) {
            if (renderable instanceof MoveableWidget widget) {
                widget.setX(leftPos + 8 + widget.getAnchorX() + Mth.floor(anchorX));
                widget.setY(topPos + 16 + widget.getAnchorY() + Mth.floor(anchorY));
                if (widget.isActive() && widget.isHovered()) shouldRenderBlur = true;
            }
        }
        guiGraphics.pose().popPose();
        if (shouldRenderBlur) renderBlur(guiGraphics);
    }

    private void renderBlur(GuiGraphics guiGraphics){
        guiGraphics.fill(RenderType.gui(),
                leftPos+8, topPos+16,
                leftPos+8+DRAGGABLE_WINDOW_WIDTH, topPos+16+DRAGGABLE_WINDOW_HEIGHT,
                160,
                0x77000000);
    }

    private void renderInspectScreen(GuiGraphics guiGraphics) {
        if(Research.playerHasResearchUnlocked(Minecraft.getInstance().player, getActiveResearch())) {
            renderUnlockedResearch(guiGraphics);
        } else {
            renderLockedResearch(guiGraphics);
        }
    }

    private void renderUnlockedResearch(GuiGraphics guiGraphics) {
        guiGraphics.blit(SCROLL_LOCATION, leftPos+7, topPos+17, 0, 52, 108, 52);
        guiGraphics.blit(SCROLL_LOCATION, leftPos+123, topPos+17, 0, 104, 46, 52);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5F, 0.5F, 1);
        guiGraphics.drawString(this.font, Component.literal("Unlocks"), 2*(leftPos+136)+1, 2*(topPos+22), 0x181d24, false);
        guiGraphics.drawString(this.font, Component.literal("ritual:"), 2*(leftPos+136)+6, 2*(topPos+22)+9, 0x181d24, false);
        guiGraphics.pose().popPose();
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xbababa, false);

        renderResearchTitle(guiGraphics);
        renderResearchDescription(guiGraphics);
        renderScrollBar(guiGraphics);
    }

    private void renderLockedResearch(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        String text = "test abcd some string blah blah blah mysterious text that should light up";
        MutableComponent component = Component.empty();
        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            component = component.append(Component.literal(String.valueOf(c)));
            if(getMenu().isCharDiscovered(c)) {

            }
        }
        guiGraphics.drawWordWrap(this.font, Component.literal(text).withStyle(EnchantmentNames.ROOT_STYLE), leftPos+16, topPos+20, 145,0x181d24);
        guiGraphics.pose().popPose();
    }

    private void renderResearchTitle(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, -0.5, 0);
        int x = leftPos+16;
        int y = topPos+22;
        guiGraphics.drawString(font, activeResearch.value().title(), x, y, 0x181d24, false);
        guiGraphics.pose().popPose();
    }

    private void renderResearchDescription(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        int scale = 2;
        guiGraphics.pose().scale(1F/scale, 1F/scale, 1);
        int x = scale*(leftPos+16);
        int y = scale*(topPos+31);
        List<FormattedCharSequence> lines = font.split(Component.literal(activeResearch.value().description()), 2*90);
        for (int i = topDescriptionLine; i < Math.min(lines.size(), topDescriptionLine+MAX_DESCRIPTION_LINES); i++) {
            if(i == Math.min(lines.size()-1, topDescriptionLine+MAX_DESCRIPTION_LINES)-1 && lines.size()-1 > topDescriptionLine+MAX_DESCRIPTION_LINES) {
                guiGraphics.drawString(font, Component.literal("..."), x, y, 0x181d24, false);
            } else {
                guiGraphics.drawString(font, lines.get(i), x, y, 0x181d24, false);
            }
            y += scale*LINE_HEIGHT;
        }
        guiGraphics.pose().popPose();
    }

    private void renderScrollBar(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        int scale = 4;
        guiGraphics.pose().scale(1F/scale, 1F/scale, 1);
        int x = scale*(leftPos + 106)+1;
        int y = scale*(topPos + 30);
        int descriptionLineCount = font.split(Component.literal(activeResearch.value().description()), 2*90).size();
        if(descriptionLineCount > MAX_DESCRIPTION_LINES) {
            int barBackgroundLength = MAX_DESCRIPTION_LINES * scale * LINE_HEIGHT;
            int barLength = Mth.floor((float) barBackgroundLength * (float) MAX_DESCRIPTION_LINES / descriptionLineCount);
            guiGraphics.fill(x, y, x + 2, y + barBackgroundLength + scale * (LINE_HEIGHT - 2), 0xFFbababa);
            guiGraphics.fill(x, y + topDescriptionLine * scale, x + 2, y + topDescriptionLine * scale + barLength, 0xFFFFFFFF);
        }
        guiGraphics.pose().popPose();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xbababa, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if(treeScreenActive()) {
            guiGraphics.blit(TREE_SCREEN_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            guiGraphics.blit(BACKGROUND, leftPos + 8, topPos + 16, 0, -Mth.floor(anchorX), -Mth.floor(anchorY), DRAGGABLE_WINDOW_WIDTH, DRAGGABLE_WINDOW_HEIGHT, 32, 32);
        }
        if(inspectScreenActive()){
            guiGraphics.blit(INSPECT_SCREEN_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        }
    }

    public boolean treeScreenActive() {
        return Research.isEmpty(getActiveResearch()); //If we have not selected a research, do default stuff
    }

    public boolean inspectScreenActive() {
        return !Research.isEmpty(getActiveResearch()); //If we have selected a research, we should render new bg and do new behavior
    }

    public Holder.Reference<Research> getActiveResearch() {
        return activeResearch;
    }

    public void setActiveResearch(Holder.Reference<Research> research) {
        this.activeResearch = research;
        this.menu.setActiveResearch(research);
    }

    public Font getFont() {
        return font;
    }

    public int getStoneX(int index) {
        if(index == 0) {
            return 0;
        }
        return getStoneX(index-1)+font.width(getMenu().getStoneWord(index-1))+1+2+2+2;
    }
}
