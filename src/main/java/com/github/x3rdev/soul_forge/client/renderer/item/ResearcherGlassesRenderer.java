package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.shader.ShaderRegistry;
import com.github.x3rdev.soul_forge.common.item.ResearcherGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ResearcherGlassesRenderer extends GeoArmorRenderer<ResearcherGlasses> {

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
                ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD); //TODO curios compat?
                if(stack.getItem() instanceof ResearcherGlasses) {
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

    public static boolean isGlassesEffectLoaded() {
        PostChain postEffect = Minecraft.getInstance().gameRenderer.postEffect;
        return postEffect != null && postEffect.getName().equals(ShaderRegistry.researcherGlassesPostEffect().toString());
    }
}
