package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.NergalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NergalRenderer extends GeoEntityRenderer<NergalEntity> {

    public NergalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "nergal")));
    }

}
