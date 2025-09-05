package com.github.x3rdev.soul_forge.mixin;

import com.github.x3rdev.soul_forge.client.ClientSetup;
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
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
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

    @Inject(method = {"renderLevel", "method_22710"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", ordinal = 0))
    private void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                             CallbackInfo ci, @Local PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();

        ClientSetup.getOrCreateOverlayTarget().bindWrite(true);
        RenderSystem.clear(16640, Minecraft.ON_OSX);
        MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
        for (Entity entity : mc.level.entitiesForRendering()) {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(!mc.level.tickRateManager().isEntityFrozen(entity));
            if(entity instanceof ItemEntity itemEntity) {
//                renderEntity(
//                        entity,
//                        camera.getPosition().x,
//                        camera.getPosition().y,
//                        camera.getPosition().z,
//                        partialTick,
//                        poseStack,
//                        immediate);
                renderItem(itemEntity, partialTick, poseStack, camera, immediate);
            }
        }
        immediate.endLastBatch();
        ClientSetup.getOrCreateOverlayTarget().unbindWrite();
        mc.getMainRenderTarget().bindWrite(true);

    }

    private void renderItem(ItemEntity itemEntity, float partialTick, PoseStack poseStack, Camera camera, MultiBufferSource.BufferSource buffer) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super ItemEntity> entityrenderer = dispatcher.getRenderer(itemEntity);
        double d0 = Mth.lerp(partialTick, itemEntity.xOld, itemEntity.getX());
        double d1 = Mth.lerp(partialTick, itemEntity.yOld, itemEntity.getY());
        double d2 = Mth.lerp(partialTick, itemEntity.zOld, itemEntity.getZ());
        float yaw = Mth.lerp(partialTick, itemEntity.yRotO, itemEntity.getYRot());
        poseStack.pushPose();
        poseStack.translate(d0-camera.getPosition().x, d1-camera.getPosition().y, d2-camera.getPosition().z);
        entityrenderer.render(itemEntity, yaw, partialTick, poseStack, buffer, dispatcher.getPackedLightCoords(itemEntity, partialTick));
        poseStack.popPose();
    }
}
