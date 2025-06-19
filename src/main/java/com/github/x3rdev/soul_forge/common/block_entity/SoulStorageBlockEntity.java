package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SoulStorageBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int maxCapacity;

    private SoulType soulType;
    private int soulCount;
    private long tickLoaded = 0;

    public SoulStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SOUL_STORAGE.get(), pos, blockState);
        this.soulType = SoulType.EMPTY;
        this.soulCount = 0;
        this.maxCapacity = 20;
    }

    public float getCrystalHeight(float partialTick) {
        int tick = getTick();
        int timeUntilApex = 3 * 20;
        int min = 1;
        int max = 3;
        if(tick < timeUntilApex) {
            return (float) (min + ((max-min) * (-(Math.cos(Math.PI * ((tick + partialTick) / timeUntilApex)) - 1) / 2)));
        } else {
            return max;
        }
    }

    public SoulType getSoulType() {
        return soulType;
    }

    public void setSoulType(SoulType soulType) {
        this.soulType = soulType;
    }

    public int getSoulCount() {
        return soulCount;
    }

    public void setSoulCount(int soulCount) {
        if(soulCount == 0) {
            setSoulType(SoulType.EMPTY);
        }
        this.soulCount = soulCount;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public int getContainedSoulColor() {
        return SoulType.SOUL.color();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("soul_type") != null) {
            this.soulType = SoulType.CODEC.parse(NbtOps.INSTANCE, tag.get("soul_type")).getOrThrow();
        }
        if(tag.get("soul_count") != null) {
            this.soulCount = Codec.INT.parse(NbtOps.INSTANCE, tag.get("soul_count")).getOrThrow();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("soul_type", SoulType.CODEC.encodeStart(NbtOps.INSTANCE, soulType).getOrThrow());
        tag.put("soul_count", Codec.INT.encodeStart(NbtOps.INSTANCE, soulCount).getOrThrow());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level.isClientSide()) {
            tickLoaded = this.level.getGameTime();
        }
    }

    public int getTick() {
        return (int) (this.level.getGameTime() - tickLoaded);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
