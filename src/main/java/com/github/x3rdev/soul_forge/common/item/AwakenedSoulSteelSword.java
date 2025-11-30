package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.client.renderer.item.SoulSteelSwordRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

import java.util.function.Consumer;

public class AwakenedSoulSteelSword extends SoulSteelSword {

    public AwakenedSoulSteelSword(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private SoulSteelSwordRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SoulSteelSwordRenderer(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "awakened_soul_steel_sword")), true);
                }
                return renderer;
            }
        });
    }
}
