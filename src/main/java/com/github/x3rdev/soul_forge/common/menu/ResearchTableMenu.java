package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ResearchTableMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerLevelAccess access;
    private final Player player;

    //Client
    public ResearchTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    //Server
    public ResearchTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.RESEARCH_TABLE.get(), containerId);
        this.container = new SimpleContainer(1);
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new Slot(container, 0, 1, 1));
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 9; j++) {
//                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//            }
//        }
//
//        for (int k = 0; k < 9; k++) {
//            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
//        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(player, container));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.RESEARCH_TABLE.get());
    }
}
