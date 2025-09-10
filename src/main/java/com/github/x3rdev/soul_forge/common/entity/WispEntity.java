package com.github.x3rdev.soul_forge.common.entity;

import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.github.x3rdev.soul_forge.common.registry.SoundRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class WispEntity extends AmbientCreature implements GeoEntity, TraceableEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("fly");
    protected static final RawAnimation SKILL_ANIM = RawAnimation.begin().thenPlay("skill");
    private static final EntityDataAccessor<Boolean> DATA_IS_ON_COOLDOWN = SynchedEntityData.defineId(WispEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Nullable
    private Player owner;
    private int cooldown = 0;

    public WispEntity(EntityType<WispEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public WispEntity(Level level, Player owner) {
        this(EntityRegistry.WISP.get(), level);
        this.owner = owner;
    }

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .build();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (!ownerExists()) {
                this.discard();
                return;
            }
            if (!getOwner().level().equals(this.level()) || this.distanceToSqr(getOwner()) > 6400) {
                this.discard();
                return;
            }
            if (cooldown-- <= 0) {
                if (getOwner().getHealth() < getOwner().getMaxHealth()) {
                    cooldown = 20 * 10;
                    getOwner().heal(3F);
                    triggerAnim("controller", "skill");
                    playSound(SoundRegistry.WISP_DING.get());
                }
            }
        } else if (this.tickCount % 30 == 0) {
            level().addParticle(ParticleRegistry.SOUL_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (getOwner() != null) {
            Vec3 targetPos = targetPosition();
            Vec3 delta = position().vectorTo(targetPos);

            if (delta.lengthSqr() > 0.1) {
                if (delta.lengthSqr() < 1) {
                    setDeltaMovement(delta.normalize().scale(0.05));
                } else if (delta.lengthSqr() < 4) {
                    setDeltaMovement(delta.normalize().scale(0.25));
                } else if (delta.lengthSqr() < 8) {
                    setDeltaMovement(delta.normalize().scale(0.5));
                } else {
                    setDeltaMovement(delta.normalize().scale(0.75));
                }
            }
            lookAt(EntityAnchorArgument.Anchor.FEET, targetPos);
        }
    }

    private boolean ownerExists() {
        return getOwner() != null && level().players().contains(getOwner())
                && getOwner().getInventory().hasAnyMatching(stack -> stack.is(ItemRegistry.WISP_AMULET.get()));
    }

    private Vec3 targetPosition() {
        return getOwner().position().add(new Vec3(0.5, 2, -0.5).yRot((float) -Math.toRadians(getOwner().yBodyRot)));
    }

    @Nullable
    public Player getOwner() {
        if (!this.level().isClientSide()) {
            return owner;
        }
        return null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float pAmount) {
        this.playHurtSound(source);
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundRegistry.WISP_HURT.get();
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_ON_COOLDOWN, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 1, this::controller)
                .triggerableAnim("skill", SKILL_ANIM));
    }

    protected <E extends WispEntity> PlayState controller(final AnimationState<E> event) {
        if (event.isMoving()) {
            return event.setAndContinue(FLY_ANIM);
        }
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
