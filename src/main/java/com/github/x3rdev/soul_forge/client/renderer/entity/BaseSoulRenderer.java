package com.github.x3rdev.soul_forge.client.renderer.entity;

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
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;

public class BaseSoulRenderer extends DynamicGeoEntityRenderer<SoulEntity> {
    public BaseSoulRenderer(EntityRendererProvider.Context renderManager, GeoModel<SoulEntity> model) {
        super(renderManager, model);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, SoulEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0, 0.15*Mth.sin((float) (Blaze3D.getTime()))+0.5, 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.translate(0, -0.5, 0);

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, 0, packedOverlay, 0.5F, 0.5F, 0.5F, alpha);


        poseStack.popPose();
    }

    @Override
    public RenderType getRenderType(SoulEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(texture);
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
