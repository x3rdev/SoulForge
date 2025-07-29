package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.entity.GhostEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GhostRenderer extends GeoEntityRenderer<GhostEntity> {

    public GhostRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "ghost")));
    }

    @Override
    public @Nullable RenderType getRenderType(GhostEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return ShaderRegistry.soul(texture);
    }
}
