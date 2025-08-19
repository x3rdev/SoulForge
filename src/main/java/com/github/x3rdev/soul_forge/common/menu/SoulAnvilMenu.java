package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.block_entity.SoulAnvilBlockEntity;
import com.github.x3rdev.soul_forge.common.registry.BlockRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SoulAnvilMenu extends AbstractContainerMenu {

    public final Player player;
    private final Container container;
    private final ContainerLevelAccess access;

    //Client
    public SoulAnvilMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(
                containerId,
                playerInventory,
                ContainerLevelAccess.create(playerInventory.player.level(), byteBuf.readBlockPos())
        );
    }

    //Server
    public SoulAnvilMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.SOUL_ANVIL.get(), containerId);
        this.container = new SimpleContainer(SoulAnvilBlockEntity.SOUL_ANVIL_CONTAINER_SIZE);
        this.access = access;
        this.player = playerInventory.player;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new Slot(container, i+j*3, 62+i*18, 25+j*18));
            }
        }
        addSlot(new Slot(container, 9, 40, 18));
        addSlot(new Slot(container, 10, 121, 18));
        addSlot(new Slot(container, 11, 40, 68));
        addSlot(new Slot(container, 12, 121, 68));

        addSlot(new Slot(container, 13, 80, 102) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 135 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 193));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, BlockRegistry.SOUL_ANVIL.get());
    }
}
