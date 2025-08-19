package com.github.x3rdev.soul_forge.client.renderer.layer;

import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SoulGlowingLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T> {

    public SoulGlowingLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    protected @Nullable RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
        return ShaderRegistry.soul(AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable)));
    }
}
