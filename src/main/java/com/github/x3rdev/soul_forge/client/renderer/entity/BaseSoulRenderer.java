package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoEntityRenderer;

public abstract class BaseSoulRenderer extends DynamicGeoEntityRenderer<SoulEntity> {

    protected BaseSoulRenderer(EntityRendererProvider.Context renderManager, GeoModel<SoulEntity> model) {
        super(renderManager, model);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, SoulEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        poseStack.scale(1.5F, 1.5F, 1.5F);
        if(!animatable.isFake()) {
            poseStack.translate(0, 0.15 * Mth.sin((float) (Blaze3D.getTime()) + animatable.hashCode()) + 0.5, 0);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        }
        poseStack.translate(0, -0.5, 0);
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }

    @Override
    public RenderType getRenderType(SoulEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return ShaderRegistry.soul(texture);
    }

    @Override
    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        IntIntPair pair = super.computeTextureSize(texture);
        if(pair.leftInt()/pair.rightInt() != 1) {
            return IntIntPair.of(pair.leftInt(), pair.leftInt());
        }
        return pair;
    }
}
