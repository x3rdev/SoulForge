package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.item.AwakenedSoulSteelShield;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AwakenedSoulsteelShieldRenderer extends GeoItemRenderer<AwakenedSoulSteelShield> {

    public AwakenedSoulsteelShieldRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "awakened_soul_steel_shield")));
        addRenderLayer(new SoulGlowingLayer<>(this));
    }
}
