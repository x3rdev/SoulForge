package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.layer.SoulGlowingLayer;
import com.github.x3rdev.soul_forge.common.block_entity.SoulAnvilBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SoulAnvilRenderer extends GeoBlockRenderer<SoulAnvilBlockEntity> {

    public SoulAnvilRenderer() {
        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "soul_anvil")));
        addRenderLayer(new SoulGlowingLayer<>(this));
    }
}
