package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ResearchTableMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final Player player;

    //Client
    public ResearchTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    //Server
    public ResearchTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.RESEARCH_TABLE.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
//        this.access.execute((p_39371_, p_39372_) -> this.clearContainer(player));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.RESEARCH_TABLE.get());
    }
}
