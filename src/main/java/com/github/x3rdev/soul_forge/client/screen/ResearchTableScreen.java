package com.github.x3rdev.soul_forge.client.screen;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.screen.widget.MoveableWidget;
import com.github.x3rdev.soul_forge.client.screen.widget.ResearchNode;
import com.github.x3rdev.soul_forge.client.screen.widget.ResearchNodeConnector;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {

    public static final ResourceLocation TREE_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_tree.png");
    public static final ResourceLocation UNLOCK_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/gui/research_table_unlock.png");
    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/soulwood_planks.png");
    public static final int DRAGGABLE_WINDOW_WIDTH = 160;
    public static final int DRAGGABLE_WINDOW_HEIGHT = 142;
    public static final int ICON_SIZE = 26;
    public static final int PADDING = ICON_SIZE/2;
    private double anchorX;
    private double anchorY;
    private int treeDepth;
    private int treeBreadth;
    private @Nullable Research research;

    public ResearchTableScreen(ResearchTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.inventoryLabelY = this.imageHeight - 93;
    }

    @Override
    protected void init() {
        super.init();
        this.anchorX = PADDING+PADDING;
        this.anchorY = DRAGGABLE_WINDOW_HEIGHT/2F;
        ResearchTree researchTree = ResearchTree.buildScreenTree();
        addResearchTreeWidgets(researchTree, 0,0,0, 0);
        this.treeDepth = researchTree.pixelDepth();
        this.treeBreadth = researchTree.pixelBreadth();
    }

    private void addResearchTreeWidgets(ResearchTree tree, int lastX, int lastY, int offsetX, int offsetY) {
        addRenderableWidget(new ResearchNode(offsetX, offsetY, this, tree.head,
                leftPos+8, topPos+16, leftPos+8+DRAGGABLE_WINDOW_WIDTH, topPos+16+DRAGGABLE_WINDOW_HEIGHT));
        addRenderableWidget(new ResearchNodeConnector(lastX, lastY, this, offsetX-lastX, offsetY-lastY,
                leftPos+8, topPos+16, leftPos+8+DRAGGABLE_WINDOW_WIDTH, topPos+16+DRAGGABLE_WINDOW_HEIGHT));
        int treeYOffset = (-tree.pixelBreadth()/2);
        for (ResearchTree childTree : tree.children) {
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
                    if (widget.isHovered()) shouldRenderBlur = true;
                }
            }
            guiGraphics.pose().popPose();
            if (shouldRenderBlur) renderBlur(guiGraphics);
        }
        if(unlockScreenActive()){

        }
        guiGraphics.blit(TREE_SCREEN_LOCATION, leftPos - 21, topPos, 176, 0, 20, 20);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        guiGraphics.pose().popPose();
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
        if(unlockScreenActive()){
            guiGraphics.blit(UNLOCK_SCREEN_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        }
    }

    public boolean treeScreenActive() {
        return getActiveResearch().isEmpty(); //If we have not selected a research, do default stuff
    }

    public boolean unlockScreenActive() {
        return getActiveResearch().isPresent(); //If we have selected a research, we should render new bg and do new behavior
    }

    public Optional<Research> getActiveResearch() {
        return Optional.ofNullable(research);
    }

    public void setActiveResearch(Research research) {
        this.research = research;
        this.menu.setActiveResearch(research);
    }

    private static final class ResearchTree implements Comparable<ResearchTree> {
        private final Research head;
        private final TreeSet<ResearchTree> children;

        private ResearchTree(Research head) {
            this.head = head;
            this.children = new TreeSet<>();
        }

        private ResearchTree(Research head, Set<ResearchTree> children) {
            this.head = head;
            this.children = new TreeSet<>(children);
        }

        private int pixelBreadth() {
            if(children.isEmpty()) {
                return ICON_SIZE;
            } else {
                int size = PADDING*(children.size()-1);
                for (ResearchTree child : children) {
                    size += child.pixelBreadth();
                }
                return size;
            }
        }

        private int pixelDepth() {
            if(children.isEmpty()) {
                return ICON_SIZE;
            } else {
                int max = -1;
                for (ResearchTree child : children) {
                    max = Math.max(max, child.pixelDepth());
                }
                return ICON_SIZE+PADDING+max;
            }

        }

        public static ResearchTree buildScreenTree() {
            // Creates a map of every research node to each of its children
            Map<Research, Set<Research>> parentToResearchMap = new HashMap<>();
            RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
            List<Research> sortedResearch = registryAccess.lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow().listElements().sorted(Comparator.comparingInt(Holder.Reference::hashCode)).map(Holder.Reference::value).toList();

            sortedResearch.forEach(research -> {
                parentToResearchMap.putIfAbsent(research.getParent(registryAccess), new HashSet<>());
                parentToResearchMap.get(research.getParent(registryAccess)).add(research);
            });
            ResearchTree tree = new ResearchTree(Research.getEmptyResearch());
            fillChildren(tree, parentToResearchMap);
            return new ResearchTree(
                    new Research(Research.EMPTY, "Research", ItemRegistry.NECRONOMICON.get().getDefaultInstance(), ItemStack.EMPTY, true),
                    tree.children
            );
        }

        private static void fillChildren(ResearchTree tree, Map<Research, Set<Research>> parentToResearchMap) {
            parentToResearchMap.getOrDefault(tree.head, Set.of()).forEach(research -> {
                tree.children.add(new ResearchTree(research));
            });
            tree.children.forEach(researchTree -> {
                fillChildren(researchTree, parentToResearchMap);
            });
//            tree.children.sort(ResearchTree::compareTo);
        }

        @Override
        public int compareTo(@NotNull ResearchTableScreen.ResearchTree o) {
            int childCount = this.children.size()-o.children.size();
            if(childCount == 0) {
                return this.head.iconItemStack().getItem().toString().compareTo(o.head.iconItemStack().getItem().toString());
            }
            return childCount;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ResearchTree otherTree) {
                return this.head.equals(otherTree.head) && this.children.equals(otherTree.children);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.head, this.children);
        }
    }
}
