package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SoulStorageBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final RandomSource randomSource;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private SoulType soulType;

    public SoulStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.SOUL_STORAGE.get(), pos, blockState);
        this.randomSource = RandomSource.create(pos.asLong());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.get("soul") != null) {
            this.soulType = SoulType.CODEC.parse(NbtOps.INSTANCE, tag.get("soul")).result().get();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(soulType != null) {
            SoulType.CODEC.encodeStart(NbtOps.INSTANCE, soulType);
        }
    }

    public RandomSource getRandomSource() {
        return this.randomSource;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
