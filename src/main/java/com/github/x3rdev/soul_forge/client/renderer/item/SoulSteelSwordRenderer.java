package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.SoulSteelSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SoulSteelSwordRenderer extends GeoItemRenderer<SoulSteelSword> {

    public SoulSteelSwordRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul_steel_sword")));
    }
}
