package com.github.x3rdev.soul_forge.common.menu;

import com.github.x3rdev.soul_forge.common.block_entity.SoulAnvilBlockEntity;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilInput;
import com.github.x3rdev.soul_forge.common.recipe.SoulAnvilRecipe;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import com.github.x3rdev.soul_forge.common.registry.RecipeTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;
import java.util.stream.IntStream;

public class SoulAnvilMenu extends AbstractContainerMenu {

    public final Player player;
    private final Container container;
    private final SoulAnvilBlockEntity blockEntity;

    //Client
    public SoulAnvilMenu(int containerId, Inventory playerInventory, FriendlyByteBuf byteBuf) {
        this(
                containerId,
                playerInventory,
                playerInventory.player.level().getBlockEntity(byteBuf.readBlockPos(), BlockEntityRegistry.SOUL_ANVIL.get()).orElseThrow()
        );
    }

    //Server
    public SoulAnvilMenu(int containerId, Inventory playerInventory, SoulAnvilBlockEntity blockEntity) {
        super(MenuTypeRegistry.SOUL_ANVIL.get(), containerId);
        this.player = playerInventory.player;
        this.container = blockEntity;
        this.blockEntity = blockEntity;
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

    public Optional<RecipeHolder<SoulAnvilRecipe>> getRecipeInContainer() {
        SoulAnvilInput input = new SoulAnvilInput(
                CraftingInput.of(3, 3, IntStream.range(0, 9).mapToObj(container::getItem).toList()),
                IntStream.range(9, 13).mapToObj(container::getItem).toList());

        return this.player.level().getRecipeManager().getRecipeFor(RecipeTypeRegistry.SOUL_ANVIL.get(), input, this.player.level());
    }

    public void startAnvil() {
        blockEntity.startAnvil();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        this.container.stopOpen(player);
    }


}
