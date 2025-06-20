package com.github.x3rdev.soul_forge.client.shader;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.ClientSetup;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class ShaderRegistry {

    //TODO if shaders running, default to rendertype_entity something emissive
    public static RenderType soul(ResourceLocation location) {
        return Internal.soul(location);
    }

    public static ResourceLocation researcherGlassesPostEffect() {
        return ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "shaders/post/researcher_glasses.json");
    }

    public static final ParticleRenderType RITUAL_TRAIL = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.setShader(ClientSetup::getRitualTrailShader);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return "RITUAL_TRAIL";
        }
    };

    private static class Internal extends RenderType {

        public Internal(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
            super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
        }

        private static RenderType soul(ResourceLocation texture) {
            return create("soul",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    1536,
                    true,
                    true,
                    RenderType.CompositeState.builder()
                            .setShaderState(new ShaderStateShard(ClientSetup::getSoulShader))
                            .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setCullState(NO_CULL)
                            .setWriteMaskState(new WriteMaskStateShard(true, true))
                            .setOverlayState(OVERLAY)
                            .createCompositeState(false)
            );
        }
    }
}
