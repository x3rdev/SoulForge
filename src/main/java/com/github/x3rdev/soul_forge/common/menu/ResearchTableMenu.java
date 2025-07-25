package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.item.Necronomicon;
import com.github.x3rdev.soul_forge.common.packet.UpdateResearchPayload;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import com.github.x3rdev.soul_forge.common.registry.SoundRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.github.x3rdev.soul_forge.common.research.ResearchTree;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class ResearchTableMenu extends AbstractContainerMenu {

    public final Player player;
    private final Container container;
    private final ContainerLevelAccess access;
    private @Nullable Holder.Reference<Research> research;

    //Client
    public ResearchTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(
                containerId,
                playerInventory,
                ContainerLevelAccess.create(playerInventory.player.level(), byteBuf.readBlockPos()),
                byteBuf.readEnum(InteractionHand.class)
        );
    }

    //Server
    public ResearchTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, InteractionHand hand) {
        super(MenuTypeRegistry.RESEARCH_TABLE.get(), containerId);
        this.container = new SimpleContainer(28);
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new Slot(container, 0, -19, 2){
            @Override
            public boolean mayPickup(Player player) {
                return false;
            }

            @Override
            public boolean isHighlightable() {
                return false;
            }
        });
        container.setItem(0, player.getItemInHand(hand).copyAndClear());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new ResearchTableSlot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new ResearchTableSlot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void submitResearch() {
        if(player.level().isClientSide()) throw new IllegalCallerException("SubmitResearch called on client");
        if(research == null) throw new IllegalArgumentException("Sent SubmitResearch packet while not in research unlock screen");
        ItemStack unlockStack = research.value().unlockItemStack();
        int count = unlockStack.getCount();
        Inventory inventory = player.getInventory();
        if(inventory.countItem(unlockStack.getItem()) >= count) {
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < inventory.getContainerSize(); j++) {
                    ItemStack itemstack = inventory.getItem(j);
                    if (itemstack.getItem().equals(unlockStack.getItem())) {
                        itemstack.shrink(1);
                    }
                }
            }
            Necronomicon.unlockResearch(player, getNecronomicon(), research);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.9F, 0.3F);
            player.closeContainer();
        }
    }

    public boolean isResearchSelected() {
        return research != null;
    }

    public ItemStack getNecronomicon() {
        return getItems().getFirst();
    }

    public void setActiveResearch(Holder.Reference<Research> research) {
        if(this.player.level().isClientSide()) {
            PacketDistributor.sendToServer(new UpdateResearchPayload(research.key(), this.containerId));
        }
        this.research = research;
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
}
