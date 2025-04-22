package com.github.x3rdev.soul_forge.common.entity.ai.moving_hitbox_attack;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class MovingHitboxAttackRegistry {

    public static final ResourceKey<Registry<MovingHitboxAttackPath>> MOVING_HITBOX_ATTACK_PATH_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("moving_hitbox_attack_path"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MOVING_HITBOX_ATTACK_PATH_KEY, MovingHitboxAttackPath.CODEC);
    }
}
