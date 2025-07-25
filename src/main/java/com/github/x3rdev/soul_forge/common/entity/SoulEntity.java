package com.github.x3rdev.soul_forge.common.entity;

import com.github.x3rdev.soul_forge.common.item.SoulBottle;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class SoulEntity extends Entity implements GeoEntity {
    private static final EntityDataAccessor<String> DATA_SOUL_TYPE = SynchedEntityData.defineId(SoulEntity.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean fake;

    public SoulEntity(EntityType<SoulEntity> pEntityType, Level pLevel, SoulType soulType, boolean fake) {
        super(pEntityType, pLevel);
        this.entityData.set(DATA_SOUL_TYPE, soulType.toString());
        this.noPhysics = false;
        this.fake = fake;
    }

    public SoulEntity(EntityType<SoulEntity> pEntityType, Level pLevel, SoulType soulType) {
        this(pEntityType, pLevel, soulType, false);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getDirectEntity().getType().equals(EntityType.PLAYER)) {
            for (int i = 0; i < 10; i++) {
                level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), Math.random()-0.5F, Math.random()-0.5F, Math.random()-0.5F);
            }
            this.remove(RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.0025D, 0.0D));
        }
        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        if(this.tickCount > 6000) {
            this.discard();
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof SoulBottle soulBottleItem) {
            boolean bottleFilled = soulBottleItem.tryFillBottle(stack, this, player);
            if (bottleFilled) {
                this.remove(RemovalReason.DISCARDED);
                return InteractionResult.sidedSuccess(player.level().isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    public SoulType getSoulType() {
        for (SoulType soulType : SoulType.values()) {
            if (soulType.toString().equals(entityData.get(DATA_SOUL_TYPE))) {
                return soulType;
            }
        }
        return SoulType.EMPTY;
    }

    public boolean isFake() {
        return fake;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SOUL_TYPE, SoulType.EMPTY.toString());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
