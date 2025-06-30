package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.block_entity.SoulStorageBlockEntity;
import com.github.x3rdev.soul_forge.common.item.SoulBottle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoBlockRenderer;
import software.bernie.geckolib.util.Color;

public class SoulStorageRenderer extends DynamicGeoBlockRenderer<SoulStorageBlockEntity> {

    private static final ResourceLocation CHAIN_LOCATION = ResourceLocation.withDefaultNamespace("textures/block/chain.png");

    public SoulStorageRenderer() {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul_storage")));
    }

    @Override
    public void renderRecursively(PoseStack poseStack, SoulStorageBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        int replacedColor = colour;
        if(bone.getName().equals("crystal")) {
            poseStack.translate(0, animatable.getCrystalHeight(partialTick), 0);
            poseStack.scale(1,1,1);
            if(overrideCrystalColor(animatable, partialTick)) {
                replacedColor = Color.RED.getColor();
            }
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, replacedColor);
        poseStack.popPose();
    }

    private boolean overrideCrystalColor(SoulStorageBlockEntity animatable, float partialTick) {
        Player player = Minecraft.getInstance().player;
        if(player.getMainHandItem().getItem() instanceof SoulBottle) {
            Vec3 lookVec = player.getLookAngle().normalize();
            AABB box = AABB.ofSize(animatable.getBlockPos().getCenter().add(0, animatable.getCrystalHeight(partialTick), 0), 1.5, 1.5, 1.5);
            return box.clip(player.getEyePosition(partialTick), player.getEyePosition(partialTick).add(lookVec.scale(10))).isPresent();
        }
        return false;
    }

    @Override
    protected @Nullable RenderType getRenderTypeOverrideForBone(GeoBone bone, SoulStorageBlockEntity animatable, ResourceLocation texturePath, MultiBufferSource bufferSource, float partialTick) {
        if(bone.getName().equals("crystal")) {
            return ShaderRegistry.soul(texturePath);
        }
        return super.getRenderTypeOverrideForBone(bone, animatable, texturePath, bufferSource, partialTick);
    }

    @Override
    public void renderFinal(PoseStack poseStack, SoulStorageBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int color) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, color);
        VertexConsumer chainBuffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(CHAIN_LOCATION));
        Vec3 crystalPos = new Vec3(0, animatable.getCrystalHeight(partialTick), 0);
        renderChain(new Vec3(0,0.5,0), crystalPos, poseStack, animatable, chainBuffer, color);
        renderChain(crystalPos, new Vec3(1, 0, 0), poseStack, animatable, chainBuffer, color);
        renderChain(crystalPos, new Vec3(0, 0, 1), poseStack, animatable, chainBuffer, color);
        renderChain(crystalPos, new Vec3(-1, 0, 0), poseStack, animatable, chainBuffer, color);
        renderChain(crystalPos, new Vec3(0, 0, -1), poseStack, animatable, chainBuffer, color);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.translate(0, 1.5+animatable.getCrystalHeight(partialTick), 0);
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(0.05F, -0.05F, 0.05F);
        Font font = Minecraft.getInstance().font;
        Component component = Component.literal("Type: " + animatable.getSoulType() + "\nCount: " + animatable.getSoulCount());
        font.drawInBatch(component, -font.width(component)/2F, 0, 0xFFFFFF, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
        poseStack.popPose();
    }

    private void renderChain(Vec3 start, Vec3 end, PoseStack poseStack, SoulStorageBlockEntity animatable, VertexConsumer buffer, int color) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        PoseStack.Pose pose = poseStack.last();
        Vec3 direction = start.vectorTo(end);
        Vec3 t = direction.normalize();
        Vec3 up = Math.abs(t.y) < 0.999 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 n = t.cross(up).normalize().scale(3F/32);
        Vec3 b = n.cross(t).normalize().scale(3F/32);
        for (int i = 0; i < direction.length(); i++) {
            float lengthProgress = (float) (direction.length() - i);
            float extra = lengthProgress < 1 ? lengthProgress : 1;
            Vec3 v1 = start.add(t.scale(i));
            Vec3 v2 = start.add(t.scale(i+extra));
            addChainVertex(animatable, buffer, pose, v1.add(b), color, 0, 0, n);
            addChainVertex(animatable, buffer, pose, v1.subtract(b), color, 3F/16, 0, n.reverse());
            addChainVertex(animatable, buffer, pose, v2.subtract(b), color, 3F/16, 1*extra, n);
            addChainVertex(animatable, buffer, pose, v2.add(b), color, 0, 1*extra, n.reverse());
            addChainVertex(animatable, buffer, pose, v1.add(n), color, 3F/16, 0, b);
            addChainVertex(animatable, buffer, pose, v1.subtract(n), color, 6F/16, 0, b.reverse());
            addChainVertex(animatable, buffer, pose, v2.subtract(n), color, 6F/16, 1*extra, b);
            addChainVertex(animatable, buffer, pose, v2.add(n), color, 3F/16, 1*extra, b.reverse());
        }
        poseStack.popPose();
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
