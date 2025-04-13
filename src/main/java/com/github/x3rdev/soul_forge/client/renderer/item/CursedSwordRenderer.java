package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.CursedSwordItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CursedSwordRenderer extends GeoItemRenderer<CursedSwordItem> {
    public CursedSwordRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "cursed_sword")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected @NotNull RenderType getRenderType(CursedSwordItem animatable, @Nullable MultiBufferSource bufferSource) {
                return RenderType.eyes(AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable)));
            }
        });
    }

}
