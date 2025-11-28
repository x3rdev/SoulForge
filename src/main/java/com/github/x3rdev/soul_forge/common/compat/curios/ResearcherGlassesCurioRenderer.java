package com.github.x3rdev.soul_forge.common.compat.curios;

import com.github.x3rdev.soul_forge.client.renderer.item.ResearcherGlassesRenderer;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ResearcherGlassesCurioRenderer extends ResearcherGlassesRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource, int packedLight, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if(stack.getItem() instanceof ResearcherGlasses animatable) {
            this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, (HumanoidModel<?>) renderLayerParent.getModel(), bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.armorCutoutNoCull(getTextureLocation(animatable)));
            this.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}
