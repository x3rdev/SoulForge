package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.nergal.NergalEntity;
import com.github.x3rdev.soul_forge.common.entity.nergal.NergalSpawnEntity;
import com.github.x3rdev.soul_forge.common.packet.SendParticlePayload;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.github.x3rdev.soul_forge.common.scheduler.ServerScheduler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
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
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        stack.consume(1, livingEntity);
        Vec3 position = livingEntity.position();
        if(!level.isClientSide()) {
            NergalSpawnEntity nergalSpawn = new NergalSpawnEntity(level);
            nergalSpawn.setPos(position.add(0,0.1,0));
            level.addFreshEntity(nergalSpawn);
            for (int i = 0; i < 180; i++) {
                ServerScheduler.schedule(
                        () -> PacketDistributor.sendToPlayersTrackingEntity(nergalSpawn, new SendParticlePayload(
                                ParticleRegistry.RITUAL_TRAIL.get(),
                                position.x()+0.25*(Math.random()-0.5F), position.y()-0.05, position.z()+0.25*(Math.random()-0.5F),
                                0.05*(Math.random()-0.5F), 0.1, 0.05*(Math.random()-0.5F))
                        ),
                        i
                );
            }
            ServerScheduler.schedule(
                    () -> {
                        NergalEntity nergal = new NergalEntity(level);
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
        return stack;
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
