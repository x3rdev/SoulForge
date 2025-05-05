package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.entity.nergal.NergalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.function.Supplier;

public class NergalRenderer extends GeoEntityRenderer<NergalEntity> {

    private final Supplier<Boolean> renderDebugHitbox;

    public NergalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "nergal")));
        addRenderLayer(new SoulGlowingLayer<>(this));
        renderDebugHitbox = () -> renderManager.getEntityRenderDispatcher().shouldRenderHitBoxes();
    }

    @Override
    public void renderFinal(PoseStack poseStack, NergalEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
        if(renderDebugHitbox.get()) {
            LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.LINES), animatable.getEntityData().get(NergalEntity.DEBUG_ATTACK_BOX), 1, 1, 1, 1);
        }
    }
}