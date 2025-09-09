package com.github.x3rdev.soul_forge.common;

import com.github.x3rdev.soul_forge.common.datagen.SoulForgeEntityTagsProvider;
import com.github.x3rdev.soul_forge.common.entity.*;
import com.github.x3rdev.soul_forge.common.entity.nergal.NergalEntity;
import com.github.x3rdev.soul_forge.common.item.Scythe;
import com.github.x3rdev.soul_forge.common.menu.ResearchTableMenu;
import com.github.x3rdev.soul_forge.common.packet.SendResearchDataPayload;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.DataAttachmentRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.github.x3rdev.soul_forge.common.research.ResearchTree;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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
    }

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.WISP.get(), WispEntity.createAttributes());
        event.put(EntityRegistry.GHOST.get(), GhostEntity.createAttributes());
        event.put(EntityRegistry.NERGAL.get(), NergalEntity.createAttributes());
        event.put(EntityRegistry.SOUL.get(), SoulEntity.createAttributes());
        event.put(EntityRegistry.UNDEAD_SOUL.get(), SoulEntity.createAttributes());
        event.put(EntityRegistry.NETHER_SOUL.get(), SoulEntity.createAttributes());
        event.put(EntityRegistry.ENDER_SOUL.get(), SoulEntity.createAttributes());
        event.put(EntityRegistry.DRAGON_SOUL.get(), SoulEntity.createAttributes());
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
        if (event.isWasDeath() && event.getOriginal().hasData(DataAttachmentRegistry.UNLOCKED_RESEARCH)) {
            event.getEntity().setData(DataAttachmentRegistry.UNLOCKED_RESEARCH, event.getOriginal().getData(DataAttachmentRegistry.UNLOCKED_RESEARCH));
        }
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
            if(stack.getItem() instanceof Scythe) {
                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_UNDEAD_SOUL)) {
                    dropUndeadSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                    return;
                }
                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_NETHER_SOUL)) {
                    dropNetherSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                    return;
                }
                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_ENDER_SOUL)) {
                    dropEnderSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                    return;
                }
                if(isEntityTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_DRAGON_SOUL)) {
                    dropDragonSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                    return;
                }
                dropNormalSoul(level, event.getEntity().position().add(0, 1.25F, 0));
            }
        }
    }

    private static boolean isEntityTypeInTag(LivingEntity entity, TagKey<EntityType<?>> tagKey) {
        return entity.getType().is(tagKey);
    }

    private static void dropUndeadSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.UNDEAD_SOUL.get(), level, SoulType.UNDEAD_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropNetherSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.NETHER_SOUL.get(), level, SoulType.NETHER_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropEnderSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.ENDER_SOUL.get(), level, SoulType.ENDER_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropDragonSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.DRAGON_SOUL.get(), level, SoulType.DRAGON_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropNormalSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.SOUL.get(), level, SoulType.SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }

    private static int inspectProgress = 0;

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        tickContainers(player);
        tickInspect(player);
    }

    private static void tickContainers(Player player) {
        if(!player.level().isClientSide()) {
            if(player.containerMenu instanceof ResearchTableMenu menu) {
                menu.tick();
            }
        }
    }

    private static void tickInspect(Player player) {
        if(!player.level().isClientSide() && Research.playerHasResearchGlasses(player)) {
            Optional<ItemEntity> itemLookingAt = getItemLookingAt(((ServerPlayer) player));
            if (itemLookingAt.isPresent() && Research.isItemUsedToUnlockNextResearch(itemLookingAt.get().getItem(), player)){
                inspectProgress++;
                if(inspectProgress % 4 == 0) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7F, 0.2F);
                }
                if (inspectProgress == 40) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.7F, 0.2F);
                    Research.getCachedUnlockableResearch(player).forEach(researchReference -> {
                        if (researchReference.value().unlockIngredient().test(itemLookingAt.get().getItem())) {
                            Research.grantResearchToPlayer(((ServerPlayer) player), researchReference);
                        }
                    });
                    inspectProgress = 0;
                }
            } else{
                inspectProgress = 0;
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

}
