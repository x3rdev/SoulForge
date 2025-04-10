package com.github.x3rdev.soul_forge.common.entity;

import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NergalEntity extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NergalEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public NergalEntity(Level level) {
        this(EntityRegistry.NERGAL.get(), level);
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 250.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 30.0)
                .build();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
