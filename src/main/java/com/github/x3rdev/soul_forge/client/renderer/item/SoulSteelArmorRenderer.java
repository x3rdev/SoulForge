package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.SoulSteelArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SoulSteelArmorRenderer extends GeoArmorRenderer<SoulSteelArmorItem> {
    public SoulSteelArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "armor/soul_steel")));
    }
}
