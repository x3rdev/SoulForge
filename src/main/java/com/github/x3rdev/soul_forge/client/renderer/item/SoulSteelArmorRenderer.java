package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.SoulScytheItem;
import com.github.x3rdev.soul_forge.common.item.SoulSteelArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SoulSteelArmorRenderer extends GeoArmorRenderer<SoulSteelArmorItem> {
    public SoulSteelArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "armor/soul_steel")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
