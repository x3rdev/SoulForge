package com.github.x3rdev.soul_forge.client;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.particle.RitualTrailParticle;
import com.github.x3rdev.soul_forge.client.particle.SoulParticle;
import com.github.x3rdev.soul_forge.client.renderer.block.*;
import com.github.x3rdev.soul_forge.client.renderer.entity.*;
import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.client.screen.SoulAnvilScreen;
import com.github.x3rdev.soul_forge.common.registry.*;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;


public class ClientSetup {

    @Nullable
    private static ShaderInstance soulShader;
    @Nullable
    private static ShaderInstance ritualTrailShader;


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
        event.registerEntityRenderer(EntityRegistry.NERGAL_SPAWN.get(), NergalSpawnRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityRegistry.DARK_TOMB.get(), context -> new DarkTombRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE.get(), context -> new StatueRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SOUL_STORAGE.get(), context -> new SoulStorageRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.SOUL_ANVIL.get(), context -> new SoulAnvilRenderer());
    }

    @SubscribeEvent
    public static void registerParticleProvider(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.SOUL_PARTICLE.get(), SoulParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.RITUAL_TRAIL.get(), RitualTrailParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        ResourceProvider provider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(provider, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul"), DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
                soulShader = shaderInstance;
            });
            event.registerShader(new ShaderInstance(provider, ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "ritual_trail"), DefaultVertexFormat.PARTICLE), shaderInstance -> {
                ritualTrailShader = shaderInstance;
            });
        } catch (IOException e) {
            SoulForge.LOGGER.warn("Failed to load shader", e);
        }
    }

    public static ShaderInstance getSoulShader() {
        return Objects.requireNonNull(soulShader, "Attempted to get shader before they have finished loading.");
    }

    public static ShaderInstance getRitualTrailShader() {
        return Objects.requireNonNull(ritualTrailShader, "Attempted to get shader before they have finished loading.");
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        event.register(MenuTypeRegistry.SOUL_ANVIL.get(), SoulAnvilScreen::new);
    }

}
