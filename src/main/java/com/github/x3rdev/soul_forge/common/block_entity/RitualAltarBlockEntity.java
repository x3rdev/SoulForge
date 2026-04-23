package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RitualAltarBlockEntity extends BlockEntity implements GeoBlockEntity, ContainerSingleItem {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation RITUAL_ANIM = RawAnimation.begin().thenPlay("ritual");

    private final IItemHandler itemHandler = new InvWrapper(this);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private ItemStack item;

    public RitualAltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.RITUAL_ALTAR.get(), pos, state);
        this.item = ItemStack.EMPTY;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("item") != null) {
            this.item = ItemStack.parse(registries, tag.get("item")).orElseThrow();
        } else {
            this.item = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(!item.isEmpty()) {
            tag.put("item", item.save(registries));
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public ItemStack getTheItem() {
        return this.item;
    }

    @Override
    public void setTheItem(ItemStack item) {
        this.item = item;
        this.setChanged();
    }

    @Override
    public ItemStack removeTheItem() {
        ItemStack copy = getTheItem().copyAndClear();
        setChanged();
        return copy;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.getItem(slot).isEmpty() && stack.getCount() == 1;
    }

    //TODO make this method return false during ritual
    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    //client code start

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::controller)
                .triggerableAnim("ritual", RITUAL_ANIM));
    }

    protected <E extends RitualAltarBlockEntity> PlayState controller(final AnimationState<E> event) {
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
