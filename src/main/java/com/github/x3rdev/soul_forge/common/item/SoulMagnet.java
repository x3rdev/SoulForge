package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.common.entity.Soul;
import com.github.x3rdev.soul_forge.common.packet.SendParticlePayload;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
 import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoulMagnet extends Item {

    private final float range;

    public SoulMagnet(Properties properties, float range) {
        super(properties.stacksTo(1));
        this.range = range;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        tick(stack, level, entity);
    }

    public static void tick(ItemStack stack, Level level, Entity entity) {
        SoulMagnet magnet = ((SoulMagnet) stack.getItem());
        if (level.getGameTime() % 5 == 0 && !level.isClientSide() && entity instanceof Player player) {
            Set<ItemStack> soulContainers = new HashSet<>();
            for (ItemStack inventoryStack : player.getInventory().items) {
                if (inventoryStack.getItem() instanceof SoulContainer) {
                    soulContainers.add(inventoryStack);
                }
            }
            if(!soulContainers.isEmpty()) {
                List<Soul> souls = level.getEntities(EntityTypeTest.forClass(Soul.class), player.getBoundingBox().inflate(magnet.range, 1, magnet.range), e -> !e.isRemoved());
                for (Soul soul : souls) {
                    Vec3 soulToPlayer = soul.position().vectorTo(player.position().add(0,0.5,0)).normalize().scale(0.6);
                    for (ItemStack soulContainerStack : soulContainers) {
                        tryPickupSoul(level, player, soul, soulContainerStack, soulToPlayer);
                    }
                }
            }
        }
    }

    private static void tryPickupSoul(Level level, Player player, Soul soul, ItemStack soulContainerStack, Vec3 soulToPlayer) {
        if(!soul.isRemoved()) {
            SoulContainer soulContainer = ((SoulContainer) soulContainerStack.getItem());
            boolean filled = soulContainer.tryFillBottle(soulContainerStack, soul, player);
            if(filled) {
                level.playSound(null, soul.getX(), soul.getY(), soul.getZ(), SoundEvents.FOX_TELEPORT, SoundSource.PLAYERS);
                for (int i = 0; i < 10; i++) {
                    PacketDistributor.sendToPlayersTrackingEntity(soul,
                            new SendParticlePayload(ParticleRegistry.SOUL_PARTICLE.get(),
                                    soul.getX()+(level.random.nextFloat()-0.5)*0.3,
                                    soul.getY()+(level.random.nextFloat()-0.5)*0.3,
                                    soul.getZ()+(level.random.nextFloat()-0.5)*0.3,
                                    soulToPlayer.x()+(level.random.nextFloat()-0.5)*0.075,
                                    soulToPlayer.y()+(level.random.nextFloat()-0.5)*0.075,
                                    soulToPlayer.z()+(level.random.nextFloat()-0.5)*0.075
                            )
                    );
                }
                soul.remove(Entity.RemovalReason.KILLED);
            }
        }
    }
}
