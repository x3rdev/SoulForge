package com.github.x3rdev.soul_forge.common;

import com.github.x3rdev.soul_forge.common.datagen.SoulForgeEntityTagsProvider;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulTypes;
import com.github.x3rdev.soul_forge.common.entity.WispEntity;
import com.github.x3rdev.soul_forge.common.item.SoulScytheItem;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.ItemRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class CommonSetup {

    public static void attributeSetup(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.WISP.get(), WispEntity.createAttributes());
    }
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        if(!level.isClientSide()) {
            if(event.getSource().getEntity() instanceof Player player) {
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if(stack.getItem() instanceof SoulScytheItem) {
                    if(isTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_UNDEAD_SOUL)) {
                        dropUndeadSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                        return;
                    }
                    if(isTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_NETHER_SOUL)) {
                        dropNetherSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                        return;
                    }
                    if(isTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_ENDER_SOUL)) {
                        dropEnderSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                        return;
                    }
                    if(isTypeInTag(event.getEntity(), SoulForgeEntityTagsProvider.DROPS_DRAGON_SOUL)) {
                        dropDragonSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                        return;
                    }
                    dropNormalSoul(level, event.getEntity().position().add(0, 1.25F, 0));
                }
            }
        }
    }

    private static boolean isTypeInTag(LivingEntity entity, TagKey<EntityType<?>> tagKey) {
        return entity.getType().is(tagKey);
    }


    private static void dropUndeadSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.UNDEAD_SOUL.get(), level, SoulTypes.UNDEAD_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropNetherSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.NETHER_SOUL.get(), level, SoulTypes.NETHER_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropEnderSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.ENDER_SOUL.get(), level, SoulTypes.ENDER_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropDragonSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.DRAGON_SOUL.get(), level, SoulTypes.DRAGON_SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }
    private static void dropNormalSoul(Level level, Vec3 pos) {
        SoulEntity soulEntity = new SoulEntity(EntityRegistry.SOUL.get(), level, SoulTypes.SOUL);
        soulEntity.setPos(pos);
        level.addFreshEntity(soulEntity);
    }

}
