package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.item.SoulSteelSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SoulSteelSwordRenderer extends GeoItemRenderer<SoulSteelSword> {

    public SoulSteelSwordRenderer(GeoModel<SoulSteelSword> model, boolean awakened) {
        super(model);
        if(awakened) {
            addRenderLayer(new SoulGlowingLayer<>(this));
        }
    }
}
