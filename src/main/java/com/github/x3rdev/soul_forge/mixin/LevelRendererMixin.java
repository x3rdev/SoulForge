package com.github.x3rdev.soul_forge.mixin;

import com.github.x3rdev.soul_forge.client.ClientSetup;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow protected abstract void renderEntity(Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource);

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = {"renderLevel", "method_22710"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", ordinal = 0))
    private void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                             CallbackInfo ci, @Local PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if(ResearcherGlasses.playerHasResearcherGlassesEquipped(mc.player)) {
            ClientSetup.getOrCreateOverlayTarget().bindWrite(true);
            RenderSystem.clear(16640, Minecraft.ON_OSX);
            MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
            entityRenderDispatcher.setRenderShadow(false);
            for (Entity entity : mc.level.entitiesForRendering()) {
                if (camera.getPosition().distanceToSqr(entity.position()) < 64 * 64 && shouldHighlightEntity(entity)) {
                    float partialTick = deltaTracker.getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(entity));
                    renderEntity(
                            entity,
                            camera.getPosition().x,
                            camera.getPosition().y,
                            camera.getPosition().z,
                            partialTick,
                            poseStack,
                            immediate);
                    if (shouldRenderParticle(camera, entity)) {
                        mc.level.addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 0.25, entity.getZ(), 0, 0, 0);
                    }
                }
            }
            entityRenderDispatcher.setRenderShadow(true);
            immediate.endLastBatch();
            ClientSetup.getOrCreateOverlayTarget().unbindWrite();
            mc.getMainRenderTarget().bindWrite(true);
        }

    }

    private boolean shouldHighlightEntity(Entity entity) {
        if(entity instanceof ItemEntity itemEntity) {
            return Research.getCachedUnlockableResearch(Minecraft.getInstance().player).stream().anyMatch(
                    researchReference -> researchReference.value().unlockIngredient().test(itemEntity.getItem())
            );
        }
        return false;
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
