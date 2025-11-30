package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.item.SoulSteelShield;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SoulSteelShieldRenderer extends GeoItemRenderer<SoulSteelShield> {

    public SoulSteelShieldRenderer(GeoModel<SoulSteelShield> model, boolean awakened) {
        super(model);
        if(awakened) {
            addRenderLayer(new SoulGlowingLayer<>(this));
        }
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        if(stack.get(DataComponentRegistry.SHIELD_BLOCKING)) {
            if(transformType.equals(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(70));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-15));
                poseStack.mulPose(Axis.XP.rotationDegrees(20));
                poseStack.translate(-1.2, 0, -0.25);
            }
            if(transformType.equals(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)) {
                poseStack.translate(1.2, 0, 0.25);
                poseStack.mulPose(Axis.YP.rotationDegrees(-70));
                poseStack.mulPose(Axis.ZP.rotationDegrees(15));
                poseStack.mulPose(Axis.XP.rotationDegrees(20));
            }
            if(transformType.equals(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)) {
                poseStack.translate(-0.25, 0, 0);
            }
            if(transformType.equals(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)) {
                poseStack.translate(0.25, 0, 0);
            }
        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }

}
