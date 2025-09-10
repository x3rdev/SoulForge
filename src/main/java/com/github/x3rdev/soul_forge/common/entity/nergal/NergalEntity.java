package com.github.x3rdev.soul_forge.common.entity.nergal;

import com.github.x3rdev.soul_forge.common.registry.EntityDataRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.SoundRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Predicate;

public class NergalEntity extends Monster implements GeoEntity, SmartBrainOwner<NergalEntity> {

    public static final EntityDataAccessor<AABB> DEBUG_ATTACK_BOX = SynchedEntityData.defineId(NergalEntity.class, EntityDataRegistry.DEBUG_BOX.get());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation NERGAL_SWING = RawAnimation.begin().thenPlay("attack1");
    private static final RawAnimation NERGAL_SWIPE = RawAnimation.begin().thenPlay("attack2");
    private static final RawAnimation NERGAL_SUMMON = RawAnimation.begin().thenPlay("summon");

    private int ticksAttacking = 0;

    public NergalEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public NergalEntity(Level level) {
        this(EntityRegistry.NERGAL.get(), level);
    }

    public Predicate<Entity> attackablePredicate() {
        return entity ->
                ((entity instanceof Mob) || (entity instanceof Player)) &&
                !entity.isInvulnerable() &&
                !entity.getType().equals(this.getType()) &&
                !entity.getType().equals(EntityRegistry.GHOST.get());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if(!attackablePredicate().test(entity)) {
            return false;
        }
        return super.doHurtTarget(entity);
    }

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ARMOR, 12F)
                .add(Attributes.ATTACK_DAMAGE, 30.0F)
                .add(Attributes.ATTACK_KNOCKBACK, 5F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5F)
                .add(Attributes.MAX_HEALTH, 70.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.22F)
                .add(Attributes.FOLLOW_RANGE, 64F)
                .build();
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    public int getTicksAttacking() {
        return ticksAttacking;
    }

    public void resetTicksAttacking() {
        this.ticksAttacking = 0;
    }

    public void incrementTicksAttacking() {
        ticksAttacking++;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<? extends ExtendedSensor<NergalEntity>> getSensors() {
        return List.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<NergalEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new NergalMoveToWalkTarget()
        );
    }

    @Override
    public BrainActivityGroup<NergalEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>().attackablePredicate(livingEntity -> attackablePredicate().test(livingEntity)),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<NergalEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new OneRandomBehaviour<>(
                    new NergalSwingAttack().cooldownFor(mob -> 80),
                    new NergalSwipeAttack().cooldownFor(mob -> 80),
                    new SummonGhostsAttack().cooldownFor(entity -> 160)
                )
        );
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "c", 1, state -> {
            if(state.isMoving()) {
                return state.setAndContinue(WALK);
            }
            return state.setAndContinue(IDLE);
        })
                .triggerableAnim("nergal_swing", NERGAL_SWING)
                .triggerableAnim("nergal_swipe", NERGAL_SWIPE)
                .triggerableAnim("nergal_summon", NERGAL_SUMMON));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DEBUG_ATTACK_BOX, AABB.INFINITE);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(6);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return super.getHurtSound(damageSource);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundRegistry.NERGAL_IDLE.get();
    }
}
