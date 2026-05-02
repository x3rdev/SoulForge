package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PedestalBlockEntity extends BlockEntity implements GeoBlockEntity, ContainerSingleItem {

    public static final int RITUAL_DURATION = 300;

    private final IItemHandler itemHandler = new InvWrapper(this);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private ItemStack item;
    private boolean ritualActive;
    private int ritualTicks;
    private @Nullable BlockPos ritualParentPos;

    public PedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.PEDESTAL.get(), pos, blockState);
        this.item = ItemStack.EMPTY;
        this.ritualActive = false;
        this.ritualTicks = 0;
        this.ritualParentPos = null;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity blockEntity) {
        if(blockEntity.isRitualActive()) {
            blockEntity.incrementRitualTicks();
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity blockEntity) {
        if(blockEntity.isRitualActive()) {
            blockEntity.incrementRitualTicks();
            if(blockEntity.getRitualTicks() > RITUAL_DURATION) {
                blockEntity.stopRitual();
                return;
            }
            if(blockEntity.getRitualTicks() < RITUAL_DURATION && blockEntity.getRitualTicks() % 3 == 0) {
                Optional<RitualAltarBlockEntity> altarOptional =
                        level.getBlockEntity(blockEntity.ritualParentPos, BlockEntityRegistry.RITUAL_ALTAR.get());
                if(altarOptional.isEmpty()) {
                    blockEntity.stopRitual();
                }
            }
        }
    }


    public boolean isRitualActive() {
        return this.ritualActive;
    }

    public void startRitual(BlockPos ritualParentPos) {
        this.ritualActive = true;
        this.ritualTicks = 0;
        this.ritualParentPos = ritualParentPos;
        this.setChanged();
    }

    public void stopRitual() {
        this.ritualActive = false;
        this.ritualTicks = 0;
        this.ritualParentPos = null;
        this.setChanged();
    }

    public int getRitualTicks() {
        return this.ritualTicks;
    }

    public void incrementRitualTicks() {
        ritualTicks++;
    }

    public @Nullable BlockPos getRitualParentPos() {
        return ritualParentPos;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("item") != null) {
            this.item = ItemStack.parse(registries, tag.get("item")).orElseThrow();
        } else {
            this.item = ItemStack.EMPTY;
        }
        if(tag.get("ritualActive") != null) {
            this.ritualActive = tag.getBoolean("ritualActive");
        }
        if(tag.get("ritualTicks") != null) {
            this.ritualTicks = tag.getInt("ritualTicks");
        }
        if(tag.get("ritualParentPos") != null) {
            this.ritualParentPos = BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.get("ritualParentPos")).getOrThrow();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(!item.isEmpty()) {
            tag.put("item", item.save(registries));
        }
        tag.putBoolean("ritualActive", ritualActive);
        tag.putInt("ritualTicks", ritualTicks);
        if(ritualParentPos != null) {
            tag.put("ritualParentPos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, ritualParentPos).getOrThrow());
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

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return isRitualActive();
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

    // client code start

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
