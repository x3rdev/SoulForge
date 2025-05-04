package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.common.entity.ai.MovingHitboxAttackPath;
import com.github.x3rdev.soul_forge.common.item.ObservationItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class DatapackRegistry {

    public static final ResourceKey<Registry<MovingHitboxAttackPath>> MOVING_HITBOX_ATTACK_PATH_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("moving_hitbox_attack_path"));
    public static final ResourceKey<Registry<ObservationItem.Observation>> OBSERVATION_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("research/observation"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MOVING_HITBOX_ATTACK_PATH_KEY, MovingHitboxAttackPath.CODEC);
        event.dataPackRegistry(OBSERVATION_KEY, ObservationItem.Observation.CODEC);
    }
}
