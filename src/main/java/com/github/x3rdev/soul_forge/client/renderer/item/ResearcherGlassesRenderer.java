package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ResearcherGlassesRenderer extends GeoArmorRenderer<ResearcherGlasses> implements ICurioRenderer {

    public ResearcherGlassesRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "armor/researcher_glasses")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @SubscribeEvent
    public static void renderGlassesOverlay(RenderFrameEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.getCameraType().isFirstPerson()) {
            Player player = minecraft.player;
            if(player != null) {
                if(ResearcherGlasses.playerHasResearcherGlassesEquipped(player)) {
                    if(!isGlassesEffectLoaded()) {
                        Minecraft.getInstance().gameRenderer.loadEffect(ShaderRegistry.researcherGlassesPostEffect());
                    }
                } else {
                    if(isGlassesEffectLoaded()) {
                        Minecraft.getInstance().gameRenderer.shutdownEffect();
                    }
                }
            }
        }
    }

    private static boolean isGlassesEffectLoaded() {
        PostChain postEffect = Minecraft.getInstance().gameRenderer.postEffect;
        return postEffect != null && postEffect.getName().equals(ShaderRegistry.researcherGlassesPostEffect().toString());
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource, int packedLight, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if(stack.getItem() instanceof ResearcherGlasses animatable) {
            this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, (HumanoidModel<?>) renderLayerParent.getModel(), bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.armorCutoutNoCull(getTextureLocation(animatable)));
            this.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}
