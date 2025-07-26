package com.github.x3rdev.soul_forge.client.screen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.widget.MoveableWidget;
import com.github.x3rdev.soul_forge.client.screen.widget.ResearchNode;
import com.github.x3rdev.soul_forge.client.screen.widget.ResearchNodeConnector;
import com.github.x3rdev.soul_forge.client.screen.widget.UnlockButton;
import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.github.x3rdev.soul_forge.common.research.ResearchTree;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {

    public static final ResourceLocation TREE_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_tree.png");
    public static final ResourceLocation INSPECT_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_inspect.png");
    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/soulwood_planks.png");
    public static final int DRAGGABLE_WINDOW_WIDTH = 160;
    public static final int DRAGGABLE_WINDOW_HEIGHT = 142;
    public static final int ICON_SIZE = 26;
    public static final int PADDING = ICON_SIZE/2;
    private double anchorX;
    private double anchorY;
    private int treeDepth;
    private int treeBreadth;
    private @Nullable Holder.Reference<Research> activeResearch;

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
        addRenderableWidget(new UnlockButton(leftPos+imageWidth/2-17, topPos+60, this));
    }

    public ItemStack getNecronomicon() {
        return getMenu().getItems().getFirst();
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
        if(mouseX-dragX > leftPos+8 && mouseX-dragX < leftPos+8+DRAGGABLE_WINDOW_WIDTH && mouseY-dragY > topPos+16 && mouseY-dragY < topPos+16+DRAGGABLE_WINDOW_HEIGHT) {
            anchorX += dragX;
            float xScroll = Math.min(ICON_SIZE/2F+PADDING, DRAGGABLE_WINDOW_WIDTH-(treeDepth));
            anchorX = Math.clamp(anchorX, xScroll, ICON_SIZE/2F+PADDING);
            anchorY += dragY;
            float yScroll = -Math.min(0, DRAGGABLE_WINDOW_HEIGHT-(treeBreadth+PADDING));
            anchorY = Math.clamp(anchorY, DRAGGABLE_WINDOW_HEIGHT/2F-yScroll, DRAGGABLE_WINDOW_HEIGHT/2F+yScroll);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().pushPose();
        if(treeScreenActive()) {
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
        if(inspectScreenActive()) {
            if(!Necronomicon.isResearchUnlocked(getNecronomicon(), getActiveResearch().orElseThrow())) {
//                ItemStack unlockStack = activeResearch.value().unlockItemStack();
//                guiGraphics.renderFakeItem(unlockStack, leftPos+14, topPos+35);
//                guiGraphics.drawString(this.font, unlockStack.getHoverName(), leftPos+41, topPos+41, 0xbababa, false);
//                guiGraphics.drawString(this.font, Component.literal(unlockStack.getCount() + "x"), leftPos+155, topPos+41, 0xbababa, false);
            }
            guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xbababa, false);
            renderDescription(guiGraphics);
        }
        guiGraphics.blit(TREE_SCREEN_LOCATION, leftPos - 21, topPos, 176, 0, 20, 20);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        guiGraphics.pose().popPose();
    }

    private void renderDescription(GuiGraphics guiGraphics) {
        int x = leftPos+155;
        int y = topPos+31;
        for (FormattedCharSequence formattedcharsequence : font.split(Component.literal(activeResearch.value().description()), 100)) {
            guiGraphics.drawString(font, formattedcharsequence, x, y, 0xbababa, false);
            y += 9;
        }
    }

    private void renderBlur(GuiGraphics guiGraphics){
        guiGraphics.fill(RenderType.gui(),
                leftPos+8, topPos+16,
                leftPos+8+DRAGGABLE_WINDOW_WIDTH, topPos+16+DRAGGABLE_WINDOW_HEIGHT,
                160,
                0x77000000);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xbababa, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if(treeScreenActive()) {
            guiGraphics.blit(TREE_SCREEN_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            guiGraphics.blit(BACKGROUND, leftPos + 8, topPos + 16, 0, -Mth.floor(anchorX), -Mth.floor(anchorY), DRAGGABLE_WINDOW_WIDTH, DRAGGABLE_WINDOW_HEIGHT, 16, 16);
        }
        if(inspectScreenActive()){
            guiGraphics.blit(INSPECT_SCREEN_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        }
    }

    public boolean treeScreenActive() {
        return getActiveResearch().isEmpty(); //If we have not selected a research, do default stuff
    }

    public boolean inspectScreenActive() {
        return getActiveResearch().isPresent(); //If we have selected a research, we should render new bg and do new behavior
    }

    public Optional<Holder.Reference<Research>> getActiveResearch() {
        return Optional.ofNullable(activeResearch);
    }

    public void setActiveResearch(Holder.Reference<Research> research) {
        this.activeResearch = research;
        this.menu.setActiveResearch(research);
    }


}
