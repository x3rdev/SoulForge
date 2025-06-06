package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SoulStorageBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private SoulType soulType;
    private int soulCount;
    private long tickLoaded = 0;

    public SoulStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SOUL_STORAGE.get(), pos, blockState);
        this.soulType = null;
        this.soulCount = 0;
    }

    public SoulType getSoulType() {
        return soulType;
    }

    public int getSoulCount() {
        return soulCount;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("soul") != null) {
            this.soulType = SoulType.CODEC.parse(NbtOps.INSTANCE, tag.get("soul")).getOrThrow();
        }
        if(tag.get("soul_count") != null) {
            this.soulCount = Codec.INT.parse(NbtOps.INSTANCE, tag.get("soul_count")).getOrThrow();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(soulType != null) {
            SoulType.CODEC.encodeStart(NbtOps.INSTANCE, soulType);
        }
        Codec.INT.encodeStart(NbtOps.INSTANCE, soulCount);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level.isClientSide()) {
            tickLoaded = this.level.getGameTime();
        }
    }

    public double getTick() {
        return this.level.getGameTime() - tickLoaded;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
