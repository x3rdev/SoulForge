package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.RitualAltarBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class RitualAltarRenderer extends GeoBlockRenderer<RitualAltarBlockEntity> {

    private final ItemRenderer itemRenderer;

    public RitualAltarRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "ritual_altar")));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void renderFinal(PoseStack poseStack, RitualAltarBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        renderItem(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }

    private void renderItem(PoseStack poseStack, RitualAltarBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1 + 0.125 * Mth.sin((float) (RenderUtil.getCurrentTick() * 0.1F)), 0.5);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) RenderUtil.getCurrentTick()));
        itemRenderer.renderStatic(animatable.getTheItem(), ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, animatable.getLevel(), animatable.hashCode());
        poseStack.popPose();
    }
}
