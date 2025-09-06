package com.github.x3rdev.soul_forge.common.entity;

import com.github.x3rdev.soul_forge.common.registry.DamageTypeRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SoulScytheProjectileEntity extends Projectile implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ItemStack stack;
    private boolean returning = false;
    private int age;

    public SoulScytheProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.stack = ItemStack.EMPTY;
    }

    public SoulScytheProjectileEntity(Level level, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.SOUL_SCYTHE_PROJECTILE.get(), level);
        setOwner(owner);
        this.stack = stack;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick() {
        Entity owner = this.getOwner();
        if (this.level().isClientSide || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition()) && this.age++ < 200) {
            super.tick();
            level().getEntities(this, this.getBoundingBox().inflate(0.25F)).forEach(entity -> {
                if (entity != getOwner()) {
                    entity.hurt(new DamageTypeRegistry(level().registryAccess()).scythe(this, owner), 8);
                }
            });
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            if (age > 45) {
                returnProjectile();
            }
            if (getOwner() != null && returning && distanceToSqr(getOwner()) < 2.5) {
                this.remove(RemovalReason.DISCARDED);
            }

            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        returnProjectile();
    }

    private void returnProjectile() {
        this.returning = true;
    }

    @Override
    public Vec3 getDeltaMovement() {
        int i = 1;
        if (returning) {
            if (getOwner() != null) {
                setDeltaMovement(position().vectorTo(getOwner().getPosition(0).add(0, 1, 0)).normalize());
            } else {
                i = -1;
            }
        }
        return super.getDeltaMovement().scale(i);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public ItemStack getItem() {
        return stack.isEmpty() ? ItemRegistry.SOUL_SCYTHE.get().getDefaultInstance() : stack;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
