package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.GhostEntity;
import com.github.x3rdev.soul_forge.common.entity.nergal.NergalEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class NergalRenderer extends GeoEntityRenderer<NergalEntity> {

    public NergalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "nergal")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected @NotNull RenderType getRenderType(NergalEntity animatable, @Nullable MultiBufferSource bufferSource) {
                return RenderType.eyes(AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable)));
            }
        });
    }
}