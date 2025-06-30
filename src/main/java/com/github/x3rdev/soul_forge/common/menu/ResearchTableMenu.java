package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.packet.UpdateResearchPayload;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class ResearchTableMenu extends AbstractContainerMenu {

    protected final Container container;
    private final ContainerLevelAccess access;
    private final Player player;
    private @Nullable Research research;

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
        container.setItem(0, player.getItemInHand(hand).copy());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new MinigameSlot(container, j + i * 9 + 1, 8 + j * 18, 17 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return super.mayPlace(stack) && stack.getItem() instanceof Rune;
                    }
                });
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new ResearchTableSlot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new ResearchTableSlot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public boolean isMinigameActive() {
        return research != null;
    }

    public void setActiveResearch(Research research) {
        if(this.player.level().isClientSide()) {
            PacketDistributor.sendToServer(new UpdateResearchPayload(research, this.containerId));
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
            return isMinigameActive();
        }

    }

    public class MinigameSlot extends ResearchTableSlot {

        public MinigameSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean isActive() {
            return super.isActive();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

    }
}
