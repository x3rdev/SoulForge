package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.packet.UpdateUnlockedResearchPayload;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ResearchTableMenu extends AbstractContainerMenu {

    public static final String[] WORDS = {"map","silver","node","pulse","grid","ember","cloud","axis","vector","stone","loop","signal","frame","byte","cache","logic","thread","kernel","stack","flux","jazz","quick","box","wizard","nymph","xenon","book", "short", "poor"};
    public static final int STONE_COUNT = 4;

    public final Player player;
    private final Container container;
    private final ContainerLevelAccess access;
    private final List<String> words;
    private Holder.Reference<Research> research;


    //Client
    public ResearchTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(
                containerId,
                playerInventory,
                ContainerLevelAccess.create(playerInventory.player.level(), byteBuf.readBlockPos())
        );
    }

    //Server
    public ResearchTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.RESEARCH_TABLE.get(), containerId);
        this.player = playerInventory.player;
        this.container = new SimpleContainer(1);
        this.access = access;
        this.research = Research.getEmptyResearch(player.registryAccess());
        this.words = new ArrayList<>();

        this.addSlot(new AncientTabletSlot(container, 0, 152, 57));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new ResearchTableSlot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new ResearchTableSlot(playerInventory, k, 8 + k * 18, 142));
        }

        selectNewWords();
    }

    public void tick() {
        if(isResearchSelected()) {
//            ItemStack[] items = research.value().unlockIngredient().getItems();
//            if(items.length != 0) {
//                int l = (int) (player.level().getGameTime() / 20 % items.length);
//                container.setItem(0, items[l]);
//            }
        }
    }

    public boolean isResearchSelected() {
        return !Research.isEmpty(research);
    }

    public ItemStack getTabletStack() {
        return container.getItem(0);
    }

    public void setActiveResearch(Holder.Reference<Research> research) {
        if(this.player.level().isClientSide()) {
            PacketDistributor.sendToServer(new UpdateUnlockedResearchPayload(research.key(), this.containerId));
        }
        this.research = research;
    }

    public void pickWord(int index) {
        if(this.player.level().isClientSide()) {
            PacketDistributor.sendToServer(new UpdateUnlockedResearchPayload(research.key(), this.containerId));
        }
        this.research = research;
    }

    public String getStoneWord(int index) {
        return words.get(index);
    }

    public void selectNewWords() {
        words.clear();
        for (int i = 0; i < STONE_COUNT; i++) {
            words.add(WORDS[nextInt(getWordStoneSeed()+i, WORDS.length)]);
        }
    }

    public int getWordStoneSeed() {
        return player.getData(DataAttachmentRegistry.WORD_STONE_SEED);
    }

    private int nextInt(int i, int mod) { //Just a simple "randomizing" function
        return Math.floorMod(i * 1664525 + 1013904223, mod);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, this.container));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.RESEARCH_TABLE.get());
    }

    public class ResearchTableSlot extends Slot {

        public ResearchTableSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean isActive() {
            return isResearchSelected();
        }

    }

    public class AncientTabletSlot extends Slot {

        public AncientTabletSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean isActive() {
            return isResearchSelected() && !Research.playerHasResearchUnlocked(player, research);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ItemRegistry.ANCIENT_TABLET);
        }
    }
}
