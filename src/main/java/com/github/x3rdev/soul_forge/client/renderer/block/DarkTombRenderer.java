package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.DarkTombBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DarkTombRenderer extends GeoBlockRenderer<DarkTombBlockEntity> {

    public DarkTombRenderer() {
        super(new DefaultedBlockGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "dark_tomb")));
    }

}
