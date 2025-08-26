package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.item.AwakenedSoulSteelArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AwakenedSoulSteelArmorRenderer extends GeoArmorRenderer<AwakenedSoulSteelArmor> {
    public AwakenedSoulSteelArmorRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "armor/awakened_soul_steel")));
        addRenderLayer(new SoulGlowingLayer<>(this));
    }
}
