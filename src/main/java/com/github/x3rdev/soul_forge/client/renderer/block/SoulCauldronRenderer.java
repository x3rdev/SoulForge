package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.block_entity.SoulCauldronBlockEntity;
import com.github.x3rdev.soul_forge.common.entity.SoulType;
import com.github.x3rdev.soul_forge.common.item.SoulBottle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.specialty.DynamicGeoBlockRenderer;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.RenderUtil;

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
            renderLiquid(poseStack, bufferSource, packedLight, packedOverlay, colour, animatable.getSoulType(), animatable.getSoulCount());
        }
        renderLabel(partialTick);
    }

    private void renderLiquid(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int colour, SoulType soultype, int soulCount) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entitySolid(TEXTURE));
        poseStack.pushPose();
        poseStack.scale(0.0625F, 0.0625F, 0.0625F);
        poseStack.translate(0, 0.001F, 0);
        PoseStack.Pose pose = poseStack.last();
        int liquidHeight = (soulCount*soultype.size()/SoulCauldronBlockEntity.MAX_CAPACITY)*MAX_LIQUID_HEIGHT + 4;
        consumer.addVertex(pose, 2, liquidHeight, 2).setUv(0, 0).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        consumer.addVertex(pose, 2, liquidHeight, 14).setUv(1, 0).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        consumer.addVertex(pose, 14, liquidHeight, 14).setUv(1, 1).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        consumer.addVertex(pose, 14, liquidHeight, 2).setUv(0, 1).setColor(colour).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        poseStack.popPose();
    }

    private void renderLabel(float partialTick) {
        LocalPlayer player = Minecraft.getInstance().player;
        Vec3 pos = animatable.getBlockPos().getCenter();
        if(player.distanceToSqr(pos.x, pos.y, pos.z) < 64) {
            player.pick(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue(), partialTick, false);

        }
    }
}
