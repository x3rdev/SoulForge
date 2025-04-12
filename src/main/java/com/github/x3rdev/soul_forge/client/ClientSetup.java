package com.github.x3rdev.soul_forge.client;

import com.github.x3rdev.soul_forge.client.particle.ModParticles;
import com.github.x3rdev.soul_forge.client.particle.SoulParticle;
import com.github.x3rdev.soul_forge.client.renderer.block.DarkTombRenderer;
import com.github.x3rdev.soul_forge.client.renderer.block.StatueRenderer;
import com.github.x3rdev.soul_forge.client.renderer.entity.*;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;


public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SOUL.get(), SoulRenderer::new);
        event.registerEntityRenderer(EntityRegistry.UNDEAD_SOUL.get(), UndeadSoulRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NETHER_SOUL.get(), NetherSoulRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ENDER_SOUL.get(), EnderSoulRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DRAGON_SOUL.get(), DragonSoulRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SOUL_SCYTHE_PROJECTILE.get(), SoulScytheProjectileRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WISP.get(), WispRenderer::new);
        event.registerEntityRenderer(EntityRegistry.GHOST.get(), GhostRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NERGAL.get(), NergalRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityRegistry.DARK_TOMB.get(), pContext -> new DarkTombRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE.get(), pContext -> new StatueRenderer());
    }

    @SubscribeEvent
    public static void registerParticleProvider(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SOUL_PARTICLE.get(), SoulParticle.MyParticleProvider::new);
    }
}
