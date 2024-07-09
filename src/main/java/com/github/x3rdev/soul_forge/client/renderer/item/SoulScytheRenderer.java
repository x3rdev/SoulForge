package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.SoulScytheItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SoulScytheRenderer extends GeoItemRenderer<SoulScytheItem> {
    public SoulScytheRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "soul_scythe")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            protected RenderType getRenderType(SoulScytheItem animatable) {
                return RenderType.eyes(new ResourceLocation(SoulForge.MOD_ID, "textures/item/soul_scythe_glowmask.png"));
            }
        });
    }
}
