package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NergalSpawnEntity extends Entity implements GeoEntity {

    private static final RawAnimation ANIMATION = RawAnimation.begin().thenLoop("animation");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NergalSpawnEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public NergalSpawnEntity(Level level) {
        this(EntityRegistry.NERGAL_SPAWN.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount > 225) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "c", 0, state -> state.setAndContinue(ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
