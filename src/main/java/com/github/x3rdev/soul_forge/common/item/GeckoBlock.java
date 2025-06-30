package com.github.x3rdev.soul_forge.common.item;

import com.github.x3rdev.soul_forge.client.model.DefaultedBlockItemGeoModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GeckoBlock extends BlockItem implements GeoItem {

    private BlockEntityWithoutLevelRenderer renderer;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GeckoBlock(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if(renderer == null) {
                    renderer = new GeoItemRenderer<GeckoBlock>(new DefaultedBlockItemGeoModel<>(BuiltInRegistries.ITEM.getKey(GeckoBlock.this)));
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}