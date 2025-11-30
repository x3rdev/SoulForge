package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.block_entity.SoulCauldronBlockEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SoulCauldronRenderer extends GeoBlockRenderer<SoulCauldronBlockEntity> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/soul_cauldron_liquid.png");
    public static final int MAX_LIQUID_HEIGHT = 11;

    public SoulCauldronRenderer() {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul_cauldron")));
        addRenderLayer(new SoulGlowingLayer<>(this));
    }

    @Override
    public void renderFinal(PoseStack poseStack, SoulCauldronBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
        if(animatable.getSoulCount() > 0) {
            renderLiquid(poseStack, animatable, bufferSource, packedLight, colour, animatable.getSoulType(), animatable.getSoulCount());
        }
        renderLabel(poseStack, animatable, bufferSource, packedLight, partialTick);
    }

    private void renderLiquid(PoseStack poseStack, SoulCauldronBlockEntity animatable, MultiBufferSource bufferSource, int packedLight, int colour, SoulType soultype, int soulCount) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.eyes(TEXTURE));
        poseStack.pushPose();
        poseStack.scale(0.0625F, 0.0625F, 0.0625F);
        poseStack.translate(0, 0.001F, 0);
        PoseStack.Pose pose = poseStack.last();
        int liquidType = soultype.ordinal()-1;
        int liquidFrame = (int) (animatable.getLevel().getGameTime()/3 % 5);
        int liquidY = (soulCount*soultype.size()/SoulCauldronBlockEntity.MAX_CAPACITY)*MAX_LIQUID_HEIGHT + 4;
        consumer.addVertex(pose, 2, liquidY, 2).setUv(liquidType*14F/70, liquidFrame*14F/70).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        consumer.addVertex(pose, 2, liquidY, 14).setUv((liquidType+1)*14F/70, liquidFrame*14F/70).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        consumer.addVertex(pose, 14, liquidY, 14).setUv((liquidType+1)*14F/70, (liquidFrame+1)*14F/70).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        consumer.addVertex(pose, 14, liquidY, 2).setUv(liquidType*14F/70, (liquidFrame+1)*14F/70).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        poseStack.popPose();
    }

    private void renderLabel(PoseStack poseStack, SoulCauldronBlockEntity animatable, MultiBufferSource bufferSource, int packedLight, float partialTick) {
        LocalPlayer player = Minecraft.getInstance().player;
        Vec3 pos = animatable.getBlockPos().getCenter();
        if(player.distanceToSqr(pos.x, pos.y, pos.z) < 64) {
            HitResult pick = player.pick(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue(), partialTick, false);
            if(pick.getType() == HitResult.Type.BLOCK && ((BlockHitResult) pick).getBlockPos().equals(animatable.getBlockPos())) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.translate(0, 1, 0);
                poseStack.scale(0.025F, 0.025F, 0.025F);
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                poseStack.scale(1, -1, 1);
                Font font = Minecraft.getInstance().font;
                Component component = Component.literal(String.valueOf(animatable.getSoulCount()));
                font.drawInBatch(component, -font.width(component)/2F, 0, 0xFFFFFF, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
                poseStack.popPose();
            }
        }
    }
}
