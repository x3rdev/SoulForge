package com.github.x3rdev.soul_forge.client.renderer.entity;

import com.github.x3rdev.soul_forge.SoulForge;
import com.github.x3rdev.soul_forge.common.entity.SoulEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class UndeadSoulRenderer extends BaseSoulRenderer {

    private static final ResourceLocation FLAME_TEXTURE = new ResourceLocation(SoulForge.MOD_ID, "textures/entity/undead_soul_flame.png");
    public UndeadSoulRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(SoulForge.MOD_ID, "undead_soul")));

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureOverrideForBone(GeoBone bone, SoulEntity animatable, float partialTick) {
        if(bone.getName().equals("flame")) {
            AnimatableTexture.setAndUpdate(FLAME_TEXTURE);
            return FLAME_TEXTURE;
        }
        return super.getTextureOverrideForBone(bone, animatable, partialTick);
    }
}
