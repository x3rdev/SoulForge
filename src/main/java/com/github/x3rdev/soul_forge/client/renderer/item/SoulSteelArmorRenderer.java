package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.item.SoulSteelArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SoulSteelArmorRenderer extends GeoArmorRenderer<SoulSteelArmor> {
    public SoulSteelArmorRenderer(GeoModel<SoulSteelArmor> model) {
        super(model);
        addRenderLayer(new SoulGlowingLayer<>(this));
    }
}
