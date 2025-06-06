package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
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
        Vec3[] chainDirections = chainDirections(animatable.getBlockPos(), 4);
        for (int i = 0; i < chainDirections.length; i++) {
            Vec3 vec3 = chainDirections[i];
            renderChain(poseStack, animatable, chainBuffer, vec3, color, i);
        }
        poseStack.popPose();
    }

    public static Vec3[] chainDirections(BlockPos pos, int chainCount) {
        Vec3[] chainDirections = new Vec3[chainCount];
        int hash = pos.hashCode();
        float start = Mth.PI * Mth.sin(hash+3F);
        for (int i = 0; i < chainCount; i++) {
            float rand = Mth.sin(hash*i+10F);
            float angle = Mth.DEG_TO_RAD * (75 + 10 * rand);
            Vec3 vec = new Vec3(1, 0, 0).zRot(angle).yRot(start+0.4F*rand+(i*Mth.TWO_PI/chainCount));
            chainDirections[i] = vec;
        }
        return chainDirections;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void renderChain(PoseStack poseStack, SoulStorageBlockEntity animatable, VertexConsumer buffer, Vec3 direction, int color, int chainIndex) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        PoseStack.Pose pose = poseStack.last();
        Vec3 t = direction.normalize();
        Vec3 n = t.cross(new Vec3(0, 1, 0)).normalize().scale(3F/32);
        Vec3 b = n.cross(t).normalize().scale(3F/32);
        Vec3 v1 = new Vec3(0, -0.001F, 0);
        Vec3 v2 = v1.add(t);
        int renderIterations = 0;
        while (renderIterations < maxIterations(animatable, chainIndex) && animatable.getLevel().isEmptyBlock(animatable.getBlockPos().offset(BlockPos.containing(v1)))) {
            addChainVertex(animatable, buffer, pose, v1.add(b), color, 0, 0, n);
            addChainVertex(animatable, buffer, pose, v1.subtract(b), color, 3F/16, 0, n.reverse());
            addChainVertex(animatable, buffer, pose, v2.subtract(b), color, 3F/16, 1, n);
            addChainVertex(animatable, buffer, pose, v2.add(b), color, 0, 1, n.reverse());
            addChainVertex(animatable, buffer, pose, v1.add(n), color, 3F/16, 0, b);
            addChainVertex(animatable, buffer, pose, v1.subtract(n), color, 6F/16, 0, b.reverse());
            addChainVertex(animatable, buffer, pose, v2.subtract(n), color, 6F/16, 1, b);
            addChainVertex(animatable, buffer, pose, v2.add(n), color, 3F/16, 1, b.reverse());
            v1 = v1.add(t);
            v2 = v2.add(t);
            renderIterations++;
        }
        poseStack.popPose();
    }

    private int maxIterations(SoulStorageBlockEntity animatable, int chainIndex) {
        double tick = animatable.getTick();
        int ticksUntilExtended = 10 * 20;
        if(tick < ticksUntilExtended*(chainIndex+1)) {
            return (int) (tick-1.5F*chainIndex*chainIndex-10F);
        } else {
            return 64;
        }
    }

    private void addChainVertex(SoulStorageBlockEntity animatable, VertexConsumer buffer, PoseStack.Pose pose, Vec3 pos, int color, float u, float v, Vec3 normal) {
        int newPackedLight = LevelRenderer.getLightColor(animatable.getLevel(), BlockPos.containing(pos).offset(animatable.getBlockPos()));
        buffer.addVertex(pose, pos.toVector3f()).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(newPackedLight).setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z);
    }

    @Override
    public AABB getRenderBoundingBox(SoulStorageBlockEntity blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(10);
    }
}
