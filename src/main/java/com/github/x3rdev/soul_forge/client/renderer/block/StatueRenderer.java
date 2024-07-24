package com.github.x3rdev.soul_forge.client.renderer.block;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.block_entity.StatueBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StatueRenderer extends GeoBlockRenderer<StatueBlockEntity> {

    public StatueRenderer() {
        super(new DefaultedBlockGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "statue")));
    }
}
