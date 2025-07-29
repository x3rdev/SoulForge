package com.github.x3rdev.soul_forge.client.renderer.item;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.item.Scythe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ScytheRenderer extends GeoItemRenderer<Scythe> {

    public static final ResourceLocation WOODEN = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/item/wooden_scythe.png");
    public static final ResourceLocation STONE = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/item/stone_scythe.png");
    public static final ResourceLocation IRON = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/item/iron_scythe.png");
    public static final ResourceLocation GOLDEN = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/item/golden_scythe.png");
    public static final ResourceLocation DIAMOND = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/item/diamond_scythe.png");
    public static final ResourceLocation NETHERITE = ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "textures/item/netherite_scythe.png");

    public ScytheRenderer() {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "scythe")));
    }

    @Override
    public ResourceLocation getTextureLocation(Scythe animatable) {
        switch (animatable.getTier()) {
            case Tiers.WOOD -> {
                return WOODEN;
            }
            case Tiers.STONE -> {
                return STONE;
            }
            case Tiers.IRON -> {
                return IRON;
            }
            case Tiers.GOLD -> {
                return GOLDEN;
            }
            case Tiers.DIAMOND -> {
                return DIAMOND;
            }
            case Tiers.NETHERITE -> {
                return NETHERITE;
            }
            default -> throw new IllegalStateException("Unexpected value: " + animatable.getTier());
        }
    }
}
