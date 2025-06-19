package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class PedestalRenderer extends GeoBlockRenderer<PedestalBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "pedestal")));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void renderFinal(PoseStack poseStack, PedestalBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        if(animatable.isRitualActive()) {
            poseStack.translate(
                    0.5+0.025*Mth.sin((float) RenderUtil.getCurrentTick()*2.5F+5)*Mth.sin((float) RenderUtil.getCurrentTick()*4F),
                    1.25 + 0.125 * Mth.sin((float) (RenderUtil.getCurrentTick() * 0.1F)),
                    0.5+0.025*Mth.sin((float) RenderUtil.getCurrentTick()*2.5F+15)*Mth.sin((float) RenderUtil.getCurrentTick()*3F));
            Vec3 ritualTranslations = getRitualTranslations(animatable);
            poseStack.translate(ritualTranslations.x, ritualTranslations.y, ritualTranslations.z);
            poseStack.scale(1.1F, 1.1F, 1.1F);
            Vec3 storageToPlayer = animatable.getBlockPos().getCenter().vectorTo(Minecraft.getInstance().player.position());
            poseStack.mulPose(Axis.YP.rotation((float) Mth.atan2(storageToPlayer.x, storageToPlayer.z)));
        } else {
            poseStack.translate(0.5, 1.25 + 0.125 * Mth.sin((float) (RenderUtil.getCurrentTick() * 0.1F)), 0.5);
            poseStack.scale(1.1F, 1.1F, 1.1F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) RenderUtil.getCurrentTick()));
        }
        itemRenderer.renderStatic(animatable.getTheItem(), ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, animatable.getLevel(), animatable.hashCode());
        poseStack.popPose();
    }

    private Vec3 getRitualTranslations(PedestalBlockEntity animatable) {
        if(animatable.getRitualTicks() < 40) {
            return Vec3.ZERO;
        } else {
            Vec3 finalTarget;
            if(animatable.getRitualParentPos() != null) {
                finalTarget = animatable.getBlockPos().getCenter().vectorTo(animatable.getRitualParentPos().getCenter().add(0, 1.5, 0));
            } else {
                finalTarget = new Vec3(0, 1.5, 0);
            }
            return finalTarget.scale(1-(200F-animatable.getRitualTicks())/160F);
        }
    }
}
