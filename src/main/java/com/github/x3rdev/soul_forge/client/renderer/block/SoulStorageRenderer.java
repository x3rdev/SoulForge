package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SoulStorageRenderer extends GeoBlockRenderer<SoulStorageBlockEntity> {

    private static final ResourceLocation CHAIN_LOCATION = ResourceLocation.withDefaultNamespace("textures/block/chain.png");

    public SoulStorageRenderer() {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul_storage")));
    }

    @Override
    public void renderFinal(PoseStack poseStack, SoulStorageBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int color) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, color);
        poseStack.pushPose();
        VertexConsumer chainBuffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(CHAIN_LOCATION));
        Vec3 direction = new Vec3(2, 1, 0).normalize();
        renderChain(poseStack, animatable, chainBuffer, direction, packedLight, color);
        poseStack.popPose();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void renderChain(PoseStack poseStack, SoulStorageBlockEntity animatable, VertexConsumer buffer, Vec3 direction, int packedLight, int color) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(45));
        PoseStack.Pose pose = poseStack.last();
        Vec3 t = direction.normalize();
        Vec3 n = new Vec3(t.y, -t.x, t.z).normalize().scale(3F/32);
        Vec3 b = n.cross(t).normalize().scale(3F/32);
        Vec3 v1 = Vec3.ZERO;
        Vec3 v2 = v1.add(t);
        int renderIterations = 0;
        while (renderIterations < 10) {
            addChainVertex(buffer, pose, v1.add(b), color, 0, 0, packedLight, n);
            addChainVertex(buffer, pose, v1.subtract(b), color, 3F/16, 0, packedLight, n.reverse());
            addChainVertex(buffer, pose, v2.subtract(b), color, 3F/16, 1, packedLight, n);
            addChainVertex(buffer, pose, v2.add(b), color, 0, 1, packedLight, n.reverse());
            addChainVertex(buffer, pose, v1.add(n), color, 3F/16, 0, packedLight, b);
            addChainVertex(buffer, pose, v1.subtract(n), color, 6F/16, 0, packedLight, b.reverse());
            addChainVertex(buffer, pose, v2.subtract(n), color, 6F/16, 1, packedLight, b);
            addChainVertex(buffer, pose, v2.add(n), color, 3F/16, 1, packedLight, b.reverse());
            v1 = v1.add(t);
            v2 = v2.add(t);
            renderIterations++;
        }
        poseStack.popPose();
    }

    private void addChainVertex(VertexConsumer buffer, PoseStack.Pose pose, Vec3 pos, int color, float u, float v, int packedLight, Vec3 normal) {
        buffer.addVertex(pose, pos.toVector3f()).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z);
    }

    @Override
    public boolean shouldRender(SoulStorageBlockEntity blockEntity, Vec3 cameraPos) {
        return super.shouldRender(blockEntity, cameraPos);
    }
}
