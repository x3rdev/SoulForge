package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.SoulSteelShield;
import com.github.x3rdev.soul_forge.common.registry.DataComponentRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SoulSteelShieldRenderer extends GeoItemRenderer<SoulSteelShield> {

    public SoulSteelShieldRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul_steel_shield")));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        if(stack.get(DataComponentRegistry.SHIELD_BLOCKING)) {
            if(transformType.equals(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) || transformType.equals(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(45));
                poseStack.translate(-0.75, 0, 0);
            }
            if(transformType.equals(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) || transformType.equals(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)) {
                poseStack.translate(-0.25, 0, 0);
            }
        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }




}
