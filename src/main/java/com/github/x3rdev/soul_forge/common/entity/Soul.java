package com.github.x3rdev.soul_forge.common.entity;

import com.github.x3rdev.soul_forge.common.item.SoulBottle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Soul extends LivingEntity implements GeoEntity {
    private static final EntityDataAccessor<String> DATA_SOUL_TYPE = SynchedEntityData.defineId(Soul.class, EntityDataSerializers.STRING);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean fake;

    public Soul(EntityType<Soul> pEntityType, Level pLevel, SoulType soulType, boolean fake) {
        super(pEntityType, pLevel);
        this.entityData.set(DATA_SOUL_TYPE, soulType.toString());
        this.fake = fake;
    }

    public Soul(EntityType<Soul> pEntityType, Level pLevel, SoulType soulType) {
        this(pEntityType, pLevel, soulType, false);
    }

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0F)
                .build();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getDirectEntity() != null && source.getDirectEntity().getType().equals(EntityType.PLAYER)) {
            for (int i = 0; i < 10; i++) {
                level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), Math.random()-0.5F, Math.random()-0.5F, Math.random()-0.5F);
            }
            this.remove(RemovalReason.KILLED);
            return true;
        }
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void tick() {
        super.tick();
//        if (!this.isNoGravity()) {
//            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.0025D, 0.0D));
//        }
//        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
//            this.move(MoverType.SELF, this.getDeltaMovement());
//        }
        if(this.tickCount > 6000) {
            this.discard();
        }
        setXRot(0);
        setYRot(0);
        setYHeadRot(0);
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
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public float getXRot() {
        return 0;
    }

    @Override
    public float getYRot() {
        return 0;
    }

    @Override
    public float getYHeadRot() {
        return 0;
    }

    @Override
    public float getViewXRot(float partialTicks) {
        return 0;
    }

    @Override
    public float getViewYRot(float partialTicks) {
        return 0;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
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
