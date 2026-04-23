package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.item.AncientTablet;
import com.github.x3rdev.soul_forge.common.packet.PickWordPayload;
import com.github.x3rdev.soul_forge.common.packet.SendDiscoveredCharsPayload;
import com.github.x3rdev.soul_forge.common.packet.UpdateUnlockedResearchPayload;
import com.github.x3rdev.soul_forge.common.registry.*;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class ResearchTableMenu extends AbstractContainerMenu {

    public static final int STONE_COUNT = 4;

    public final Player player;
    private final Container container;
    private final ContainerLevelAccess access;
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

        this.addSlot(new AncientTabletSlot(container, 0, 152, 57));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new ResearchTableSlot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.addSlot(new ResearchTableSlot(playerInventory, k, 8 + k * 18, 142));
        }
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
            PacketDistributor.sendToServer(
                    new UpdateUnlockedResearchPayload(research.key(), this.containerId));
        } else {
            PacketDistributor.sendToPlayer((ServerPlayer) this.player,
                    new SendDiscoveredCharsPayload(research.getKey(), this.player.getData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS).getOrDefault(research.key(), new ArrayList<>())));
        }
        this.research = research;
    }

    public void pickWord(int index) {
        if(this.player.level().isClientSide()) {
            PacketDistributor.sendToServer(new PickWordPayload(index, this.containerId));
        }
        String stoneWord = getStoneWord(index);
        Map<ResourceKey<Research>, List<Character>> discoveredChars =
                new HashMap<>(player.getData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS));
        List<Character> characters = new ArrayList<>(discoveredChars.getOrDefault(this.research.key(), Collections.emptyList()));
        for(int i = 0; i < stoneWord.length(); i++) {
            char c = stoneWord.charAt(i);
            if(!characters.contains(c)) {
                characters.add(c);
            }
        }
        discoveredChars.put(this.research.key(), characters);
        player.setData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS, discoveredChars);
        getTabletStack().shrink(1);
        if(!this.player.level().isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            List<Character> chars = discoveredChars.getOrDefault(this.research.key(), new ArrayList<>());
            SendDiscoveredCharsPayload payload = new SendDiscoveredCharsPayload(this.research.key(), chars);
            PacketDistributor.sendToPlayer(serverPlayer, payload);

            if(isIncantationFullyDiscovered(stoneWord) && !Research.playerHasResearchUnlocked(player, this.research)) {
                Research.grantResearchToPlayer(serverPlayer, this.research);
            }
        }
    }

    public String getStoneWord(int index) {
        ItemStack stack = container.getItem(0);
        List<String> tabletWordList = ((AncientTablet) stack.getItem()).getTabletWordList(stack, getWordStoneSeed());
        return tabletWordList.get(index);
    }

    public int getWordStoneSeed() {
        return getTabletStack().getOrDefault(DataComponentRegistry.ANCIENT_TABLET_SEED, -1);
    }

    public boolean isCharDiscovered(char c) {
        return player.getData(DataAttachmentRegistry.RESEARCH_DISCOVERED_CHARS).getOrDefault(research.key(), Collections.emptyList()).contains(c);
    }

    public boolean isIncantationFullyDiscovered(String word) {
        for (char c : word.toCharArray()) {
            if (!isCharDiscovered(c)) return false;
        }
        return true;
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
        public void set(ItemStack stack) {
            super.set(stack);
            Level level = ResearchTableMenu.this.player.level();
            if(level instanceof ServerLevel serverLevel) {
                if (stack.getItem() instanceof AncientTablet tablet) {
                    tablet.generateTabletSeed(serverLevel, stack);
                }
            }
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
