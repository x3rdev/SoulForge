package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import com.github.x3rdev.soul_forge.common.entity.nergal.NergalSpawn;
import com.github.x3rdev.soul_forge.common.packet.SendParticlePayload;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.github.x3rdev.soul_forge.common.scheduler.ServerScheduler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScrollOfShadows extends Item {

    public ScrollOfShadows() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if(livingEntity instanceof Player player) {
            stack.consume(1, livingEntity);
            if (!level.isClientSide()) {
                BlockHitResult blockhitresult = getPlayerPOVHitResult(
                        level, player, ClipContext.Fluid.NONE
                );
                Vec3 position = blockhitresult.getType().equals(HitResult.Type.MISS) ? livingEntity.position() : blockhitresult.getLocation();
                NergalSpawn nergalSpawn = new NergalSpawn(level);
                nergalSpawn.setPos(position.add(0, 0.1, 0));
                level.addFreshEntity(nergalSpawn);
                for (int i = 0; i < 180; i++) {
                    ServerScheduler.schedule(
                            () -> PacketDistributor.sendToPlayersTrackingEntity(nergalSpawn, new SendParticlePayload(
                                    ParticleRegistry.RITUAL_TRAIL.get(),
                                    position.x() + 0.25 * (Math.random() - 0.5F), position.y() - 0.05, position.z() + 0.25 * (Math.random() - 0.5F),
                                    0.05 * (Math.random() - 0.5F), 0.1, 0.05 * (Math.random() - 0.5F))
                            ),
                            i
                    );
                }
                ServerScheduler.schedule(
                        () -> {
                            Nergal nergal = new Nergal(level);
                            nergal.setPos(position);
                            level.addFreshEntity(nergal);
                            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
                            lightningbolt.setVisualOnly(true);
                            lightningbolt.setPos(position);
                            level.addFreshEntity(lightningbolt);
                        },
                        190
                );
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }
}
