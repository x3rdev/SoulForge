package com.github.x3rdev.soul_forge.common.entity;

import com.github.x3rdev.soul_forge.common.item.SoulBottleItem;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;

public class SoulEntity extends Entity implements GeoEntity {
    private static final EntityDataAccessor<String> DATA_SOUL_TYPE = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public SoulEntity(EntityType<?> pEntityType, Level pLevel, SoulType soulType) {
        super(pEntityType, pLevel);
        this.entityData.define(DATA_SOUL_TYPE, soulType.toString());
        this.noPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.0025D, 0.0D));
        }
        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > (double)1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if(stack.getItem() instanceof SoulBottleItem soulBottleItem) {
            boolean bottleFilled = soulBottleItem.tryFillBottle(stack, this, pPlayer);
            if(bottleFilled) {
                this.remove(RemovalReason.DISCARDED);
                pPlayer.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH);
                return InteractionResult.sidedSuccess(pPlayer.level().isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    public SoulType getSoulType() {
        for (SoulTypes soulType : SoulTypes.values()) {
            if(soulType.toString().equals(entityData.get(DATA_SOUL_TYPE))) {
                return soulType;
            }
        }
        return SoulTypes.SOUL;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
