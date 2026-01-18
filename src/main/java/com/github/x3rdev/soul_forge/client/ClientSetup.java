package com.github.x3rdev.soul_forge.client;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.keymapping.KeyMappings;
import com.github.x3rdev.soul_forge.client.particle.RitualTrailParticle;
import com.github.x3rdev.soul_forge.client.particle.SoulParticle;
import com.github.x3rdev.soul_forge.client.renderer.block.*;
import com.github.x3rdev.soul_forge.client.renderer.entity.*;
import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.client.screen.SoulAnvilScreen;
import com.github.x3rdev.soul_forge.common.compat.ModCompatibility;
import com.github.x3rdev.soul_forge.common.compat.curios.CuriosCompat;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import com.github.x3rdev.soul_forge.common.item.WingItem;
import com.github.x3rdev.soul_forge.common.registry.BlockEntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.EntityRegistry;
import com.github.x3rdev.soul_forge.common.registry.MenuTypeRegistry;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        if(ModCompatibility.curiosModPresent()) {
            CuriosCompat.registerCuriosRenderers();
        }
    }

    @Nullable
    private static ShaderInstance soulShader;
    @Nullable
    private static ShaderInstance ritualTrailShader;
    
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SOUL.get(), SoulRenderer::new);
//        event.registerEntityRenderer(EntityRegistry.UNDEAD_SOUL.get(), UndeadSoulRenderer::new);
//        event.registerEntityRenderer(EntityRegistry.NETHER_SOUL.get(), NetherSoulRenderer::new);
//        event.registerEntityRenderer(EntityRegistry.ENDER_SOUL.get(), EnderSoulRenderer::new);
//        event.registerEntityRenderer(EntityRegistry.DRAGON_SOUL.get(), DragonSoulRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SOUL_SCYTHE_PROJECTILE.get(), SoulScytheProjectileRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WISP.get(), WispRenderer::new);
        event.registerEntityRenderer(EntityRegistry.GHOST.get(), GhostRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NERGAL.get(), NergalRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NERGAL_SPAWN.get(), NergalSpawnRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SPARK.get(), SparkRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityRegistry.DARK_TOMB.get(), context -> new DarkTombRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE.get(), context -> new StatueRenderer());
        event.registerBlockEntityRenderer(BlockEntityRegistry.PEDESTAL.get(), PedestalRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.SOUL_CAULDRON.get(), context -> new SoulCauldronRenderer());
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

    private static RenderTarget overlayTarget;

    public static RenderTarget getOrCreateOverlayTarget() {
        if(overlayTarget == null) {
            Minecraft mc = Minecraft.getInstance();
            int width = mc.getWindow().getWidth();
            int height = mc.getWindow().getHeight();
            overlayTarget = new TextureTarget(width, height, false, Minecraft.ON_OSX);
        }
        return overlayTarget;
    }

    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Post event) {
        if (KeyMappings.KEY_FLY.get().isDown()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getInventory().getArmor(2 /* chest */).getItem() instanceof WingItem item && !player.isFallFlying()) {
                player.setDeltaMovement(item.getBehavior(player, item));
            }
        }
    }
}