package com.github.x3rdev.soul_forge.mixin;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.ClientSetup;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.entity.Ghost;
import com.github.x3rdev.soul_forge.common.entity.nergal.Nergal;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Unique
    private static final ResourceLocation FOOTSTEP_TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/soul_bricks.png");

    @Shadow protected abstract void renderEntity(Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource);

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow public abstract void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix);

    @Shadow protected abstract MeshData buildClouds(Tesselator tesselator, double x, double y, double z, Vec3 cloudColor);

    @Inject(method = {"renderLevel", "method_22710"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", ordinal = 0))
    private void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                             CallbackInfo ci, @Local PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if(ResearcherGlasses.playerHasResearcherGlassesEquipped(mc.player)) {
            ClientSetup.getOrCreateOverlayTarget().bindWrite(true);
            RenderSystem.clear(16640, Minecraft.ON_OSX);
            MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
            entityRenderDispatcher.setRenderShadow(false);
//            for (Entity entity : mc.level.entitiesForRendering()) {
//                if (camera.getPosition().distanceToSqr(entity.position()) < 64 * 64 && shouldHighlightEntity(entity)) {
//                    float partialTick = deltaTracker.getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(entity));
//                    renderEntity(
//                            entity,
//                            camera.getPosition().x,
//                            camera.getPosition().y,
//                            camera.getPosition().z,
//                            partialTick,
//                            poseStack,
//                            immediate);
//                    if (shouldRenderParticle(camera, entity)) {
//                        mc.level.addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 0.25, entity.getZ(), 0, 0, 0);
//                    }
//                }
//            }
            entityRenderDispatcher.setRenderShadow(true);
            renderFootstep(mc.player.position().toVector3f(), poseStack, immediate);
            immediate.endLastBatch();
            ClientSetup.getOrCreateOverlayTarget().unbindWrite();
            mc.getMainRenderTarget().bindWrite(true);
        }

    }

    private void renderFootstep(Vector3f pos, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.translate(0, -1, 0);
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = bufferSource.getBuffer(ShaderRegistry.soul(FOOTSTEP_TEXTURE_LOCATION));
        consumer.addVertex(pose, -1, 0, -1).setColor(0xFFFFFFFF).setUv(0, 0).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, -1, 0);
        consumer.addVertex(pose, 1, 0, -1).setColor(0xFFFFFFFF).setUv(1, 0).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, -1, 0);
        consumer.addVertex(pose, 1, 0, 1).setColor(0xFFFFFFFF).setUv(1, 1).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, -1, 0);
        consumer.addVertex(pose, -1, 0, 1).setColor(0xFFFFFFFF).setUv(0, 1).setLight(LightTexture.FULL_BRIGHT).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, -1, 0);
        poseStack.popPose();
    }

    private boolean shouldRenderParticle(Camera camera, Entity entity) {
        Vec3 cameraLookVec = new Vec3(camera.getLookVector());
        Vec3 pos = camera.getPosition();
        Vec3 entityPos = entity.getPosition(camera.getPartialTickTime());
        // entity center defined as origin
        Vec3 relativePos = new Vec3(pos.x - entityPos.x, pos.y - entityPos.y - 0.25, pos.z - entityPos.z).scale(-1);

        return minDistance(cameraLookVec, relativePos) < Mth.square(0.125) * 3;
    }

    // lv = camera viewing vector, t = translation (relative position to target entity)
    private double minDistance(Vec3 cameraLookVec, Vec3 vecToTarget) {
        double arg = -1 * (cameraLookVec.x * vecToTarget.x + cameraLookVec.y * vecToTarget.y + cameraLookVec.z * vecToTarget.z) /
                (Mth.square(cameraLookVec.x) + Mth.square(cameraLookVec.y) + Mth.square(cameraLookVec.z));
        // returns minimally sized sphere around entity which contains the viewing vector

        return Mth.square(cameraLookVec.x * arg + vecToTarget.x) + Mth.square(cameraLookVec.y * arg + vecToTarget.y) + Mth.square(cameraLookVec.z * arg + vecToTarget.z);
    }

}
