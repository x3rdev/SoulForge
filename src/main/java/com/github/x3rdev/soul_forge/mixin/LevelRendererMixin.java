package com.github.x3rdev.soul_forge.mixin;

import com.github.x3rdev.soul_forge.client.ClientSetup;
import com.github.x3rdev.soul_forge.common.research.Research;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow protected abstract void renderEntity(Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource);

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = {"renderLevel", "method_22710"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V", ordinal = 0))
    private void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix,
                             CallbackInfo ci, @Local PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if(Research.playerHasResearchGlasses(mc.player)) {
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
            return Research.getCachedUnlockableResearch(Minecraft.getInstance().player, Minecraft.getInstance().level.registryAccess()).stream().anyMatch(
                    researchReference -> researchReference.value().unlockItemStack().is(itemEntity.getItem().getItem())
            );
        }
        return false;
    }
}
