package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.item.AwakenedSoulSteelSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AwakenedSoulSteelSwordRenderer extends GeoItemRenderer<AwakenedSoulSteelSword> {

    public AwakenedSoulSteelSwordRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "awakened_soul_steel_sword")));
        addRenderLayer(new SoulGlowingLayer<>(this));
    }
}
