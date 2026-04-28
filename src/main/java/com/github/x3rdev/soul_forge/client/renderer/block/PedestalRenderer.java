package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.block_entity.PedestalBlockEntity;
import com.github.x3rdev.soul_forge.common.registry.ParticleRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class PedestalRenderer extends GeoBlockRenderer<PedestalBlockEntity> {

    public static final int RITUAL_DURATION = PedestalBlockEntity.RITUAL_DURATION;
    public static final ResourceLocation ACTIVATED_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/pedestal_activated.png");
    public static final ResourceLocation SPELL_EXPLOSION_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/pedestal_explosion.png");
    public static final ResourceLocation SPELL_WHIRL_LOCATION = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/block/pedestal_whirl.png");

    private final ItemRenderer itemRenderer;

    public PedestalRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "pedestal")));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void renderFinal(PoseStack poseStack, PedestalBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        renderItem(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
        if(animatable.isRitualActive() && animatable.isMasterPedestal()) {
            masterPedestalRender(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
        }
    }

    private void renderItem(PoseStack poseStack, PedestalBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        if(animatable.isRitualActive()) {
            poseStack.translate(0.5, 1.25, 0.5);
            Vec3 ritualTranslations = getRitualTranslations(animatable, partialTick);
            if(animatable.getRitualTicks() < 40) {
                poseStack.translate(
                        0.025 * Mth.sin((float) RenderUtil.getCurrentTick() * 2.0F + 5) * Mth.sin((float) RenderUtil.getCurrentTick() * 4F),
                        0.125 * Mth.sin((float) (RenderUtil.getCurrentTick() * 0.1F)),
                        0.025 * Mth.sin((float) RenderUtil.getCurrentTick() * 2.0F + 15) * Mth.sin((float) RenderUtil.getCurrentTick() * 3F));
            } else {
                if(animatable.getRitualTicks() < RITUAL_DURATION - 40 - 30 && !animatable.getTheItem().isEmpty() && !animatable.isMasterPedestal()) {
                    Vec3 particlePos = animatable.getBlockPos().getCenter().add(ritualTranslations).add(0, 0.75, 0);
                    animatable.getLevel().addParticle(ParticleRegistry.RITUAL_TRAIL.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
                }
            }
            poseStack.translate(ritualTranslations.x, ritualTranslations.y, ritualTranslations.z);
            poseStack.scale(1.1F, 1.1F, 1.1F);
            Vec3 pedestalToPlayer = animatable.getBlockPos().getCenter().vectorTo(Minecraft.getInstance().player.position());
            poseStack.mulPose(Axis.YP.rotation((float) Mth.atan2(pedestalToPlayer.x, pedestalToPlayer.z)));
        } else {
            poseStack.translate(0.5, 1.0 + 0.125 * Mth.sin((float) (RenderUtil.getCurrentTick() * 0.1F)), 0.5);
            poseStack.scale(1.1F, 1.1F, 1.1F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) RenderUtil.getCurrentTick()));
        }
        itemRenderer.renderStatic(animatable.getTheItem(), ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, animatable.getLevel(), animatable.hashCode());
        poseStack.popPose();
    }

    private void masterPedestalRender(PoseStack poseStack, PedestalBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0001, 0.5);
        float angle = (animatable.getRitualTicks()+partialTick)*Mth.clamp((animatable.getRitualTicks()+partialTick)/30F, 1F, 10F);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        if(animatable.getRitualTicks() < RITUAL_DURATION - 22) {
            PoseStack.Pose whirlPose = poseStack.last();
            VertexConsumer whirlConsumer = bufferSource.getBuffer(ShaderRegistry.soul(SPELL_WHIRL_LOCATION));
            whirlConsumer.addVertex(whirlPose, -5, 0, -5).setColor(colour).setUv(0, 0).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            whirlConsumer.addVertex(whirlPose, 5, 0, -5).setColor(colour).setUv(1, 0).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            whirlConsumer.addVertex(whirlPose, 5, 0, 5).setColor(colour).setUv(1, 1).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            whirlConsumer.addVertex(whirlPose, -5, 0, 5).setColor(colour).setUv(0, 1).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
        }
        poseStack.popPose();
        Vec3 u = animatable.getBlockPos().getCenter().add(new Vec3(3.75, 0, 0).yRot(angle*Mth.DEG_TO_RAD));
        Vec3 v = animatable.getBlockPos().getCenter().add(new Vec3(-3.75, 0, 0).yRot(angle*Mth.DEG_TO_RAD));
        if(animatable.getRitualTicks() < RITUAL_DURATION - 40 - 30) {
            animatable.getLevel().addParticle(ParticleRegistry.RITUAL_TRAIL.get(), u.x, u.y - 0.05, u.z, Math.random() * 0.01, Math.random() * 0.05F, Math.random() * 0.01);
            animatable.getLevel().addParticle(ParticleRegistry.RITUAL_TRAIL.get(), v.x, v.y - 0.05, v.z, Math.random() * 0.01, Math.random() * 0.05F, Math.random() * 0.01);
        }
        if(animatable.getRitualTicks() == RITUAL_DURATION - 22) {
            animatable.getLevel().playSound(Minecraft.getInstance().player, animatable.getBlockPos(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 0.6F, 1.5F);
        }
        if(animatable.getRitualTicks() > RITUAL_DURATION - 22  && animatable.getRitualTicks() < RITUAL_DURATION) {
            poseStack.pushPose();
            poseStack.translate(0.5, 2, 0.5);
            int progress = animatable.getRitualTicks()/2;
            Vec3 storageToPlayer = animatable.getBlockPos().getCenter().vectorTo(Minecraft.getInstance().player.position());
            poseStack.mulPose(Axis.YP.rotation((float) Mth.atan2(storageToPlayer.x, storageToPlayer.z)));
            PoseStack.Pose explosionPose = poseStack.last();
            VertexConsumer explosionConsumer = bufferSource.getBuffer(RenderType.entityCutout(SPELL_EXPLOSION_LOCATION));
            explosionConsumer.addVertex(explosionPose, -5, -5, 0).setColor(colour).setUv(0, progress/11F).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            explosionConsumer.addVertex(explosionPose, 5, -5, 0).setColor(colour).setUv(1, progress/11F).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            explosionConsumer.addVertex(explosionPose, 5, 5, 0).setColor(colour).setUv(1, (progress+1)/11F).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            explosionConsumer.addVertex(explosionPose, -5, 5, 0).setColor(colour).setUv(0, (progress+1)/11F).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0, 1, 0);
            poseStack.popPose();
        }
    }

    private Vec3 getRitualTranslations(PedestalBlockEntity animatable, float partialTick) {
        if(animatable.getRitualTicks() < 40) {
            return Vec3.ZERO;
        } else {
            Vec3 finalTarget;
            if(animatable.getRitualParentPos() != null) {
                finalTarget = animatable.getBlockPos().getCenter().vectorTo(animatable.getRitualParentPos().getCenter().add(0, 1.5, 0));
            } else {
                finalTarget = new Vec3(0, 1.5, 0);
            }
            float scale = Math.min(0,(animatable.getRitualTicks()+partialTick-200F)/160F);
            return finalTarget.add(finalTarget.scale(scale).yRot((animatable.getRitualTicks()+partialTick-40)/20F));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(PedestalBlockEntity animatable) {
        if(animatable.isRitualActive()) {
            return ACTIVATED_LOCATION;
        }
        return super.getTextureLocation(animatable);
    }

    @Override
    public AABB getRenderBoundingBox(PedestalBlockEntity blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(2);
    }
}
