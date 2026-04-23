package com.github.x3rdev.soul_forge.common;

import com.github.x3rdev.soul_forge.common.compat.ModCompatibility;
import com.github.x3rdev.soul_forge.common.compat.curios.CuriosCompat;
import com.github.x3rdev.soul_forge.common.enchantment.EnchantmentBootstrap;
import com.github.x3rdev.soul_forge.common.entity.Ghost;
import com.github.x3rdev.soul_forge.common.entity.Soul;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.entity.WispEntity;
import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import com.github.x3rdev.soul_forge.common.item.OccultNecklace;
import com.github.x3rdev.soul_forge.common.item.Scythe;
import com.github.x3rdev.soul_forge.common.item.WingItem;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.packet.SendResearchDataPayload;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import com.github.x3rdev.soul_forge.common.scheduler.ServerScheduler;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CommonSetup {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.PEDESTAL.get(),
                (blockEntity, direction) -> blockEntity.getItemHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.RITUAL_ALTAR.get(),
                (blockEntity, direction) -> blockEntity.getItemHandler()
        );
        if(ModCompatibility.curiosModPresent()) {
            CuriosCompat.registerCapabilities(event);
        }
    }



    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.WISP.get(), WispEntity.createAttributes());
        event.put(EntityRegistry.GHOST.get(), Ghost.createAttributes());
        event.put(EntityRegistry.NERGAL.get(), Nergal.createAttributes());
        event.put(EntityRegistry.SOUL.get(), Soul.createAttributes());
//        event.put(EntityRegistry.UNDEAD_SOUL.get(), Soul.createAttributes());
//        event.put(EntityRegistry.NETHER_SOUL.get(), Soul.createAttributes());
//        event.put(EntityRegistry.ENDER_SOUL.get(), Soul.createAttributes());
//        event.put(EntityRegistry.DRAGON_SOUL.get(), Soul.createAttributes());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                EntityRegistry.GHOST.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        //TODO this even handler is most likely unnecessary, but double check before removing
//        if (event.isWasDeath() && event.getOriginal().hasData(DataAttachmentRegistry.UNLOCKED_RESEARCH)) {
//            event.getEntity().setData(DataAttachmentRegistry.UNLOCKED_RESEARCH, event.getOriginal().getData(DataAttachmentRegistry.UNLOCKED_RESEARCH));
//        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SendResearchDataPayload(serverPlayer.getData(DataAttachmentRegistry.UNLOCKED_RESEARCH)));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if(!level.isClientSide() && event.getSource().getEntity() instanceof Player player) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            int reapingLevel = stack.getEnchantmentLevel(level.registryAccess().lookup(Registries.ENCHANTMENT).get().getOrThrow(EnchantmentBootstrap.REAPING));
            if(stack.getItem() instanceof Scythe || reapingLevel > 0) {
//                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_UNDEAD_SOUL)) {
//                    dropUndeadSoul(level, event.getEntity().position().add(0, 1.25F, 0));
//                    return;
//                }
//                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_NETHER_SOUL)) {
//                    dropNetherSoul(level, event.getEntity().position().add(0, 1.25F, 0));
//                    return;
//                }
//                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_ENDER_SOUL)) {
//                    dropEnderSoul(level, event.getEntity().position().add(0, 1.25F, 0));
//                    return;
//                }
//                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_DRAGON_SOUL)) {
//                    dropDragonSoul(level, event.getEntity().position().add(0, 1.25F, 0));
//                    return;
//                }
                ServerScheduler.schedule(() -> {
                    dropNormalSoul(level, event.getEntity().position().add(0, 0.25F, 0));
                }, 1);

            }
        }
    }

    private static boolean isEntityTypeInTag(LivingEntity entity, TagKey<EntityType<?>> tagKey) {
        return entity.getType().is(tagKey);
    }

//    private static void dropUndeadSoul(Level level, Vec3 pos) {
//        Soul soulEntity = new Soul(EntityRegistry.UNDEAD_SOUL.get(), level, SoulType.UNDEAD_SOUL);
//        soulEntity.setPos(pos);
//        level.addFreshEntity(soulEntity);
//    }
//    private static void dropNetherSoul(Level level, Vec3 pos) {
//        Soul soulEntity = new Soul(EntityRegistry.NETHER_SOUL.get(), level, SoulType.NETHER_SOUL);
//        soulEntity.setPos(pos);
//        level.addFreshEntity(soulEntity);
//    }
//    private static void dropEnderSoul(Level level, Vec3 pos) {
//        Soul soulEntity = new Soul(EntityRegistry.ENDER_SOUL.get(), level, SoulType.ENDER_SOUL);
//        soulEntity.setPos(pos);
//        level.addFreshEntity(soulEntity);
//    }
//    private static void dropDragonSoul(Level level, Vec3 pos) {
//        Soul soulEntity = new Soul(EntityRegistry.DRAGON_SOUL.get(), level, SoulType.DRAGON_SOUL);
//        soulEntity.setPos(pos);
//        level.addFreshEntity(soulEntity);
//    }
    private static void dropNormalSoul(Level level, Vec3 pos) {
        Soul soulEntity = new Soul(EntityRegistry.SOUL.get(), level, SoulType.SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        tickContainers(player);
    }

    private static void tickContainers(Player player) {
        if(!player.level().isClientSide()) {
            if(player.containerMenu instanceof ResearchTableMenu menu) {
                menu.tick();
            }
        }
    }

    private static Optional<ItemEntity> getItemLookingAt(ServerPlayer player) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle().normalize();

        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AABB box = AABB.ofSize(eyePos.add(lookVec.scale(i)), 1, 1, 1);
            entities.addAll(player.level().getEntities(player, box));
        }
        return entities.stream()
                .filter(ItemEntity.class::isInstance)
                .map(ItemEntity.class::cast)
                .min((o1, o2) -> (int) (o1.distanceToSqr(player) - o2.distanceToSqr(player)));
    }

    @SubscribeEvent
    public static void serverChatEvent(ServerChatEvent event) {

    }

    @SubscribeEvent
    public static void entityAttackEvent(LivingIncomingDamageEvent event) {
        if (isUndead(event.getSource().getEntity()) && event.getEntity() instanceof Player player) {
            if (ModCompatibility.curiosModPresent()) {
                Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
                if (curiosInventory.isPresent() && curiosInventory.get().isEquipped(ItemRegistry.OCCULT_NECKLACE.get())) {
                    event.setAmount(Math.max(event.getAmount() - 1.5f, 0));
                }
            }
            else {
                for (ItemStack item : player.getInventory().items) {
                    if (item.getItem() instanceof OccultNecklace) {
                        event.setAmount(Math.max(event.getAmount() - 1.5f, 0));
                        break;
                    }
                }
            }
        }
    }

    private static boolean isUndead(Entity e) {
        if (e instanceof LivingEntity entity) {
            switch (entity) {
                case Zombie zombie -> {
                    return true;
                }
                case AbstractSkeleton skeleton -> {
                    return true;
                }
                case Zoglin zoglin -> {
                    return true;
                }
                default -> {}
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void entityFallEvent(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getInventory().getArmor(2).getItem() instanceof WingItem item) {
                /* need to send packet to server containing player velocity
                double velocity = player.getDeltaMovement().y;
                System.out.println(event.getDistance());
                System.out.println(velocity);
                System.out.println(item.adjustedFallHeight((float) velocity));
                event.setDistance(item.adjustedFallHeight((float) velocity)); */
                event.setCanceled(true);
            }
        }
    }
}
