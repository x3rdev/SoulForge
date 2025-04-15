package com.github.x3rdev.soul_forge.client;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.particle.SoulParticle.MyParticleProvider;
import com.github.x3rdev.soul_forge.client.renderer.block.DarkTombRenderer;
import com.github.x3rdev.soul_forge.client.renderer.block.StatueRenderer;
import com.github.x3rdev.soul_forge.client.renderer.entity.*;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;


public class ClientSetup {

    @Nullable
    private static ShaderInstance soulShader;

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
        event.registerSpriteSet(ModParticles.SOUL_PARTICLE.get(), MyParticleProvider::new);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        ResourceProvider provider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(provider, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
                soulShader = shaderInstance;
            });
        } catch (IOException e) {
            SoulForge.LOGGER.warn("Failed to load shader", e);
        }
    }

    public static ShaderInstance getSoulShader() {
        return Objects.requireNonNull(soulShader, "Attempted to get shader before they have finished loading.");
    }
}
