package com.github.x3rdev.soul_forge.common.registry;

import com.github.x3rdev.soul_forge.common.entity.brain.MovingHitboxAttackPath;
import com.github.x3rdev.soul_forge.common.research.Research;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class DatapackRegistry {

    public static final ResourceKey<Registry<MovingHitboxAttackPath>> MOVING_HITBOX_ATTACK_PATH_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("moving_hitbox_attack_path"));
    public static final ResourceKey<Registry<Research>> RESEARCH_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("research"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MOVING_HITBOX_ATTACK_PATH_KEY, MovingHitboxAttackPath.CODEC);
        event.dataPackRegistry(RESEARCH_KEY, Research.CODEC, Research.CODEC);
    }
}
