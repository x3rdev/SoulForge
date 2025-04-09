package com.github.x3rdev.soul_forge.common.block_entity;

import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DarkTombBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected static final RawAnimation OPEN_TOMB = RawAnimation.begin().thenPlay("opening");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DarkTombBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DARK_TOMB.get(), pPos, pBlockState);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> PlayState.CONTINUE)
                .triggerableAnim("open_tomb", OPEN_TOMB));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
